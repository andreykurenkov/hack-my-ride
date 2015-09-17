package thingswithworth.org.transittimes.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.astuetz.PagerSlidingTabStrip;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import thingswithworth.org.transittimes.R;
import thingswithworth.org.transittimes.TransitTimesApplication;
import thingswithworth.org.transittimes.model.Stop;
import thingswithworth.org.transittimes.net.events.FilterMessage;
import thingswithworth.org.transittimes.net.events.OpenPreferencesRequest;

/**
 * Created by Alex on 8/23/2015.
 */
public class TransitSystemFragment extends Fragment implements SearchView.OnQueryTextListener, ViewPager.OnPageChangeListener
{
    @Bind(R.id.tabs)
    PagerSlidingTabStrip mTabStrip;

    @Bind(R.id.pager)
    ViewPager mViewPager;

    @Bind(R.id.toolbar)
    Toolbar toolbar;


    private TransitSystemPagerAdapter mPagerAdapter;
    private int currentAgency = 0;
    private  Drawer.Result drawer;

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

        mViewPager.addOnPageChangeListener(this);

        toolbar.setTitle("Transit Times");
        toolbar.inflateMenu(R.menu.menu_main);
        Menu menu = toolbar.getMenu();
        menu.findItem(R.id.search).setIcon(
                new IconDrawable(getActivity(), Iconify.IconValue.fa_search)
                        .color(0xFFFFFF)
                        .actionBarSize());

        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.search).getActionView();

        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }


        final AccountHeader.Result headerResult = new AccountHeader()
                .withActivity(getActivity())
                .withHeaderBackground(R.drawable.header)
                .build();

        drawer = new Drawer().withActivity(getActivity())
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("VTA").withIdentifier(2),
                        new PrimaryDrawerItem().withName("MARTA").withIdentifier(1),
                        new PrimaryDrawerItem().withName("BART").withIdentifier(3),
                        new PrimaryDrawerItem().withName("Caltrain").withIdentifier(4),
                        new PrimaryDrawerItem().withName("RTD").withIdentifier(5),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Settings").withIdentifier(999).withIcon(new IconDrawable(getActivity(), Iconify.IconValue.fa_gear))

                )
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View view) {

                    }

                    @Override
                    public void onDrawerClosed(View view) {

                    }
                })
                .withOnDrawerItemClickListener(
                        (adapterView, drawerView, i, l, iDrawerItem) ->
                        {
                            if (iDrawerItem.getIdentifier() == 999) {
                                TransitTimesApplication.getBus().post(new OpenPreferencesRequest());
                            } else {
                                updateAgency(iDrawerItem.getIdentifier());
                            }
                        }
                )
                .build();
    }

    public void updateBeaconStop(Stop s)
    {
        getActivity().runOnUiThread(()->
            mPagerAdapter.updateStop(s)
        );
    }

    public void updateAgency(int i)
    {
        currentAgency = i;
        mPagerAdapter.updateAgency(i);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        InputMethodManager inputManager =
                (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText!=null) {
            TransitTimesApplication.getBus().post(new FilterMessage(newText));
            return true;
        }
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position)
    {
        if(position == 0)
        {
            Menu menu = toolbar.getMenu();
            menu.findItem(R.id.search).setVisible(true);
        }
        else
        {
            Menu menu = toolbar.getMenu();
            menu.findItem(R.id.search).setVisible(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
            fragments[0] = new RouteListFragment();
            fragments[1] = new StopListFragment();
            fragments[2] = new BeaconStopListFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        public void updateStop(Stop s)
        {
            List<Stop> stops = new ArrayList<Stop>();
            stops.add(s);
            ((StopListFragment)fragments[2]).updateStops(stops);
        }

        public void updateAgency(int i) {
            ((RouteListFragment)fragments[0]).updateAgency(i);
        }
    }
}
