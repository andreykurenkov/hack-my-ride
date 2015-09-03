package thingswithworth.org.transittimes.net.interfaces;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;
import thingswithworth.org.transittimes.model.Route;
import thingswithworth.org.transittimes.model.Stop;
import thingswithworth.org.transittimes.model.StopTime;

/**
 * Created by andrey on 9/3/15.
 */
public interface StopService {
    @GET("/api/stop/{stop_id}")
    public Observable<Stop> getStop(@Path("stop_id") int stop_id);

    @GET("/api/stop/{stop_id}/times")
    public Observable<List<StopTime>> getStopTimesAtStop(@Path("stop_id") int stop_id);

    @GET("/api/stop/{stop_id}/stoptimeafter/{time}")
    public Observable<StopTime> getNextStopTimeAtStop(@Path("stop_id") int stop_id,@Path("time") int time);

}
