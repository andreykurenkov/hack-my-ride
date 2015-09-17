package thingswithworth.org.transittimes.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.RetrofitError;
import rx.Observable;
import thingswithworth.org.transittimes.R;
import thingswithworth.org.transittimes.model.SecondsPosixTime;
import thingswithworth.org.transittimes.model.SecondsTime;
import thingswithworth.org.transittimes.model.Stop;
import thingswithworth.org.transittimes.model.StopTime;
import thingswithworth.org.transittimes.model.Trip;
import thingswithworth.org.transittimes.net.events.OpenRouteRequest;
import thingswithworth.org.transittimes.net.service.RestUtil;
import thingswithworth.org.transittimes.net.service.TransitTimesRESTServices;

/**
 * Created by Alex on 9/2/2015.
 */
public class StopDetailFragment extends Fragment implements OnMapReadyCallback
{
    private Stop mCurrentStop;

    @Bind(R.id.titleView)
    TextView mTitleView;

    @Bind(R.id.detailView)
    TextView mDetailView;

    @Bind(R.id.firstStopTime)
    TextView firstStopTimeView;

    @Bind(R.id.secondStopTime)
    TextView secondStopTimeView;

    @Bind(R.id.thirdStopTime)
    TextView thirdStopTimeView;

    @Bind(R.id.fourthStopTime)
    TextView fourthStopTimeView;

    @Bind(R.id.fifthStopTime)
    TextView fifthStopTimeView;

    @Bind(R.id.scheduleContainer)
    LinearLayout mContainer;

    GoogleMap mMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stop_detail, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        SupportMapFragment fragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
        refreshView();
    }

    public void updateStopAndRefresh(Stop s)
    {
        mCurrentStop = s;
        refreshView();
    }

    public void updateStop(Stop s)
    {
        mCurrentStop = s;
    }

    private void refreshView()
    {
        if(mCurrentStop!=null && mTitleView!=null)
        {
            mTitleView.setText(mCurrentStop.getName().replace("(0)", "(outbound)").replace("(1)", "(inbound)"));

            int currentTotalSeconds = RestUtil.getSecondsOfDay();
            int day = RestUtil.getDayOfWeek();
            final TextView[] textViews = {firstStopTimeView, secondStopTimeView, thirdStopTimeView, fourthStopTimeView, fifthStopTimeView};
            if(mCurrentStop.getStopTimes()!=null && !textViews[0].getText().toString().contains("scheduled")) {
                for (int i = 0; i < Math.min(mCurrentStop.getStopTimes().size(),3); i++) {
                    StopTime stopTime = mCurrentStop.getStopTimes().get(i);
                    if(stopTime.getTripData()!=null){
                        final int index = i;
                        String str = stopTime.getTripData().getHeadsign() + " scheduled: " + stopTime.getDeparture_time().toString(false, false);
                        getActivity().runOnUiThread(() -> {
                            textViews[index].setText(str);
                            textViews[index].invalidate();
                        });
                    }
                }
            }
            TransitTimesRESTServices.getInstance().stopService.getNextStopTimesAtStop(mCurrentStop.getId(), currentTotalSeconds, 6,day).subscribe(
                    (stop_times) -> {
                        mCurrentStop.setStopTimes(stop_times);
                        if (stop_times.size() == 0) {
                            mDetailView.setText("Departure time is not available");
                            mContainer.setVisibility(View.GONE);
                        } else {
                            for (int i = 0; i < 5; i++) {
                                final int index = i;
                                StopTime stopTime = stop_times.get(i);
                                TransitTimesRESTServices.getInstance().tripService.getTrip(stopTime.getTrip()).subscribe((trip)-> {
                                               stopTime.setTripData(trip);
                                                String str = trip.getHeadsign() + " scheduled: " + stopTime.getDeparture_time().toString(false, false);
                                                getActivity().runOnUiThread(() -> {
                                                       textViews[index].setText(str);
                                                        textViews[index].invalidate();
                                                    });

                                    try {

                                        TransitTimesRESTServices.getInstance().stopTimeService.getRealTimeStopTimesPrediction(stopTime.getId()).subscribe(real_time_stop -> {
                                            stopTime.setRealtime(real_time_stop.getTimeOfDay());
                                            getActivity().runOnUiThread(() -> {
                                                textViews[index].setText(str + ", predicted: " + stopTime.getRealtime().toString(false, false));
                                                textViews[index].invalidate();
                                            });
                                        });
                                    }catch(RetrofitError e){
                                        Log.e("StopDetailFragment","Can't get real time",e);
                                    }
                                    });
                            }
                        }
                    });
            if (mMap != null) {
                updateMap();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        if(mCurrentStop!=null)
        {
           updateMap();
        }
    }
    private void updateMap()
    {
        LatLng point = new LatLng(mCurrentStop.getPoint().getCoordinates()[1], mCurrentStop.getPoint().getCoordinates()[0]);
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(point)
                .title(mCurrentStop.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 13));
    }
}
