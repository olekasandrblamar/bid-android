package com.bid.app.ui.fragment.onboard;

import android.os.Bundle;
import android.os.Handler;
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
import com.bid.app.model.request.SignUpRequest;
import com.bid.app.model.response.RegisterResponse;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Constants;
import com.bid.app.util.Logger;
import com.bid.app.util.Utils;

import org.apache.commons.lang3.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePasswordFragment extends BaseFragment {

    private static final String TAG = CreatePasswordFragment.class.getSimpleName();

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

        sessionManager = new SessionManager(requireActivity());
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
        return bundle != null ? bundle.getString(IBundle.BUNDLE_MOBILE_NUMBER) : "";
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

        final AppCompatButton btnVerify = view.findViewById(R.id.btn_continue_create_pin);

        tvShowPin.setOnClickListener(this);
        tvReLogin.setOnClickListener(this);
        btnVerify.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.iv_back_tool_bar:
                getFragmentManager().popBackStack();
                break;

            case R.id.tv_re_login_create_pin:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new VerifyMobileNumberFragment(), R.id.frame_layout, false, false, false, null);
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
        if (!Utils.isValidPassword(password)) {
            AlertUtils.showAlerterWarning(requireActivity(), "Password must comprise of:\nAt least one lowercase alphabet.\nAt least one uppercase albhabet.\nAt least one number from [0-9].\nLength must be greater than 7.");
        }
        else {
            callRegisterApi(password);
        }
//        boolean lowerCaseValidation = false;
//        boolean upperCaseValidation = false;
//        boolean numbersValidation = false;
//        boolean lengthValidation = password.length() > 7;
//
//        for (char c : password.toCharArray()) {
//            if (Character.isLowerCase(c)) lowerCaseValidation = true;
//            if (Character.isUpperCase(c)) upperCaseValidation = true;
//            if (Character.isDigit(c)) numbersValidation = true;
//
//        }
//
//        if (!lowerCaseValidation)
//            AlertUtils.showAlerterWarning(requireActivity(), "Password must contains at least one lowercase alphabet.");
//        if (!upperCaseValidation)
//            AlertUtils.showAlerterWarning(requireActivity(), "Password must contains at least one uppercase alphabet.");
//        if (!numbersValidation)
//            AlertUtils.showAlerterWarning(requireActivity(), "Password must contains at least one Digit [0-9]");
//        if (!lengthValidation)
//            AlertUtils.showAlerterWarning(requireActivity(), "Password Length must be greater than 7");
//
//        if(lowerCaseValidation&&upperCaseValidation&&numbersValidation&lengthValidation){
//            callRegisterApi(password);
//        }


    }

    private void callRegisterApi(final String password) {

        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);

        final SignUpRequest request = new SignUpRequest();
        request.setMobile(getMobileNumber());
        request.setCode(getOtpCode());
        request.setPassword(password);
        Logger.e(TAG, "request : " + request.toString());

        final Call<RegisterResponse> call = apiInterface.register(request);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();

                if (response.body() != null) {
                    Logger.e(TAG, "response.body() " + response.body().toString());

                    final RegisterResponse registerResponse = response.body();
                    final ErrorStatus status = registerResponse.getErrorStatus();

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        AlertUtils.showAlerterSuccess(requireActivity(), status.getMessage());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new LoginFragment(), R.id.frame_layout, false, false, true, null);
                            }
                        }, Constants.HANDLER_TIMER);

                    } else {
                        AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                    }
                } else {
                    Logger.e(TAG, "response.body() == null");
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }
}
