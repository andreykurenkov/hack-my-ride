package thingswithworth.org.transittimes.bluetooth.events;

import org.altbeacon.beacon.Beacon;

import thingswithworth.org.transittimes.model.Stop;
import thingswithworth.org.transittimes.model.StopTime;

/**
 * Created by andrey on 9/3/15.
 */
public class AtStopBeaconNotification {
    private Beacon beacon;
    private Stop stop;
    private StopTime nextTime;

    public AtStopBeaconNotification(Beacon beacon, Stop stop, StopTime nextTime) {

        this.beacon = beacon;
        this.stop = stop;
        this.nextTime = nextTime;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    public Stop getStop() {
        return stop;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
    }

    public StopTime getNextTime() {
        return nextTime;
    }

    public void setNextTime(StopTime nextTime) {
        this.nextTime = nextTime;
    }

}
