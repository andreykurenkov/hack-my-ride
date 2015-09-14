package thingswithworth.org.transittimes.bluetooth.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.parceler.Parcels;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

import rx.Observable;
import thingswithworth.org.transittimes.R;
import thingswithworth.org.transittimes.TransitTimesApplication;
import thingswithworth.org.transittimes.bluetooth.BluetoothUtil;
import thingswithworth.org.transittimes.bluetooth.events.AtStopNotification;
import thingswithworth.org.transittimes.bluetooth.events.NewBeaconSeen;
import thingswithworth.org.transittimes.model.SharedPreferencesModel;
import thingswithworth.org.transittimes.model.Stop;
import thingswithworth.org.transittimes.model.StopTime;
import thingswithworth.org.transittimes.model.Trip;
import thingswithworth.org.transittimes.net.service.TransitTimesRESTServices;
import thingswithworth.org.transittimes.ui.activity.MainActivity;

/**
 * Created by andrey on 8/28/15.
 */
public class BeaconMonitoringService extends Service implements BeaconConsumer {
    private String TAG = "BeaconMonitoringService";
    private int mLastSeenId;
    public static Stop lastSeenStop;

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
                        respondToNewStop(beacon,id);
                    }
                }
            }

        });

        try {
            TransitTimesApplication.getBeaconManager().startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }

    private void respondToNewStop(Beacon beacon, int id) {
        Stop stop = TransitTimesRESTServices.getInstance().getDetailedStopTimes(id,3,true);
        lastSeenStop = stop;
        TransitTimesApplication.getBus().post(new AtStopNotification(beacon,stop,stop.getStopTimes()));
        postNotification(stop, stop.getStopTimes(), 3);
    }

    private void postNotification(Stop stop, List<StopTime> nextTimes, int num){
        NotificationCompat.Builder mBuilder =
        new NotificationCompat.Builder(this)
                .setContentTitle("At "+stop.getName())
                .setContentText("Expand for info about "+nextTimes.size()+" upcoming stops")
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        // Sets a title for the Inbox in expanded layout
         // Moves events into the expanded layout
        inboxStyle.setSummaryText("Click to go to app view.");
        for(int i=0;i<num;i++) {
            if(nextTimes.size()<num)
                break;
            StopTime stopTime = nextTimes.get(i);
            if(stopTime.getRealtime()!=null) {
                String contentText = "At " + stopTime.getArrival_time().toString(false, false) +
                        " (predicted " + stopTime.getRealtime().toString(false, false) +
                        ") for " + stopTime.getTripData().getHeadsign() + "\n";
                inboxStyle.addLine(contentText);
            }else{
                String contentText = "At " + stopTime.getArrival_time().toString(false, false) +
                        " for " + stopTime.getTripData().getHeadsign() + "\n";
                inboxStyle.addLine(contentText);
            }
        }
        // Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);
        stop.setStopTimes(nextTimes);
        Parcelable stopData = Parcels.wrap(stop);
        resultIntent.putExtra("stop_data", stopData);
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
        // mId allows you to update the notification later on.
        NotificationManagerCompat.from(this).notify(0, mBuilder.build());


    }


}
