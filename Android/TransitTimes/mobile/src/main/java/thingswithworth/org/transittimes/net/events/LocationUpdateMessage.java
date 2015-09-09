package thingswithworth.org.transittimes.net.events;

import android.location.Location;

/**
 * Created by Alex on 9/8/2015.
 */
public class LocationUpdateMessage
{
    private Location location;

    public LocationUpdateMessage(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
