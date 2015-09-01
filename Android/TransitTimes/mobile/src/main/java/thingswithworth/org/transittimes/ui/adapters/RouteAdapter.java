package thingswithworth.org.transittimes.ui.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.android.iconify.Iconify;
import com.squareup.otto.Bus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import thingswithworth.org.transittimes.R;
import thingswithworth.org.transittimes.TransitTimesApplication;
import thingswithworth.org.transittimes.model.Route;
import thingswithworth.org.transittimes.net.events.OpenRouteRequest;
import thingswithworth.org.transittimes.net.service.TransitTimesRESTServices;

/**
 * Created by Alex on 8/27/2015.
 */
public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder>
{

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.titleView)
        TextView titleView;
        @Bind(R.id.detailView)
        TextView detailView;
        @Bind(R.id.iconView)
        TextView iconView;
        @Bind(R.id.card_view)
        CardView container;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private List<Route> mRouteList;
    private Context mContext;
    private Bus mBus;
    private TransitTimesRESTServices mService;

    public RouteAdapter(List<Route> items, Context context)
    {
        mRouteList = items;
        mContext = context;
        mBus = TransitTimesApplication.getBus();
        mService = TransitTimesRESTServices.getInstance();
    }

    @Override
    public RouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_route, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RouteAdapter.ViewHolder holder, int position)
    {
        Route route = mRouteList.get(position);
        holder.titleView.setText(route.getLong_name());
        holder.detailView.setText(route.getAgency().getName());
        if(route.getRoute_type()== Route.RouteType.BUS)
        {
            holder.iconView.setText("{fa-bus}");
        }
        else if(route.getRoute_type()==Route.RouteType.LIGHTRAIL || route.getRoute_type()==Route.RouteType.SUBWAY)
        {
            holder.iconView.setText("{fa-train}");
        }
        Iconify.addIcons(holder.iconView);

        holder.container.setOnClickListener((v)->
            {
                Dialog dialog = new MaterialDialog.Builder(mContext)
                                .title("Loading...")
                                .content("Loading stops...")
                                .progress(true, 0)
                                .show();

                Log.d("Retrofit","Getting Route");

                Observable.zip(
                    mService.routeService.getRoute(route.getId()),
                    mService.routeService.getStops(route.getId()),
                    (route_detail, stops) -> {
                        route_detail.setStops(stops);
                        return route_detail;
                    }
                ).subscribe(
                    (route_detail)->
                        mBus.post(new OpenRouteRequest(route_detail, dialog)),
                    (error)->
                            Log.e("Retrofit", error.getMessage())
                );
            }
        );

    }

    @Override
    public int getItemCount() {
        return mRouteList.size();
    }
}
