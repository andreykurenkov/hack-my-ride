package thingswithworth.org.transittimes.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.Bind;
import butterknife.ButterKnife;
import thingswithworth.org.transittimes.R;
import thingswithworth.org.transittimes.model.Stop;

/**
 * Created by Alex on 9/2/2015.
 */
public class StopDetailFragment extends Fragment implements OnMapReadyCallback
{
    private Stop mCurrentStop;

    @Bind(R.id.titleView)
    TextView mTitleView;

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
        if(mCurrentStop!=null)
            mTitleView.setText(mCurrentStop.getName());

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
