package com.bid.app.ui.fragment.onboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.bid.app.R;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.model.request.ForgetPasswordRequest;
import com.bid.app.model.response.ForgetPasswordResponse;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.retrofit.APIClient;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Utils;

import org.apache.commons.lang3.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordFragment extends BaseFragment {

    private static final String TAG = ForgetPasswordFragment.class.getSimpleName();

    private APIInterface apiInterface;

    private AppCompatEditText etMobile;

    private AppCompatButton btnSendCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_forget_password, container, false);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        initToolBar(view);
        initController(view);

        return view;
    }

    private void initToolBar(View view) {

        final ImageView ivBack = view.findViewById(R.id.iv_back_tool_bar);
        final AppCompatTextView tvTitle = view.findViewById(R.id.tv_title_tool_bar);
        final ImageView iv_right_tool_bar = view.findViewById(R.id.iv_right_tool_bar);
        iv_right_tool_bar.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.title_forget_password));

        ivBack.setOnClickListener(this);

    }

    private void initController(View view) {

        etMobile = view.findViewById(R.id.et_email_forget_password);
        btnSendCode = view.findViewById(R.id.btn_send_code);

        btnSendCode.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.iv_back_tool_bar:
                getFragmentManager().popBackStack();
                break;

            case R.id.btn_send_code:
                validatePassword();
                break;
        }
    }

    private void validatePassword() {
        if (StringUtils.isEmpty(etMobile.getText().toString())) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter your registered phone number!");
        }
//        else if (!Utils.isValidEmail(etMobile.getText().toString())) {
//            AlertUtils.showAlerterWarning(requireActivity(), "Please enter valid email address!");
//        }
        else {
            callForgetPasswordApi();
        }
    }

    private void callForgetPasswordApi() {

        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);

        final ForgetPasswordRequest request = new ForgetPasswordRequest();
        request.setMobile(etMobile.getText().toString());

        final Call<ForgetPasswordResponse> call = apiInterface.forgetPassword(request);
        call.enqueue(new Callback<ForgetPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgetPasswordResponse> call, Response<ForgetPasswordResponse> response) {

                CustomProgressDialog.getInstance().dismissDialog();

                final ForgetPasswordResponse forgetPasswordResponse = response.body();
                final ErrorStatus status = forgetPasswordResponse.getErrorStatus();

                if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                    AlertUtils.showAlerterSuccess(requireActivity(), status.getMessage());

                    getFragmentManager().popBackStack();
                } else {
                    AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ForgetPasswordResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }
}