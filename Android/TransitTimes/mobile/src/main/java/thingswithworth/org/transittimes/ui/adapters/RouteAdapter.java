package thingswithworth.org.transittimes.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import thingswithworth.org.transittimes.R;
import thingswithworth.org.transittimes.model.Route;

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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private List<Route> mRouteList;
    private Context mContext;

    public RouteAdapter(List<Route> items, Context context)
    {
        mRouteList = items;
        mContext = context;
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
        //Iconify.addIcons(holder.iconView);

    }

    @Override
    public int getItemCount() {
        return mRouteList.size();
    }
}
