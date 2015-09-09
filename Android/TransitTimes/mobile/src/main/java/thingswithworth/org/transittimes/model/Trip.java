package thingswithworth.org.transittimes.model;

/**
 * Created by Alex on 8/27/2015.
 */
public class Trip {
    private int route;
   // private Service service;
    private String headsign;
    private String direction;
    //private Block block;
   // private Shape shape;
    //private Polyline geometry;
    private String wheelchar_accessible;
    private String bikes_allowed;

    public Trip(int route, String headsign, String direction, String wheelchar_accessible, String bikes_allowed) {
        this.route = route;
        this.headsign = headsign;
        this.direction = direction;
        this.wheelchar_accessible = wheelchar_accessible;
        this.bikes_allowed = bikes_allowed;
    }

    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        this.route = route;
    }

    public String getHeadsign() {
        return headsign;
    }

    public void setHeadsign(String headsign) {
        this.headsign = headsign;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getWheelchar_accessible() {
        return wheelchar_accessible;
    }

    public void setWheelchar_accessible(String wheelchar_accessible) {
        this.wheelchar_accessible = wheelchar_accessible;
    }

    public String getBikes_allowed() {
        return bikes_allowed;
    }

    public void setBikes_allowed(String bikes_allowed) {
        this.bikes_allowed = bikes_allowed;
    }
}
