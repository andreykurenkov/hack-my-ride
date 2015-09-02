package thingswithworth.org.transittimes.model;

/**
 * Created by Alex on 8/30/2015.
 */
public class Polyline
{
    private double[][][] coordinates;

    public Polyline() {
    }

    public Polyline(double[][][] coordinates) {
        this.coordinates = coordinates;
    }

    public double[][][] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[][][] coordinates) {
        this.coordinates = coordinates;
    }
}
