package thingswithworth.org.transittimes.net.events;

import android.app.Dialog;

import thingswithworth.org.transittimes.model.Route;

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
