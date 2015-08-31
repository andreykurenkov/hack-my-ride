package thingswithworth.org.transittimes.net.interfaces;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;
import thingswithworth.org.transittimes.model.Agency;

/**
 * Created by Alex on 8/27/2015.
 */
public interface AgencyService
{
    @GET("/api/agencies")
    public Observable<List<Agency>> getAgencies();

    @GET("api/agency/{agency_id}")
    public Observable<Agency> getAgency(@Path("agency_id")int id);
}
