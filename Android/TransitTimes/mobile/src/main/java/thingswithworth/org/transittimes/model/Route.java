package thingswithworth.org.transittimes.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alex on 8/27/2015.
 */
public class Route
{
    public enum RouteType
    {
        LIGHTRAIL,
        SUBWAY,
        RAIL,
        BUS,
        FERRY,
        CABLECAR,
        GONDOLA,
        FUNICULAR
    }

    private int id;
    private String route_id;
    private Agency agency;
    private String short_name;
    private String long_name;
    private String route_desc;
    @SerializedName("rtype")
    private RouteType route_type;

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
}
