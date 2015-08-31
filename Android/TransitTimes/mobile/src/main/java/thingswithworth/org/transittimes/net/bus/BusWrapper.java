package thingswithworth.org.transittimes.net.bus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by Alex on 8/28/2015.
 */
public class BusWrapper
{
    private static Bus instance;

    public static Bus getBus()
    {
        if(instance == null)
        {
            instance = new Bus(ThreadEnforcer.ANY);
        }
        return instance;
    }
}
