package thingswithworth.org.transittimes.net.serivice;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import thingswithworth.org.transittimes.model.Route;
import thingswithworth.org.transittimes.model.RouteTypeDeserializer;
import thingswithworth.org.transittimes.net.interfaces.AgencyService;
import thingswithworth.org.transittimes.net.interfaces.RouteService;

/**
 * Created by Alex on 8/27/2015.
 */
public class TransitTimesService
{
    private static final String BASE_URL = "http://hackmyride.cloudapp.net:8000";
    public AgencyService agencyService;
    public RouteService routeService;
    private static TransitTimesService service;


    public static TransitTimesService getInstance()
    {
        if(service==null)
        {
            service = new TransitTimesService();
        }
        return service;
    }

    private TransitTimesService()
    {
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
                .setConverter(new GsonConverter(gson))
                .build();


        agencyService = adapter.create(AgencyService.class);
        routeService = adapter.create(RouteService.class);
    }
}
