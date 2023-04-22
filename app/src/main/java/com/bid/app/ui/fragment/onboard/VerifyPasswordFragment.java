package com.bid.app.ui.fragment.onboard;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.bid.app.R;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.request.LoginRequest;
import com.bid.app.model.response.LoginResponse;
import com.bid.app.model.view.DeviceDetail;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;
import com.bid.app.util.SessionManagerUtils;
import com.google.gson.Gson;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyPasswordFragment extends BaseFragment {

    private static final String TAG = VerifyPasswordFragment.class.getSimpleName();

    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private AppCompatTextView tvShowPin;
    private AppCompatTextView tvReLogin;

    private AppCompatEditText etPassword;

    private boolean isShowPin = true;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_pin, container, false);

        sessionManager = new SessionManager(Objects.requireNonNull(requireActivity()));
        apiInterface = APIClient.getClient().create(APIInterface.class);

        initToolBar(view);
        initController(view);

        return view;
    }

    private String getFragmentFrom() {
        final Bundle bundle = getArguments();
        return bundle != null ? bundle.getString(IBundle.BUNDLE_FRAGMENT_FROM) : "";
    }

    private String getValue() {
        final Bundle bundle = getArguments();
        return bundle != null ? bundle.getString(IBundle.BUNDLE_EMAIL) : "";
    }

    private String getMobileNumber() {
        final Bundle bundle = getArguments();
        return bundle != null ? bundle.getString(IBundle.BUNDLE_MOBILE_NUMBER) : sessionManager.getMobileNumber();
    }

    private String getOtpCode() {
        final Bundle bundle = getArguments();
        return bundle != null ? bundle.getString(IBundle.BUNDLE_CODE) : "";
    }

    private void initToolBar(View view) {

        final ImageView ivBack = view.findViewById(R.id.iv_back_tool_bar);
        final AppCompatTextView tvTitle = view.findViewById(R.id.tv_title_tool_bar);
        final ImageView iv_right_tool_bar = view.findViewById(R.id.iv_right_tool_bar);
        iv_right_tool_bar.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.text_create_pin));

        ivBack.setOnClickListener(this);

    }

    private void initController(View view) {

        tvShowPin = view.findViewById(R.id.tv_show_create_pin);
        tvReLogin = view.findViewById(R.id.tv_re_login_create_pin);

        etPassword = view.findViewById(R.id.et_one_verify_otp);
        /*Pattern pattern = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=).{8,}$");*/


        final AppCompatButton btnVerify = view.findViewById(R.id.btn_continue_create_pin);

        tvShowPin.setOnClickListener(this);
        tvReLogin.setOnClickListener(this);
        btnVerify.setOnClickListener(this);

        etPassword.requestFocus();

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.iv_back_tool_bar:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new LoginFragment(), R.id.frame_layout, false, false, false, null);
                break;

            case R.id.tv_re_login_create_pin:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new LoginFragment(), R.id.frame_layout, false, false, false, null);
                break;

            case R.id.tv_show_create_pin:
                if (isShowPin) {
                    tvShowPin.setTextColor(ContextCompat.getColor(requireActivity(), R.color.colorWhite));
                    tvShowPin.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.colorTextSecondary));
                    etPassword.setTransformationMethod(new PasswordTransformationMethod());
                    isShowPin = false;
                } else {
                    tvShowPin.setTextColor(ContextCompat.getColor(requireActivity(), R.color.colorTextSecondary));
                    tvShowPin.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.colorWhite));
                    etPassword.setTransformationMethod(null);
                    isShowPin = true;
                }


                break;

            case R.id.btn_continue_create_pin:
                validatePin();
                break;
        }
    }

    private void validatePin() {
        String password = etPassword.getText().toString();

        boolean passwordValidation = !password.isEmpty();


        if (!passwordValidation)
            AlertUtils.showAlerterWarning(requireActivity(), "Password must not be empty");

        if (passwordValidation) {
            callLoginApi(password);
        }


    }


    private void callLoginApi(final String password) {

        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);

        final LoginRequest request = new LoginRequest();
        request.setUsername(getMobileNumber());
        request.setPassword(password);
        request.setType("mobile");
        DeviceDetail deviceDetail = new DeviceDetail();
        deviceDetail.setDeviceToken(sessionManager.getFCMToken());
        request.setDeviceDetails(deviceDetail);

        final Call<LoginResponse> call = apiInterface.loginUser(request);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();


                if (response.body() != null) {
                    Logger.e(TAG, "response.body() " + response.body().toString());

                    final LoginResponse loginResponse = response.body();
                    final ErrorStatus status = loginResponse.getError();

                    Gson gson = new Gson();
                    String loginResponseJson = gson.toJson(loginResponse);
                    Logger.e(TAG, "loginResponseJson: " + loginResponseJson);

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        AlertUtils.showAlerterSuccess(requireActivity(), status.getMessage());
                        SessionManagerUtils.setUserDetailsInSession(sessionManager, loginResponse, password);
                        AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), DashboardActivity.class, true, false, false, null);
                    } else {
                        AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                    }
                } else {
                    Logger.e(TAG, "response.body() == null");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();


            }
        });
    }


}
