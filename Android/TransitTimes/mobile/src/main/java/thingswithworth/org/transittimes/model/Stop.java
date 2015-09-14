package thingswithworth.org.transittimes.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Alex on 8/27/2015.
 */

@Parcel
public class Stop
{
    int id;
    String stop_id;
    String code;
    @SerializedName("desc")
    String description;
    Point point;
    String name;
    List<StopTime> stopTimes;


    public Stop() {
    }

    public Stop(int id, String stop_id, String code, String description, Point point) {
        this.id = id;
        this.stop_id = stop_id;
        this.code = code;
        this.description = description;
        this.point = point;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStop_id() {
        return stop_id;
    }

    public void setStop_id(String stop_id) {
        this.stop_id = stop_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StopTime> getStopTimes() {
        return stopTimes;
    }

    public void setStopTimes(List<StopTime> stop_times) {
        this.stopTimes = stop_times;
    }
}
