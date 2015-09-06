package thingswithworth.org.transittimes.net.service;

import android.app.Application;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import thingswithworth.org.transittimes.model.Route;
import thingswithworth.org.transittimes.model.RouteTypeDeserializer;
import thingswithworth.org.transittimes.net.interfaces.AgencyService;
import thingswithworth.org.transittimes.net.interfaces.RouteService;
import thingswithworth.org.transittimes.net.interfaces.StopService;
import thingswithworth.org.transittimes.net.interfaces.TripService;

/**
 * Created by Alex on 8/27/2015.
 */
public class TransitTimesRESTServices
{
    private static final String BASE_URL = "http://hackmyride.cloudapp.net:8000";
    private String TAG = "REST";
    public AgencyService agencyService;
    public RouteService routeService;
    public TripService tripService;
    public StopService stopService;

    private static TransitTimesRESTServices service;
    private static Application context;
    private long SIZE_OF_CACHE = 1024 * 1024 * 10;//10 Meg, not so much

    public static TransitTimesRESTServices init(Application context)
    {
        if(service==null)
        {
            service = new TransitTimesRESTServices(context);
        }
        return service;
    }

    public static TransitTimesRESTServices getInstance()
    {
        if(service==null)
        {
            throw new IllegalStateException("Trying to get TransitTimesRESTServices without init being called.");
        }
        return service;
    }

    private TransitTimesRESTServices(Application context)
    {
        this.context = context;
        OkHttpClient caching_client = new OkHttpClient();
        try {
            Cache responseCache = new Cache(context.getCacheDir(), SIZE_OF_CACHE);
            caching_client.setCache(responseCache);
        } catch (Exception e) {
            Log.d(TAG, "Unable to set http cache", e);
        }
        caching_client.setReadTimeout(30, TimeUnit.SECONDS);
        caching_client.setConnectTimeout(30, TimeUnit.SECONDS);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Route.RouteType.class, new RouteTypeDeserializer())
                .addDeserializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                        final Expose expose = fieldAttributes.getAnnotation(Expose.class);
                        return expose != null && !expose.deserialize();
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> aClass) {
                        return false;
                    }
                })
                .create();

        caching_client.networkInterceptors().add(mCacheControlInterceptor);

        // Create Executor
        Executor executor = Executors.newCachedThreadPool();

        RestAdapter adapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setExecutors(executor, executor)
                .setEndpoint(BASE_URL)
                .setClient(new OkClient(caching_client))
                .setConverter(new GsonConverter(gson))
                .build();


        agencyService = adapter.create(AgencyService.class);
        routeService = adapter.create(RouteService.class);
        stopService = adapter.create(StopService.class);
        tripService = adapter.create(TripService.class);
    }

    private static final Interceptor mCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //See https://docs.google.com/presentation/d/1eJa0gBZLpZRQ5vjW-eqLyekEgB54n4fQ1N4jDcgMZ1E/edit#slide=id.g75a45c04a_079
            Request request = chain.request();

            // Add Cache Control only for GET methods
            if (request.method().equals("GET")) {
                    // 4 weeks stale
                    request.newBuilder()
                            .header("Cache-Control", "public, max-stale=2419200")
                            .build();
            }

            Response response = chain.proceed(request);

            // Re-write response CC header to force use of cache
            //This should be on server side really
            return response.newBuilder()
                    .header("Cache-Control", "public, max-age=86400") // 1 day
                    .build();
        }
    };


}
