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
import com.squareup.otto.Bus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import thingswithworth.org.transittimes.R;
import thingswithworth.org.transittimes.TransitTimesApplication;
import thingswithworth.org.transittimes.model.Stop;
import thingswithworth.org.transittimes.net.events.OpenRouteRequest;
import thingswithworth.org.transittimes.net.events.OpenStopRequest;
import thingswithworth.org.transittimes.net.service.TransitTimesRESTServices;

/**
 * Created by Alex on 9/8/2015.
 */
public class StopAdapter extends RecyclerView.Adapter<StopAdapter.ViewHolder>
{
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.titleView)
        TextView mTitleView;

        @Bind(R.id.detailView)
        TextView mDetailView;

        @Bind(R.id.card_view)
        CardView mContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private Context mContext;
    private List<Stop> mStopList;

    private TransitTimesRESTServices mRestService;
    private Bus mBus;

    public StopAdapter(List<Stop> stopList, Context context)
    {
        mContext = context;
        mStopList = stopList;
        mRestService = TransitTimesRESTServices.getInstance();
        mBus = TransitTimesApplication.getBus();
    }

    @Override
    public StopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_stop, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StopAdapter.ViewHolder holder, int position)
    {
        Stop stop = mStopList.get(position);
        holder.mTitleView.setText(stop.getName());
        holder.mDetailView.setText(stop.getDescription());

        holder.mContainer.setOnClickListener((v)->
                {
                    Dialog dialog = new MaterialDialog.Builder(mContext)
                            .title("Loading...")
                            .content("Loading stop...")
                            .progress(true, 0)
                            .show();

                    Log.d("Retrofit", "Getting Stop");

                    mBus.post(new OpenStopRequest(stop, dialog));
                }
        );
    }

    @Override
    public int getItemCount() {
        return mStopList.size();
    }
}
