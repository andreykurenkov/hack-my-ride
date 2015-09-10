package thingswithworth.org.transittimes.net.events;

/**
 * Created by Alex on 9/9/2015.
 */
public class FilterMessage
{
    private String filter;

    public FilterMessage(String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return filter;
    }
}
