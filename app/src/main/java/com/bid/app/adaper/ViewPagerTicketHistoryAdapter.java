package com.bid.app.adaper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.bid.app.model.view.Trip;
import com.bid.app.ui.fragment.tripHistory.TripHistoryPartFragment;

import java.util.ArrayList;

public class ViewPagerTicketHistoryAdapter extends FragmentStatePagerAdapter {

    private String[] tabHeaders = {"ALL", "Upcoming", "Past"};
    ArrayList<Trip> allTrip, upcomingTrip, pastTrip;

    public ViewPagerTicketHistoryAdapter(FragmentManager fragmentManager, ArrayList<Trip> allTrip, ArrayList<Trip> upcomingTrip, ArrayList<Trip> pastTrip) {
        super(fragmentManager);
        this.allTrip = allTrip;
        this.upcomingTrip = upcomingTrip;
        this.pastTrip = pastTrip;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new TripHistoryPartFragment(allTrip);
        }
        else if(position == 1) {
            fragment = new TripHistoryPartFragment(upcomingTrip);
        } else if (position == 2) {
            fragment = new TripHistoryPartFragment(pastTrip);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tabHeaders.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabHeaders[position];
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}



