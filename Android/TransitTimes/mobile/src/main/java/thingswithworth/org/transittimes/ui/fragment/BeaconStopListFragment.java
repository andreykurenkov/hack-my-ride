package thingswithworth.org.transittimes.ui.fragment;

import android.util.Log;

import thingswithworth.org.transittimes.net.events.LocationUpdateMessage;

/**
 * Created by Alex on 9/10/2015.
 */
public class BeaconStopListFragment extends StopListFragment
{
    @Override
    public void onLocation(LocationUpdateMessage locMessage) {
        //swallow message
        Log.d("BeaconStopListFragment", "swallowing on location");
    }
}
