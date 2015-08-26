package thingswithworth.org.transittimes.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import butterknife.Bind;
import butterknife.ButterKnife;
import thingswithworth.org.transittimes.R;

/**
 * Created by Alex on 8/23/2015.
 */
public class TransitSystemFragment extends Fragment
{
    @Bind(R.id.tabs)
    PagerSlidingTabStrip mTabStrip;

    @Bind(R.id.pager)
    ViewPager mViewPager;


    private TransitSystemPagerAdapter mPagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPagerAdapter = new TransitSystemPagerAdapter(getChildFragmentManager());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_transitsystem_tab_host, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mPagerAdapter);
        mTabStrip.setViewPager(mViewPager);
        mViewPager.setCurrentItem(0);


        //menu inflation if necessary
    }

    private class TransitSystemPagerAdapter extends FragmentPagerAdapter
    {
        private final String[] TITLES = {"Browse", "Nearby", "Beacons"};
        private Fragment[] fragments;

        //fragment declarations

        public TransitSystemPagerAdapter(FragmentManager fm)
        {
            super(fm);
            fragments = new Fragment[3];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
            return new Fragment();
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }
}
