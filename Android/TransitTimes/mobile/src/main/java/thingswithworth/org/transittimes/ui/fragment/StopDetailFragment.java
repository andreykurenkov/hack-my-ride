package thingswithworth.org.transittimes.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import thingswithworth.org.transittimes.R;
import thingswithworth.org.transittimes.model.Stop;

/**
 * Created by Alex on 9/2/2015.
 */
public class StopDetailFragment extends Fragment
{
    private Stop mCurrentStop;

    @Bind(R.id.titleView)
    TextView mTitleView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stop_detail, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    public void updateStopAndRefresh(Stop s)
    {
        mCurrentStop = s;
        refreshView();
    }

    private void refreshView()
    {
        if(mCurrentStop!=null)
            mTitleView.setText(mCurrentStop.getName());
    }
}
