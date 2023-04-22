package com.bid.app.adaper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bid.app.ui.fragment.IdDocuments.ArchivedIdDocumentsFragment;
import com.bid.app.ui.fragment.IdDocuments.CurrentIdDocumentsFragment;

public class ViewPagerIdDocumentsAdapter extends FragmentPagerAdapter {

    private String[] tabHeaders = {"Current", "Archived"};

    public ViewPagerIdDocumentsAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new CurrentIdDocumentsFragment();
        } else if (position == 1) {
            fragment = new ArchivedIdDocumentsFragment();
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
}



