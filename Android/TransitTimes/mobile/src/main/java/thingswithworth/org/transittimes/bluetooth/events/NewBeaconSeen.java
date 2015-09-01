package thingswithworth.org.transittimes.bluetooth.events;

import org.altbeacon.beacon.Beacon;

import thingswithworth.org.transittimes.bluetooth.BluetoothUtil;

/**
 * Created by andrey on 8/31/15.
 */
public class NewBeaconSeen {

    private Beacon beacon;

    public NewBeaconSeen(Beacon beacon)
    {
        this.beacon = beacon;
    }


    public Beacon getBeacon() {
        return beacon;
    }

    public int getStopId(){
        return BluetoothUtil.getStopIdFromBeacon(beacon);
    }

}
