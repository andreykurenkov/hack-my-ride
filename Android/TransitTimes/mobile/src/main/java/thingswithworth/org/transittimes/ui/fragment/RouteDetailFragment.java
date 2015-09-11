package thingswithworth.org.transittimes.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import thingswithworth.org.transittimes.R;
import thingswithworth.org.transittimes.model.Route;
import thingswithworth.org.transittimes.model.Stop;

/**
 * Created by Alex on 8/29/2015.
 */
public class RouteDetailFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener
{
    GoogleMap mMap;

    private Route mLoadedRoute;

    private StopDetailFragment stopDetailFragment;

    private HashMap<Marker, Stop> marker_to_stop;

    public RouteDetailFragment()
    {
        stopDetailFragment = new StopDetailFragment();
        marker_to_stop = new HashMap<>();
    }

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

        getChildFragmentManager().beginTransaction()
                .add(R.id.container, stopDetailFragment)
                .commit();

    }

    public void updateRoute(Route route)
    {
        mLoadedRoute = route;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        marker_to_stop.clear();
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        if(mLoadedRoute!=null)
        {
            if(mLoadedRoute.getStops()!=null)
            {
                LatLngBounds.Builder bounds = new LatLngBounds.Builder();
                for(Stop s: mLoadedRoute.getStops())
                {
                    LatLng coordinate = new LatLng(s.getPoint().getCoordinates()[1], s.getPoint().getCoordinates()[0]);
                   marker_to_stop.put(mMap.addMarker(new MarkerOptions()
                        .position(coordinate)
                        .title(s.getName())), s);

                    bounds.include(coordinate);
                }


                PolylineOptions routeOptions = new PolylineOptions();
                if(mLoadedRoute.getGeometry()!=null) {
                    for (double[][] d : mLoadedRoute.getGeometry().getCoordinates()) {
                        for (int i = 0; i < d.length; i++) {
                            double[] point = d[i];
                            LatLng coordinate = new LatLng(point[1], point[0]);
                            routeOptions.add(coordinate);
                            bounds.include(coordinate);
                        }
                    }
                    mMap.addPolyline(routeOptions);


                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50));
                mMap.setMyLocationEnabled(true);
                stopDetailFragment.updateStopAndRefresh(mLoadedRoute.getStops().get(0));
            }

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        marker.showInfoWindow();
        Stop s = marker_to_stop.get(marker);
        stopDetailFragment.updateStopAndRefresh(s);
        return true;
    }
}
