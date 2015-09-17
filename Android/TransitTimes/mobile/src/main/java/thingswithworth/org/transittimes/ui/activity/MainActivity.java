package thingswithworth.org.transittimes.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mikepenz.materialdrawer.Drawer;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import thingswithworth.org.transittimes.R;
import thingswithworth.org.transittimes.TransitTimesApplication;
import thingswithworth.org.transittimes.bluetooth.events.AtStopNotification;
import thingswithworth.org.transittimes.bluetooth.service.BeaconMonitoringService;
import thingswithworth.org.transittimes.net.events.LocationUpdateMessage;
import thingswithworth.org.transittimes.net.events.OpenPreferencesRequest;
import thingswithworth.org.transittimes.net.events.OpenRouteRequest;
import thingswithworth.org.transittimes.net.events.OpenStopRequest;
import thingswithworth.org.transittimes.ui.fragment.RouteDetailFragment;
import thingswithworth.org.transittimes.ui.fragment.StopDetailFragment;
import thingswithworth.org.transittimes.ui.fragment.TransitPreferenceFragment;
import thingswithworth.org.transittimes.ui.fragment.TransitSystemFragment;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, LocationListener {
    private static String TAG = "MainActivity";
    private TransitSystemFragment systemFragment;
    private RouteDetailFragment routeDetailFragment;
    private StopDetailFragment stopDetailFragment;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates = false;
    private Drawer.Result drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TransitTimesApplication.getBus().register(this);

        if (savedInstanceState == null) {
            systemFragment = new TransitSystemFragment();
            routeDetailFragment = new RouteDetailFragment();
            stopDetailFragment = new StopDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, systemFragment)
                    .commit();
        }


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mGoogleApiClient.isConnected() && !mRequestingLocationUpdates)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        if(getIntent().hasExtra("beacon_stop_id"))
        {
            Parcelable stopData = getIntent().getParcelableExtra("beacon_stop_id");
            if(BeaconMonitoringService.lastSeenStop!=null)
                stopDetailFragment.updateStopAndRefresh(BeaconMonitoringService.lastSeenStop);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, stopDetailFragment)
                    .addToBackStack("")
                    .commit();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mRequestingLocationUpdates = false;
    }

    @Subscribe
    public void onOpenRoute(OpenRouteRequest openRouteRequest)
    {
        Log.d(TAG,"New OpenRouteRequest seen: "+openRouteRequest.getRoute().getRoute_id());
        runOnUiThread(() -> {
            if (openRouteRequest.getDialog() != null) {
                openRouteRequest.getDialog().hide();
            }

            routeDetailFragment.updateRoute(openRouteRequest.getRoute());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, routeDetailFragment)
                    .addToBackStack("")
                    .commit();
        });
    }
    @Subscribe
    public void onOpenStop(OpenStopRequest openStopRequest)
    {
        Log.d(TAG,"New OpenStopRequest seen: "+openStopRequest.getStop().getStop_id());
        runOnUiThread(() -> {
            if (openStopRequest.getDialog() != null) {
                openStopRequest.getDialog().hide();
            }

            stopDetailFragment.updateStopAndRefresh(openStopRequest.getStop());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, stopDetailFragment)
                    .addToBackStack("")
                    .commit();
        });
    }

    @Subscribe
    public void onOpenPreference(OpenPreferencesRequest request)
    {
        runOnUiThread(() -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new TransitPreferenceFragment())
                    .addToBackStack("")
                    .commit();
        });
    }

    @Subscribe
    public void onStopNotification(AtStopNotification stopNotification)
    {
        Log.d(TAG, "Bus event for ID " + stopNotification.getStop().getStop_id());
        systemFragment.updateBeaconStop(stopNotification.getStop());
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        Location mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastKnownLocation!=null)
        {
            TransitTimesApplication.getBus().post(new LocationUpdateMessage(mLastKnownLocation));
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(30000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(!mRequestingLocationUpdates)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            mRequestingLocationUpdates = true;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null)
        {
            TransitTimesApplication.getBus().post(new LocationUpdateMessage(location));
        }
    }


}
