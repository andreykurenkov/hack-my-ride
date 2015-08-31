package thingswithworth.org.transittimes.bluetooth.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;

import java.util.Collection;

import thingswithworth.org.transittimes.R;
import thingswithworth.org.transittimes.TransitTimesApplication;
import thingswithworth.org.transittimes.bluetooth.BluetoothUtil;
import thingswithworth.org.transittimes.bluetooth.events.NewBeaconSeen;
import thingswithworth.org.transittimes.model.SharedPreferencesModel;
import thingswithworth.org.transittimes.ui.activity.MainActivity;

/**
 * Created by andrey on 8/28/15.
 */
public class BeaconMonitoringService extends Service implements BeaconConsumer {
    private int mLastSeenId;
    @Override
    public void onDestroy() {
        super.onDestroy();
        TransitTimesApplication.getBeaconManager().unbind(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLastSeenId = SharedPreferencesModel.lastSeenStopBeaconId;
        TransitTimesApplication.getBeaconManager().bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        TransitTimesApplication.getBeaconManager().setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for(Beacon beacon : beacons){
                    //EditText editText = (EditText)RangingActivity.this.findViewById(R.id.rangingText);
                    TransitTimesApplication.getBus().post(new NewBeaconSeen(beacon));
                    int id = BluetoothUtil.getStopIdFromBeacon(beacon);
                    if(id!=mLastSeenId) {
                        mLastSeenId = id;
                        SharedPreferencesModel.updateLastSeenBeacon(id);
                        sendNotification(id);
                    }
                }
            }

        });

        try {
            TransitTimesApplication.getBeaconManager().startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }

    private void sendNotification(int id) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Beacon Seen for stop")
                        .setContentText("Predicted time of arrival ")
                        .setSmallIcon(R.mipmap.ic_launcher);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());
    }


}
