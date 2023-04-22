package com.bid.app.adaper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bid.app.ui.fragment.address.ArchivedAddressFragment;
import com.bid.app.ui.fragment.address.CurrentAddressFragment;

public class AddressPagerAdapter extends FragmentPagerAdapter {

    public AddressPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new CurrentAddressFragment();
        } else if (position == 1) {
            fragment = new ArchivedAddressFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "Current";
        } else if (position == 1) {
            title = "Archived";
        }
        return title;
    }
}
