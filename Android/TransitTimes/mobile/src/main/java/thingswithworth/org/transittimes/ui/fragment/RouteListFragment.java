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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import thingswithworth.org.transittimes.R;
import thingswithworth.org.transittimes.model.Route;
import thingswithworth.org.transittimes.net.service.TransitTimesRESTServices;
import thingswithworth.org.transittimes.ui.adapters.RouteAdapter;

/**
 * Created by Alex on 8/27/2015.
 */
public class RouteListFragment extends Fragment
{
    @Bind(R.id.genericListView)
    RecyclerView mRecyclerView;

    @Bind(R.id.loadingCircle)
    ProgressBar mLoadingCircle;

    private List<Route> mRouteList;
    private TransitTimesRESTServices mTransitTimesService;
    private RouteAdapter mRouteAdapter;





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState==null)
        {
            mRouteList = new ArrayList<>();
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
        mRouteAdapter = new RouteAdapter(mRouteList, getActivity());
        mRecyclerView.setAdapter(mRouteAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mTransitTimesService = TransitTimesRESTServices.getInstance();
        mTransitTimesService.routeService.getRoutes(1).subscribe((routes)->
        {
             mRouteList.clear();
            for(Route route : routes)
            {
                mRouteList.add(route);
            }
                getActivity().runOnUiThread(()->
                mRouteAdapter.notifyDataSetChanged()
            );
        }
        );
    }
}
