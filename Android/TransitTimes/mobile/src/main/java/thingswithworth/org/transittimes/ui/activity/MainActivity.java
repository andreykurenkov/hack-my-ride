package thingswithworth.org.transittimes.ui.activity;

import android.location.Location;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.squareup.otto.Subscribe;

import org.altbeacon.beacon.BeaconManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import thingswithworth.org.transittimes.R;
import thingswithworth.org.transittimes.TransitTimesApplication;
import thingswithworth.org.transittimes.bluetooth.BluetoothUtil;
import thingswithworth.org.transittimes.bluetooth.events.NewBeaconSeen;
import thingswithworth.org.transittimes.net.events.LocationUpdateMessage;
import thingswithworth.org.transittimes.net.events.OpenRouteRequest;
import thingswithworth.org.transittimes.net.events.OpenStopRequest;
import thingswithworth.org.transittimes.ui.fragment.RouteDetailFragment;
import thingswithworth.org.transittimes.ui.fragment.StopDetailFragment;
import thingswithworth.org.transittimes.ui.fragment.TransitSystemFragment;
import thingswithworth.org.transittimes.ui.menu.AppDrawer;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, LocationListener {
    private static String TAG = "MainActivity";
    private TransitSystemFragment systemFragment;
    private RouteDetailFragment routeDetailFragment;
    private StopDetailFragment stopDetailFragment;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates = false;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        TransitTimesApplication.getBus().register(this);

        mToolbar.setTitle("TransitTimes");
        setSupportActionBar(mToolbar);

        final Drawer.Result drawer = new AppDrawer().withActivity(this)
                .withToolbar(mToolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("VTA").withIdentifier(1),
                        new PrimaryDrawerItem().withName("MARTA").withIdentifier(2)
                )
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View view) {

                    }

                    @Override
                    public void onDrawerClosed(View view) {

                    }
                })
                .withOnDrawerItemClickListener(
                        (adapterView, view, i, l, iDrawerItem) ->
                        {

                        }
                )
                .build();

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

            stopDetailFragment.updateStop(openStopRequest.getStop());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, stopDetailFragment)
                    .addToBackStack("")
                    .commit();
        });
    }

    @Subscribe
    public void onNewBeacon(NewBeaconSeen seenBeacon)
    {
        Log.d(TAG, "Bus event for ID " + seenBeacon.getStopId());
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
