package com.bid.app.ui.fragment.address;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.bid.app.R;
import com.bid.app.adaper.AddressPagerAdapter;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.google.android.material.tabs.TabLayout;

public class AddressFragment extends BaseFragment {

    private static final String TAG = AddressFragment.class.getSimpleName();

    private TabLayout tabLayout;

    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_address, container, false);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_address), R.drawable.ic_back_arrow, false);

        initController(view);

        return view;
    }

    private void initController(View view) {

        tabLayout = view.findViewById(R.id.tab_layout);

        viewPager = view.findViewById(R.id.view_pager);

        final AddressPagerAdapter addressPagerAdapter = new AddressPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(addressPagerAdapter);
        addressPagerAdapter.notifyDataSetChanged();

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
    }
}
