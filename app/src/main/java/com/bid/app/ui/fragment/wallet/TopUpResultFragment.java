package com.bid.app.ui.fragment.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bid.app.R;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.Questionnaire;
import com.bid.app.model.response.StatusResponse;
import com.bid.app.model.view.Schedule;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.home.HomeWalletFragment;
import com.bid.app.util.AlertUtils;

public class TopUpResultFragment extends BaseFragment { // ST
    AppCompatButton confirm;
    StatusResponse statusResponse;
    TextView message;
    ImageView result_img;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_top_up_result, container, false);
        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.checkout), R.drawable.ic_back_arrow, false);
        getBundle();
        init(view);
        return view;
    }
    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            statusResponse = (StatusResponse) bundle.getSerializable(IBundle.BUNDLE_STATUS_RESPONSE);
        }
    }

    public void init(View view) {
        confirm = view.findViewById(R.id.confirm);
        message = view.findViewById(R.id.message);
        result_img = view.findViewById(R.id.result_img);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new HomeWalletFragment(), R.id.frame_layout, false, false, false, null);
            }
        });
        message.setText(statusResponse.getError().getMessage());
        if(statusResponse.getError().getCode().equals("0")) {

        }
        else {
            result_img.setImageResource(R.drawable.ic_close_round_red);;
        }
    }

}
