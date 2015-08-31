package thingswithworth.org.transittimes.model;

import android.content.SharedPreferences;

/**
 * Created by andrey on 8/31/15.
 */
public class SharedPreferencesModel {
    public static final String LAST_SEEN_BEACON_KEY = "LAST_SEEN_BEACON";
    public static int lastSeenStopBeaconId;
    private static SharedPreferences preferences;

    public static void init(SharedPreferences preferences){
        SharedPreferencesModel.preferences = preferences;
        lastSeenStopBeaconId = preferences.getInt(LAST_SEEN_BEACON_KEY,-1);
    }

    public static void updateLastSeenBeacon(int lastId){
        preferences.edit().putInt(LAST_SEEN_BEACON_KEY,lastSeenStopBeaconId);
        lastSeenStopBeaconId = lastId;
    }
}
