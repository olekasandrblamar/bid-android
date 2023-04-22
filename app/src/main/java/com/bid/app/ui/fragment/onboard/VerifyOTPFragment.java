package com.bid.app.ui.fragment.onboard;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.bid.app.R;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.request.VerifyPhoneRequest;
import com.bid.app.model.response.StatusResponse;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Constants;
import com.bid.app.util.Logger;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOTPFragment extends BaseFragment {

    private static final String TAG = VerifyOTPFragment.class.getSimpleName();

    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private AppCompatTextView tvVerificationType;
    private AppCompatTextView tvVerificationTypeCode;
    private AppCompatTextView tvResendCode;

    private AppCompatEditText etOne;
    private AppCompatEditText etTwo;
    private AppCompatEditText etThree;
    private AppCompatEditText etFour;


    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_otp_verify, container, false);

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

    private void initToolBar(View view) {

        final ImageView ivBack = view.findViewById(R.id.iv_back_tool_bar);
        final AppCompatTextView tvTitle = view.findViewById(R.id.tv_title_tool_bar);
        final ImageView iv_right_tool_bar = view.findViewById(R.id.iv_right_tool_bar);
        iv_right_tool_bar.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.title_verification_code));

        ivBack.setOnClickListener(this);

    }

    private void initController(View view) {

        tvVerificationType = view.findViewById(R.id.tv_text_two_otp_verify);
        tvVerificationTypeCode = view.findViewById(R.id.tv_text_verification_otp_verify);
        tvResendCode = view.findViewById(R.id.tv_resend_otp_verify);

        etOne = view.findViewById(R.id.et_one_verify_otp);
        etTwo = view.findViewById(R.id.et_two_verify_otp);
        etThree = view.findViewById(R.id.et_three_verify_otp);
        etFour = view.findViewById(R.id.et_four_verify_otp);

        final AppCompatButton btnVerify = view.findViewById(R.id.btn_checkout_otp);

        btnVerify.setOnClickListener(this);

        if ("EMAIL_FRAGMENT".equalsIgnoreCase(getFragmentFrom())) {
            tvVerificationType.setText(getValue());
            tvVerificationTypeCode.setText("Email Verification Code");
        } else {
            tvVerificationType.setText(getMobileNumber());
            tvVerificationTypeCode.setText("Mobile Verification Code");
        }

        etOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etTwo.setFocusableInTouchMode(true);
                etTwo.requestFocus();

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etThree.setFocusableInTouchMode(true);
                etThree.requestFocus();

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etFour.setFocusableInTouchMode(true);
                etFour.requestFocus();

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.iv_back_tool_bar:
                getFragmentManager().popBackStack();
                break;

            case R.id.tv_resend_otp_verify:
                calVerifyPhone();
                break;

            case R.id.btn_checkout_otp:
                validateOTP();
                break;
        }
    }

    private void validateOTP() {

        final boolean isOTPEntered = StringUtils.isEmpty(Objects.requireNonNull(etOne.getText()).toString())
                || StringUtils.isEmpty(Objects.requireNonNull(etTwo.getText()).toString())
                || StringUtils.isEmpty(Objects.requireNonNull(etThree.getText()).toString())
                || StringUtils.isEmpty(Objects.requireNonNull(etFour.getText()).toString());

        if (isOTPEntered) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter OTP to verify!");
        } else {
            final String code = etOne.getText().toString() + etTwo.getText().toString() + etThree.getText().toString() + etFour.getText().toString();
            calVerifyCode(code);
        }
    }

    private void calVerifyCode(final String code) {

        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);

        final VerifyPhoneRequest request = new VerifyPhoneRequest();
        request.setMobile(getMobileNumber());
        request.setCode(code);
        Logger.e(TAG, "request : " + request.toString());

        final Call<StatusResponse> call = apiInterface.verifyPhone1(request);
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();

                if (response.body() != null) {
                    Logger.e(TAG, "response.body() " + response.body().toString());

                    final StatusResponse statusResponse = response.body();
                    final ErrorStatus status = statusResponse.getError();

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        AlertUtils.showAlerterSuccess(requireActivity(), status.getMessage());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                final Bundle bundle = new Bundle();
                                bundle.putString(IBundle.BUNDLE_MOBILE_NUMBER, getMobileNumber());
                                bundle.putString(IBundle.BUNDLE_CODE, code);
                                bundle.putString(IBundle.BUNDLE_FRAGMENT_FROM, "OTP_FRAGMENT");
                                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new CreatePasswordFragment(), R.id.frame_layout, true, false, true, bundle);
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
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }

    private void calVerifyPhone() {

        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);

        final VerifyPhoneRequest request = new VerifyPhoneRequest();
        request.setMobile(getMobileNumber());
        Logger.e(TAG, "request : " + request.toString());

        final Call<StatusResponse> call = apiInterface.verifyPhone1(request);
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();

                if (response.body() != null) {
                    Logger.e(TAG, "response.body() " + response.body().toString());

                    final StatusResponse statusResponse = response.body();
                    final ErrorStatus status = statusResponse.getError();

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        AlertUtils.showAlerterSuccess(requireActivity(), status.getMessage());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                final Bundle bundle = new Bundle();
                                bundle.putString(IBundle.BUNDLE_MOBILE_NUMBER, getMobileNumber());
                                bundle.putString(IBundle.BUNDLE_FRAGMENT_FROM, Constants.MOBILE_NUMBER_FRAGMENT);
                                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new CreatePasswordFragment(), R.id.frame_layout, true, false, true, bundle);
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
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }
}
