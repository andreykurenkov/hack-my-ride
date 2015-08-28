package thingswithworth.org.transittimes.model;

/**
 * Created by Alex on 8/27/2015.
 */
public class Point
{
    private String type;
    private int[] coordinates;

    public Point() {
    }

    public Point(String type, int[] coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }
}
