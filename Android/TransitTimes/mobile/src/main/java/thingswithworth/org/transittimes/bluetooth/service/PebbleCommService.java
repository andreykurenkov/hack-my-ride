package thingswithworth.org.transittimes.bluetooth.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;
import com.squareup.otto.Subscribe;

import java.util.UUID;

import thingswithworth.org.transittimes.TransitTimesApplication;
import thingswithworth.org.transittimes.bluetooth.events.AtStopNotification;
import thingswithworth.org.transittimes.model.StopTime;

/**
 * Created by andrey on 9/5/15.
 */
public class PebbleCommService extends Service {
    private String TAG = "PebbleCommService";
    private final static UUID PEBBLE_APP_UUID = UUID.fromString("68932137-3a91-4205-bf0d-4a5fcebd88dc");
    private PebbleDictionary data;
    @Override
    public void onDestroy() {
        super.onDestroy();
        PebbleKit.closeAppOnPebble(this,PEBBLE_APP_UUID);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service running");

        PebbleKit.registerPebbleConnectedReceiver(getApplicationContext(), new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "Pebble connected!");
            }

        });

        PebbleKit.registerPebbleDisconnectedReceiver(getApplicationContext(), new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "Pebble disconnected!");
            }

        });

        PebbleKit.registerReceivedAckHandler(getApplicationContext(), new PebbleKit.PebbleAckReceiver(PEBBLE_APP_UUID) {

            @Override
            public void receiveAck(Context context, int transactionId) {
                Log.i(TAG, "Received ack for transaction " + transactionId);
            }

        });

        PebbleKit.registerReceivedNackHandler(getApplicationContext(), new PebbleKit.PebbleNackReceiver(PEBBLE_APP_UUID) {

            @Override
            public void receiveNack(Context context, int transactionId) {
                Log.i(TAG, "Received nack for transaction " + transactionId+" , resending.");
                PebbleKit.sendDataToPebble(getApplicationContext(), PEBBLE_APP_UUID, data);
            }

        });
        TransitTimesApplication.getBus().register(this);
    }

    @Subscribe
    public void atStopNotification(AtStopNotification atStopNotification)
    {
        PebbleKit.startAppOnPebble(getApplicationContext(), PEBBLE_APP_UUID);
        Log.i(TAG, "Sending Pebble info for stop " + atStopNotification.getStop().getName());
        data = new PebbleDictionary();

        data.addString(0, atStopNotification.getStop().getName().replace("(0)", "(out)").replace("(1)", "(in)"));
        data.addInt8(1, (byte)Math.min(5,atStopNotification.getTimes().size()));
        int i=2;
        for(StopTime time:atStopNotification.getTimes()){
            data.addString((i++), time.getTripData().getHeadsign());
            String str = time.getArrival_time().toString(false,false);
            if(time.getRealtime()!=null)
                str+=", predicted: "+time.getRealtime().toString(false,false);
            data.addString((i++), str);
            if(i==12)//send max of 5 times
                break;
        }

        PebbleKit.sendDataToPebble(getApplicationContext(), PEBBLE_APP_UUID, data);
    }
}
