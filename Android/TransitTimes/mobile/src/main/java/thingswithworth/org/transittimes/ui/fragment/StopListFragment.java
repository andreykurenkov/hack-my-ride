package thingswithworth.org.transittimes.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import thingswithworth.org.transittimes.R;
import thingswithworth.org.transittimes.TransitTimesApplication;
import thingswithworth.org.transittimes.model.Route;
import thingswithworth.org.transittimes.model.Stop;
import thingswithworth.org.transittimes.net.events.LocationUpdateMessage;
import thingswithworth.org.transittimes.net.service.TransitTimesRESTServices;
import thingswithworth.org.transittimes.ui.adapters.RouteAdapter;
import thingswithworth.org.transittimes.ui.adapters.StopAdapter;

/**
 * Created by Alex on 9/3/2015.
 */
public class StopListFragment extends Fragment
{
    @Bind(R.id.genericListView)
    RecyclerView mRecyclerView;

    @Bind(R.id.loadingCircle)
    ProgressBar mLoadingCircle;

    protected List<Stop> mStopList;
    private TransitTimesRESTServices mTransitTimesService;
    private StopAdapter mStopAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState==null)
        {
            mStopList = new ArrayList<>();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_generic_list_fragment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mStopAdapter = new StopAdapter(mStopList, getActivity());
        mRecyclerView.setAdapter(mStopAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTransitTimesService = TransitTimesRESTServices.getInstance();

    }

    @Override
    public void onResume() {
        super.onResume();
        TransitTimesApplication.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        TransitTimesApplication.getBus().unregister(this);
    }

    @Subscribe
    public void onLocation(LocationUpdateMessage locMessage)
    {
        mTransitTimesService.stopService.getNearestStops(locMessage.getLocation().getLatitude(), locMessage.getLocation().getLongitude(), 0.5).subscribe((stops)->
        {
            mStopList.clear();
            mStopList.addAll(stops);
            getActivity().runOnUiThread(()->
                mStopAdapter.notifyDataSetChanged());
        });
    }

    public void updateStops(List<Stop> stops)
    {
        mStopList.clear();
        mStopList.addAll(stops);
    }
}
