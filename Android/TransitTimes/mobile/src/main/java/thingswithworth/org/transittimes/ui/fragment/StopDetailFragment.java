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
import rx.Observable;
import thingswithworth.org.transittimes.R;
import thingswithworth.org.transittimes.model.SecondsPosixTime;
import thingswithworth.org.transittimes.model.SecondsTime;
import thingswithworth.org.transittimes.model.Stop;
import thingswithworth.org.transittimes.model.StopTime;
import thingswithworth.org.transittimes.model.Trip;
import thingswithworth.org.transittimes.net.events.OpenRouteRequest;
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

            GregorianCalendar cal = new GregorianCalendar();
            int currentSecond = cal.get(Calendar.SECOND);
            int currentMinute = cal.get(Calendar.MINUTE);
            int currentHour = cal.get(Calendar.HOUR_OF_DAY);
            int currentTotalSeconds = currentSecond + currentMinute * 60 + currentHour * 3600;
            final TextView[] textViews = {firstStopTimeView, secondStopTimeView, thirdStopTimeView, fourthStopTimeView, fifthStopTimeView};

            TransitTimesRESTServices.getInstance().stopService.getNextStopTimesAtStop(mCurrentStop.getId(), currentTotalSeconds, 6).subscribe(
                    (stop_times) -> {
                        if (stop_times.size() == 0) {
                            mDetailView.setText("Departure time is not available");
                            mContainer.setVisibility(View.GONE);
                        } else {
                            for (int i = 0; i < 5; i++) {
                                final int index = i;
                                StopTime stopTime = stop_times.get(i);
                                Observable.zip(
                                    TransitTimesRESTServices.getInstance().tripService.getTrip(stopTime.getTrip()),
                                    TransitTimesRESTServices.getInstance().stopTimeService.getRealTimeStopTimesPrediction(stopTime.getId()),
                                    (trip, real_time) -> {
                                        if(stopTime.getDeparture_time()!=null)
                                            return trip.getHeadsign() + " scheduled: "+stopTime.getDeparture_time().toString(false, false)+", predicted: "+real_time.getTimeOfDay();
                                        else
                                            return trip.getHeadsign() + " predicted: "+real_time.getTimeOfDay().toString(false,false);
                                    })
                                    .subscribe(
                                            (str)-> getActivity().runOnUiThread(() -> {textViews[index].setText(str); textViews[index].invalidate();}),
                                            (error)->{Log.e("StopDetailFragment", error.getMessage()); getActivity().runOnUiThread(() -> {textViews[index].setText("No real-time data is available"); textViews[index].invalidate();});}

                                 );
                            }
                        }});
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
