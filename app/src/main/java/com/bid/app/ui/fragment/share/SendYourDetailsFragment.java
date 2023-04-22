package com.bid.app.ui.fragment.share;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.bid.app.R;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;

public class SendYourDetailsFragment extends BaseFragment {

    private static final String TAG = SendYourDetailsFragment.class.getSimpleName();

    private AppCompatTextView tvDoNotShow;

    private AppCompatButton btnSendDetails;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_send_your_details, container, false);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_send_details), R.drawable.ic_back_arrow, false);

        initController(view);

        return view;
    }

    private void initController(View view) {

        tvDoNotShow = view.findViewById(R.id.tv_do_not_show_send_details);

        btnSendDetails = view.findViewById(R.id.btn_get_started_send_details);

        tvDoNotShow.setOnClickListener(this);
        btnSendDetails.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.tv_do_not_show_send_details:

                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new SelectSendDetailsFragment(), R.id.frame_layout, false, false, false, null);
                break;

            case R.id.btn_get_started_send_details:

                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new SelectSendDetailsFragment(), R.id.frame_layout, false, false, false, null);
                break;
        }
    }

}
