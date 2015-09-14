package thingswithworth.org.transittimes.bluetooth;

import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;

import java.net.URL;

/**
 * Created by andrey on 8/31/15.
 */
public class BluetoothUtil {
    private static final String TAG = "BluetoothUtil";
    public static String getBeaconURL(Beacon beacon){
        String url =UrlBeaconUrlCompressor.uncompress(beacon.getId1().toByteArray());
        return url.substring(0,url.length()-5);
    }

    public static int getStopIdFromURL(String url){
        String[] parts = url.split("/");
        return Integer.parseInt(parts[parts.length-1]);
    }

    public static int getStopIdFromBeacon(Beacon beacon){
        Log.i(TAG, "Beacon detected with id1: "+beacon.getId1()+" id2:"+beacon.getId2()+" id3: "+beacon.getId3());
        String id = beacon.getId1().toString();
        int stop_id = Integer.parseInt(id.split("-")[2]);
        return stop_id;
    }
}
