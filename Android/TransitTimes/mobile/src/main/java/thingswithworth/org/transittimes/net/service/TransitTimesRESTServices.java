package thingswithworth.org.transittimes.net.service;

import android.app.Application;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import thingswithworth.org.transittimes.model.Route;
import thingswithworth.org.transittimes.model.RouteTypeDeserializer;
import thingswithworth.org.transittimes.net.interfaces.AgencyService;
import thingswithworth.org.transittimes.net.interfaces.RouteService;

/**
 * Created by Alex on 8/27/2015.
 */
public class TransitTimesRESTServices
{
    private static final String BASE_URL = "http://hackmyride.cloudapp.net:8000";
    private String TAG = "REST";
    public AgencyService agencyService;
    public RouteService routeService;
    private static TransitTimesRESTServices service;
    private static Application context;
    private long SIZE_OF_CACHE = 1024 * 1024 * 10;

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
            return null;
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

        RestAdapter adapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setClient(new OkClient(caching_client))
                .setConverter(new GsonConverter(gson))
                .build();


        agencyService = adapter.create(AgencyService.class);
        routeService = adapter.create(RouteService.class);
    }


}
