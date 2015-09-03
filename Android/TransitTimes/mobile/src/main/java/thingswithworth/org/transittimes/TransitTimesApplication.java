package thingswithworth.org.transittimes;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import thingswithworth.org.transittimes.bluetooth.service.BeaconMonitoringService;
import thingswithworth.org.transittimes.model.SharedPreferencesModel;
import thingswithworth.org.transittimes.net.service.TransitTimesRESTServices;
import thingswithworth.org.transittimes.ui.activity.MainActivity;

/**
 * Created by andrey on 8/22/15.
 */
public class TransitTimesApplication  extends Application implements BootstrapNotifier{
    private static final String TAG = "TransitTimesApplication";
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private boolean haveDetectedBeaconsSinceBoot = false;
    private MainActivity monitoringActivity = null;
    private static BeaconManager beaconManager;
    private static Bus instance;

    public static Bus getBus()
    {
        if(instance == null)
        {
            instance = new Bus(ThreadEnforcer.ANY);
        }
        return instance;
    }


    public static BeaconManager getBeaconManager(){
        Log.d(TAG, "Called get for beacon "+beaconManager);
        return beaconManager;
    }

    public void onCreate() {
        super.onCreate();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        // Detect the Eddystone URL frame:
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20"));

        Log.d(TAG, "setting up background monitoring for beacons and power saving");
        // wake up the app when a beacon is seen
        Region region = new Region("backgroundRegion",null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);

        // simply constructing this class and holding a reference to it in your custom Application
        // class will automatically cause the BeaconLibrary to save battery whenever the application
        // is not visible.  This reduces bluetooth power usage by about 60%
        backgroundPowerSaver = new BackgroundPowerSaver(this);
        beaconManager.setForegroundBetweenScanPeriod(5);

        // If you wish to test beacon detection in the Android Emulator, you can use code like this:
        // BeaconManager.setBeaconSimulator(new TimedBeaconSimulator() );
        // ((TimedBeaconSimulator) BeaconManager.getBeaconSimulator()).createTimedSimulatedBeacons();
        startService(new Intent(this, BeaconMonitoringService.class));

        TransitTimesRESTServices.init(this);

        SharedPreferencesModel.init(PreferenceManager.getDefaultSharedPreferences(this));

    }

    @Override
    public void didEnterRegion(Region region) {
       Log.d(TAG, "Entered region.");
        startService(new Intent(getApplicationContext(), BeaconMonitoringService.class));

    }

    @Override
    public void didExitRegion(Region region) {
        Log.d(TAG, "Exited region.");
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }


}
