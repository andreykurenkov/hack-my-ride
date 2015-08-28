package thingswithworth.org.transittimes.net.interfaces;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;
import thingswithworth.org.transittimes.model.Route;

/**
 * Created by Alex on 8/27/2015.
 */
public interface RouteService
{
    @GET("/api/agency/{agency_id}/routes")
    public Observable<List<Route>> getRoutes(@Path("agency_id") int agency_id);

    @GET("/api/route/{route_id}")
    public Observable<Route> getRoute(@Path("route_id") int route_id);
}
