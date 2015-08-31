package thingswithworth.org.transittimes.net.bus.events;

import android.app.Dialog;

import java.util.List;

import thingswithworth.org.transittimes.model.Route;
import thingswithworth.org.transittimes.model.Stop;

/**
 * Created by Alex on 8/28/2015.
 */
public class OpenRouteRequest
{
    private Route route;
    private Dialog dialog;

    public OpenRouteRequest(Route route, Dialog dialog)
    {
        this.route = route;
        this.dialog = dialog;
    }


    public Route getRoute() {
        return route;
    }

    public Dialog getDialog() {
        return dialog;
    }
}
