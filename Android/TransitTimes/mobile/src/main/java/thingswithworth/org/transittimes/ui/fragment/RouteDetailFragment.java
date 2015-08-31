package thingswithworth.org.transittimes.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.Bind;
import butterknife.ButterKnife;
import thingswithworth.org.transittimes.R;
import thingswithworth.org.transittimes.model.Route;
import thingswithworth.org.transittimes.model.Stop;

/**
 * Created by Alex on 8/29/2015.
 */
public class RouteDetailFragment extends Fragment implements OnMapReadyCallback
{
    GoogleMap mMap;

    private Route mLoadedRoute;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_route_detail, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        SupportMapFragment fragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

    public void updateRoute(Route route)
    {
        mLoadedRoute = route;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        if(mLoadedRoute!=null)
        {
            if(mLoadedRoute.getStops()!=null)
            {
                for(Stop s: mLoadedRoute.getStops())
                {
                    mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(s.getPoint().getCoordinates()[1], s.getPoint().getCoordinates()[0]))
                        .title(s.getName())
                    );
                }
            }
        }
    }
}
