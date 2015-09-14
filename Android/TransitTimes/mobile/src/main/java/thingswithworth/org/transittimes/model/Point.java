package thingswithworth.org.transittimes.model;

import org.parceler.Parcel;

/**
 * Created by Alex on 8/27/2015.
 */
@Parcel
public class Point
{
    String type;
    double[] coordinates;

    public Point() {
    }

    public Point(String type, double[] coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
