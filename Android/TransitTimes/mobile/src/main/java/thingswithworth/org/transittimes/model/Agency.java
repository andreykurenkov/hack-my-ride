package thingswithworth.org.transittimes.model;

/**
 * Created by Alex on 8/27/2015.
 */
public class Agency
{
    private int id;
    private String agency_id;
    private String name;
    private String url;
    private String timezone;
    private String phone;


    public Agency() {
    }

    public Agency(int id, String agency_id, String agency_name, String url, String agency_timezone, String agency_phone) {
        this.id = id;
        this.agency_id = agency_id;
        this.name = agency_name;
        this.url = url;
        this.timezone = agency_timezone;
        this.phone = agency_phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(String agency_id) {
        this.agency_id = agency_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}


