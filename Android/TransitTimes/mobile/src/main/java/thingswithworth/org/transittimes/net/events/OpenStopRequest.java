package thingswithworth.org.transittimes.net.events;

import android.app.Dialog;

import thingswithworth.org.transittimes.model.Stop;

/**
 * Created by Alex on 8/28/2015.
 */
public class OpenStopRequest
{
    private Stop stop;
    private Dialog dialog;

    public OpenStopRequest(Stop stop, Dialog dialog)
    {
        this.stop = stop;
        this.dialog = dialog;
    }


    public Stop getStop() {
        return stop;
    }

    public Dialog getDialog() {
        return dialog;
    }
}
