package thingswithworth.org.transittimes.net.interfaces;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;
import thingswithworth.org.transittimes.model.SecondsPosixTime;
import thingswithworth.org.transittimes.model.StopTime;

/**
 * Created by andrey on 9/13/15.
 */
public interface StopTimeService {
    @GET("/api/stop_time/{stop_time_id}")
    public Observable<StopTime> getStopTime(@Path("stop_time_id") int stop_time_id);

    @GET("/api/realtime/stop_time_departure/{stop_time_id}")
    public Observable<SecondsPosixTime> getRealTimeStopTimesPrediction(@Path("stop_time_id") int stop_time_id);
}
