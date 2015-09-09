package thingswithworth.org.transittimes.bluetooth.events;

import org.altbeacon.beacon.Beacon;

import java.util.List;

import thingswithworth.org.transittimes.model.Stop;
import thingswithworth.org.transittimes.model.StopTime;
import thingswithworth.org.transittimes.model.Trip;

/**
 * Created by andrey on 9/3/15.
 */
public class AtStopNotification {
    private Beacon beacon;
    private Stop stop;
    private List<StopTime> nextTimes;

    public AtStopNotification(Beacon beacon, Stop stop, List<StopTime> nextTimes) {
        this.beacon = beacon;
        this.stop = stop;
        this.nextTimes = nextTimes;
    }

    public AtStopNotification(Stop stop, List<StopTime> nextTimes) {
        this(null,stop,nextTimes);
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

    public List<StopTime> getTimes() {
        return nextTimes;
    }

    public void setNextTime(List<StopTime> nextTimes) {
        this.nextTimes = nextTimes;
    }

}
