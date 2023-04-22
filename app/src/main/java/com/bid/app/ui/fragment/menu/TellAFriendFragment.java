package com.bid.app.ui.fragment.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.bid.app.R;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.model.request.TellAFriendRequest;
import com.bid.app.model.response.TellAFriendResponse;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.retrofit.APIClient;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TellAFriendFragment extends BaseFragment {

    private static final String TAG = TellAFriendFragment.class.getSimpleName();

    private APIInterface apiInterface;

    private AppCompatEditText etEmail;
    private AppCompatEditText etFeedBack;

    private ImageView ivClear;

    private AppCompatButton btnSend;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tell_a_friend, container, false);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_tell_a_friend), R.drawable.ic_menu, true);

        initController(view);

        return view;
    }

    private void initController(View view) {

        etEmail = view.findViewById(R.id.et_email_send_feedback);
        etFeedBack = view.findViewById(R.id.et_feedback);

        ivClear = view.findViewById(R.id.iv_email_send_feedback);

        btnSend = view.findViewById(R.id.btn_send_feedback);

        ivClear.setOnClickListener(this);
        btnSend.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.btn_send_feedback:
                callTellAFriendApi();
                break;
        }
    }

    private void callTellAFriendApi() {

        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);

        final TellAFriendRequest request = new TellAFriendRequest();
        request.setEmail(etEmail.getText().toString());
        request.setMessage(etFeedBack.getText().toString());
        Logger.e(TAG, "request : " + request);

        final Call<TellAFriendResponse> call = apiInterface.tellAFriend(request);
        call.enqueue(new Callback<TellAFriendResponse>() {
            @Override
            public void onResponse(Call<TellAFriendResponse> call, Response<TellAFriendResponse> response) {

                CustomProgressDialog.getInstance().dismissDialog();

                final TellAFriendResponse tellAFriendResponse = response.body();
                Logger.e(TAG, tellAFriendResponse.toString());
                final ErrorStatus status = tellAFriendResponse.getError();

                if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                    AlertUtils.showAlerterSuccess(requireActivity(), status.getMessage());
                } else {
                    AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                }
            }

            @Override
            public void onFailure(Call<TellAFriendResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });

    }
}
