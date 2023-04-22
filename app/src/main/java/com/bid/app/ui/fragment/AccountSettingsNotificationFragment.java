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

public class AccountSettingsNotificationFragment extends BaseFragment {

    private static final String TAG = AccountSettingsNotificationFragment.class.getSimpleName();

    private APIInterface apiInterface;



    private ImageView ivAccountUpdates;
    private ImageView ivDetailsShared;
    private ImageView ivAccountUpdates1;

    private boolean isActionUpdates = false;
    private boolean isDetailsShared = false;
    private boolean isAccountUpdates = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account_settings_notification, container, false);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_notifications), R.drawable.ic_back_arrow, false);

        initController(view);

        return view;
    }



    private void initController(View view) {

        ivAccountUpdates = view.findViewById(R.id.iv_account_updates);
        ivDetailsShared = view.findViewById(R.id.iv_details_shared);
        ivAccountUpdates1 = view.findViewById(R.id.iv_account_updates1);


        ivAccountUpdates.setOnClickListener(this);
        ivDetailsShared.setOnClickListener(this);
        ivAccountUpdates1.setOnClickListener(this);

        enableActionUpdate();
        enableDetailsShared();
        enableAccountUpdate();

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.iv_account_updates:
                enableAccountUpdate();
                break;

            case R.id.iv_details_shared:
                enableDetailsShared();
                break;

            case R.id.iv_account_updates1:
                enableActionUpdate();
                break;
        }
    }
    private void enableAccountUpdate() {

        if (isAccountUpdates) {
            ivAccountUpdates.setImageResource(R.drawable.ic_switch_on);
            isAccountUpdates = false;
        } else {
            ivAccountUpdates.setImageResource(R.drawable.ic_switch_off);
            isAccountUpdates = true;
        }
    }

    private void enableDetailsShared() {

        if (isDetailsShared) {
            ivDetailsShared.setImageResource(R.drawable.ic_switch_on);
            isDetailsShared = false;
        } else {
            ivDetailsShared.setImageResource(R.drawable.ic_switch_off);
            isDetailsShared = true;
        }
    }

    private void enableActionUpdate() {

        if (isActionUpdates) {
            ivAccountUpdates1.setImageResource(R.drawable.ic_switch_on);
            isActionUpdates = false;
        } else {
            ivAccountUpdates1.setImageResource(R.drawable.ic_switch_off);
            isActionUpdates = true;
        }
    }
        }


