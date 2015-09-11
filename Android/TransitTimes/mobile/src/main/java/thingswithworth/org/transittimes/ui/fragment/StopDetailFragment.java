package thingswithworth.org.transittimes.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import thingswithworth.org.transittimes.R;
import thingswithworth.org.transittimes.model.Stop;
import thingswithworth.org.transittimes.model.StopTime;
import thingswithworth.org.transittimes.net.service.TransitTimesRESTServices;

/**
 * Created by Alex on 9/2/2015.
 */
public class StopDetailFragment extends Fragment implements OnMapReadyCallback
{
    private Stop mCurrentStop;
    private List<StopTime> mNextStopTimes;

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

        if(mCurrentStop!=null) {
            mTitleView.setText(mCurrentStop.getName());

            GregorianCalendar cal = new GregorianCalendar();
            int currentSecond =cal.get(Calendar.SECOND);
            int currentMinute =cal.get(Calendar.MINUTE);
            int currentHour =cal.get(Calendar.HOUR);
            int currentTotalSeconds = currentSecond + currentMinute*60 + currentHour*3600;

            TransitTimesRESTServices.getInstance().stopService.getNextStopTimesAtStop(mCurrentStop.getId(), currentTotalSeconds, 6).subscribe(
                    (stop_times)->{

                        List<StopTime> stopTimes = stop_times;


                        getActivity().runOnUiThread(() ->
                        {
                            if(stopTimes.size()==0)
                            {
                                mDetailView.setText("Departure time is not available");
                                mContainer.setVisibility(View.GONE);
                            }
                            else {
                                try {
                                    mContainer.setVisibility(View.VISIBLE);
                                    mDetailView.setText("Next Departure Time: " + stopTimes.get(0).getDeparture_time().toString(false, false));
                                    firstStopTimeView.setText(stopTimes.get(1).getDeparture_time().toString(false, false));
                                    secondStopTimeView.setText(stopTimes.get(2).getDeparture_time().toString(false, false));
                                    thirdStopTimeView.setText(stopTimes.get(3).getDeparture_time().toString(false, false));
                                    fourthStopTimeView.setText(stopTimes.get(4).getDeparture_time().toString(false, false));
                                    fifthStopTimeView.setText(stopTimes.get(5).getDeparture_time().toString(false, false));
                                } catch (IndexOutOfBoundsException aioob) {

                                }
                            }
                        });

                    }
            );
        }

        if(mMap!=null)
        {
            updateMap();
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
