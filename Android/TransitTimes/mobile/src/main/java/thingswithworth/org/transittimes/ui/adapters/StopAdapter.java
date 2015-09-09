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
import thingswithworth.org.transittimes.model.Stop;

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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private Context mContext;
    private List<Stop> mStopList;

    public StopAdapter(List<Stop> stopList, Context context)
    {
        mContext = context;
        mStopList = stopList;
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

    }

    @Override
    public int getItemCount() {
        return mStopList.size();
    }
}
