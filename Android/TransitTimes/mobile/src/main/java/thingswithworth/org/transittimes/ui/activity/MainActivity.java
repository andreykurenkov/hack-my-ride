package thingswithworth.org.transittimes.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
import thingswithworth.org.transittimes.net.events.OpenRouteRequest;
import thingswithworth.org.transittimes.ui.fragment.RouteDetailFragment;
import thingswithworth.org.transittimes.ui.fragment.TransitSystemFragment;
import thingswithworth.org.transittimes.ui.menu.AppDrawer;


public class MainActivity extends AppCompatActivity  {
    private static String TAG = "MainActivity";
    private BeaconManager mBeaconManager;
    private TransitSystemFragment systemFragment;
    private RouteDetailFragment routeDetailFragment;

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
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, systemFragment)
                    .commit();
        }
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
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Subscribe
    public void onOpenRoute(OpenRouteRequest openRouteRequest)
    {
        Log.d(TAG,"New OpenRouteRequest seen: "+openRouteRequest.getRoute().getRoute_id());

        runOnUiThread(()-> {
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
    public void onNewBeacon(NewBeaconSeen seenBeacon)
    {
        Log.d(TAG,"Bus event for ID "+ seenBeacon.getStopId());
    }
}
