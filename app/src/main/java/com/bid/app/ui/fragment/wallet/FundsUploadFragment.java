package com.bid.app.ui.fragment.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bid.app.R;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;

public class FundsUploadFragment extends BaseFragment { // ST
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_funds_upload, container, false);
        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_funds_upload), R.drawable.ic_back_arrow, false);
        return view;
    }
}
