package com.bid.app.ui.fragment.discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bid.app.R;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.home.HomeBIDFragment;

public class DiscoverDetailFragment extends BaseFragment {

    private static final String TAG = HomeBIDFragment.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discover_details, container, false);

        ((DashboardActivity) requireActivity()).setTitleAndImage("Discover", R.drawable.ic_back_arrow, false);
        initController(view);

        return view;
    }


    private void initController(View view) {


    }

}
