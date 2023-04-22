package com.bid.app.ui.fragment.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bid.app.R;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.retrofit.APIClient;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;

public class HelpFragment extends BaseFragment {

    private static final String TAG = HelpFragment.class.getSimpleName();

    private APIInterface apiInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_help, container, false);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_help), R.drawable.ic_menu, false);

        initController(view);

        return view;
    }

    private void initController(View view) {


    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {


        }
    }
}