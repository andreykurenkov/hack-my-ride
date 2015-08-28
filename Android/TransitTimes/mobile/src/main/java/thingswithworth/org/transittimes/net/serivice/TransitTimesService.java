package thingswithworth.org.transittimes.net.serivice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
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
        Gson gson = new GsonBuilder().create();

        RestAdapter adapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .build();


        agencyService = adapter.create(AgencyService.class);
        routeService = adapter.create(RouteService.class);
    }
}
