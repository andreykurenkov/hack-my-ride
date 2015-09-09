package thingswithworth.org.transittimes.net.interfaces;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;
import thingswithworth.org.transittimes.model.Stop;
import thingswithworth.org.transittimes.model.Trip;

/**
 * Created by andrey on 9/5/15.
 */
public interface TripService {
    @GET("/api/trip/{trip_id}")
    public Observable<Trip> getTrip(@Path("trip_id") int trip_id);

    @GET("/api/trip/{trip_id}/stops")
    public Observable<List<Stop>> getStopsForTrip(@Path("trip_id") int trip_id);
}
