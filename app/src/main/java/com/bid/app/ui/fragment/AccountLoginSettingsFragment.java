package com.bid.app.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bid.app.R;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.retrofit.APIClient;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;

public class AccountLoginSettingsFragment extends BaseFragment {

    private static final String TAG = AccountLoginSettingsFragment.class.getSimpleName();

    private APIInterface apiInterface;



    private ImageView ivUserFingerprint;


    private boolean isUserFingerprint = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account_login_settings, container, false);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_login_settings), R.drawable.ic_back_arrow, false);

        initController(view);

        return view;
    }

    private void initController(View view) {

        ivUserFingerprint = view.findViewById(R.id.iv_user_fingerprint);

        ivUserFingerprint.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.iv_user_fingerprint:
                enableUserFingerprint();
                break;


        }
    }
    private void enableUserFingerprint() {

        if (isUserFingerprint) {
            ivUserFingerprint.setImageResource(R.drawable.ic_switch_on);
            isUserFingerprint = false;
        } else {
            ivUserFingerprint.setImageResource(R.drawable.ic_switch_off);
            isUserFingerprint = true;
        }
    }

}
