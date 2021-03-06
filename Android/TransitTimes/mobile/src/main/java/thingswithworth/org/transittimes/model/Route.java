package thingswithworth.org.transittimes.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Alex on 8/27/2015.
 */
public class Route
{
    public enum RouteType
    {
        LIGHTRAIL(0),
        SUBWAY(1),
        RAIL(2),
        BUS(3),
        FERRY(4),
        CABLECAR(5),
        GONDOLA(6),
        FUNICULAR(7);

        private final int key;

        RouteType(int key)
        {
            this.key = key;
        }
        public int getKey()
        {
            return this.key;
        }

        public static RouteType fromKey(int key)
        {
            for(RouteType type: RouteType.values())
            {
                if(type.getKey() == key)
                {
                    return type;
                }
            }
            return null;
        }
    }

    private int id;
    private String route_id;
    private Agency agency;
    private String short_name;
    private String long_name;
    private String route_desc;
    @SerializedName("rtype")
    private RouteType route_type;
    @Expose(deserialize = false)
    private List<Stop> stops;
    private Polyline geometry;

    public Route() {
    }

    public Route(int id, String route_id, Agency agency, String short_name, String long_name, String route_desc, RouteType route_type) {

        this.id = id;
        this.route_id = route_id;
        this.agency = agency;
        this.short_name = short_name;
        this.long_name = long_name;
        this.route_desc = route_desc;
        this.route_type = route_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public String getRoute_desc() {
        return route_desc;
    }

    public void setRoute_desc(String route_desc) {
        this.route_desc = route_desc;
    }

    public RouteType getRoute_type() {
        return route_type;
    }

    public void setRoute_type(RouteType route_type) {
        this.route_type = route_type;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public Polyline getGeometry() {
        return geometry;
    }

    public void setGeometry(Polyline geometry) {
        this.geometry = geometry;
    }
}
