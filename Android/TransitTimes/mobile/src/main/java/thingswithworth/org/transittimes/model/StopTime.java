package thingswithworth.org.transittimes.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Alex on 8/27/2015.
 */
@Parcel
public class StopTime {
    int id;
    int trip;
    int stop;
    SecondsTime arrival_time;
    SecondsTime departure_time;
    int stop_sequence;
    String stop_headsign;
    String pickup_type;
    String drop_of_type;
    float shape_dist_traveled;

    Trip tripData;
    Stop stopData;

    SecondsTime realtime;

    public StopTime(){}

    public StopTime(int id, int trip, int stop, SecondsTime arrival_time, SecondsTime departure_time,
                    int stop_sequence, String stop_headsign, String pickup_type, String drop_of_type,
                    float shape_dist_traveled) {
        this.id = id;
        this.trip = trip;
        this.stop = stop;
        this.arrival_time = arrival_time;
        this.departure_time = departure_time;
        this.stop_sequence = stop_sequence;
        this.stop_headsign = stop_headsign;
        this.pickup_type = pickup_type;
        this.drop_of_type = drop_of_type;
        this.shape_dist_traveled = shape_dist_traveled;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getTrip() {
        return trip;
    }

    public void setTrip(int trip) {
        this.trip = trip;
    }

    public int getStop() {
        return stop;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

    public SecondsTime getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(SecondsTime arrival_time) {
        this.arrival_time = arrival_time;
    }

    public SecondsTime getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(SecondsTime departure_time) {
        this.departure_time = departure_time;
    }

    public int getStop_sequence() {
        return stop_sequence;
    }

    public void setStop_sequence(int stop_sequence) {
        this.stop_sequence = stop_sequence;
    }

    public String getStop_headsign() {
        return stop_headsign;
    }

    public void setStop_headsign(String stop_headsign) {
        this.stop_headsign = stop_headsign;
    }

    public String getPickup_type() {
        return pickup_type;
    }

    public void setPickup_type(String pickup_type) {
        this.pickup_type = pickup_type;
    }

    public String getDrop_of_type() {
        return drop_of_type;
    }

    public void setDrop_of_type(String drop_of_type) {
        this.drop_of_type = drop_of_type;
    }

    public float getShape_dist_traveled() {
        return shape_dist_traveled;
    }

    public void setShape_dist_traveled(float shape_dist_traveled) {
        this.shape_dist_traveled = shape_dist_traveled;
    }

    public Trip getTripData() {
        return tripData;
    }

    public void setTripData(Trip tripData) {
        this.tripData = tripData;
    }

    public Stop getStopData() {
        return stopData;
    }

    public void setStopData(Stop stopData) {
        this.stopData = stopData;
    }

    public SecondsTime getRealtime() {
        return realtime;
    }

    public void setRealtime(SecondsTime realtime) {
        this.realtime = realtime;
    }
}
