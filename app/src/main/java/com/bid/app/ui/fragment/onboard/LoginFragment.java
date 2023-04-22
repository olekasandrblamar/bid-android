package com.bid.app.ui.fragment.onboard;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.bid.app.R;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.request.LoginRequest;
import com.bid.app.model.response.LoginResponse;
import com.bid.app.model.view.Country;
import com.bid.app.model.view.DeviceDetail;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.activity.OnBoardingActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Constants;
import com.bid.app.util.SessionManagerUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends BaseFragment {

    private static final String TAG = LoginFragment.class.getSimpleName();

    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private EditText phone_number_et;
    private EditText password_et;
    private TextView country_code;
    private AppCompatButton btnLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        initToolBar(view);
        initController(view);

        return view;
    }

    private void initToolBar(View view) {

        final ImageView ivBack = view.findViewById(R.id.iv_back_tool_bar);
        final ImageView iv_right_tool_bar = view.findViewById(R.id.iv_right_tool_bar);
        iv_right_tool_bar.setVisibility(View.GONE);
        final AppCompatTextView tvTitle = view.findViewById(R.id.tv_title_tool_bar);
        tvTitle.setText(getResources().getString(R.string.title_login));

        ivBack.setOnClickListener(this);

    }

    private void initController(View view) {
        country_code = view.findViewById(R.id.country_code);
        phone_number_et = view.findViewById(R.id.phone_number_et);
        password_et = view.findViewById(R.id.pin_et);
        /*Pattern pattern = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=).{8,}$");*/
        country_code.setOnClickListener(this);
        if(getCountry() != null) country_code.setText(getCountry().getDial_code());
        final AppCompatTextView tvForgetPassword = view.findViewById(R.id.tv_forget_password_login);

        btnLogin = view.findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);

    }
//    @Override
//    public void clickOnCountry(int position, Country c) {
//        super.clickOnCountry(position, c);
//        sessionManager.setCountry(c.getName());
//
//    }

    private Country getCountry() {
        final Bundle bundle = getArguments();
        return bundle != null ? bundle.getParcelable(IBundle.BUNDLE_COUNTRY_OBJECT) : null;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.country_code:
                final Bundle bundle = new Bundle();
                bundle.putString(IBundle.BUNDLE_FRAGMENT_FROM, Constants.LOG_IN_FRAGMENT);
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new CountryListFragment(LoginFragment.this), R.id.frame_layout, true, false, true, bundle);
                break;
            case R.id.iv_back_tool_bar:
//                Objects.requireNonNull(getFragmentManager()).popBackStack(); // When user is signed in...this won't work
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new GetStartedFragment(), R.id.frame_layout, false, false, false, null);
                break;

            case R.id.tv_forget_password_login:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new ForgetPasswordFragment(), R.id.frame_layout, true, false, false, null);
                break;

            case R.id.btn_login:
                validateLogin();
                break;
        }
    }

    private void validateLogin() {

        if (StringUtils.isEmpty(Objects.requireNonNull(phone_number_et.getText()).toString())) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter your mobile number!");
        } /*else if (!Utils.isValidEmail(etEmail.getText().toString())) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter valid email address!");
        } */ else if (StringUtils.isEmpty(password_et.getText().toString())) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter your password!");
        } else {
            callLoginApi();
        }
    }

    private void callLoginApi() {

        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);
        String username = country_code.getText().toString() + Objects.requireNonNull(phone_number_et.getText()).toString();
        final LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(Objects.requireNonNull(password_et.getText()).toString());
        request.setType("mobile");
        DeviceDetail deviceDetail = new DeviceDetail();
        deviceDetail.setDeviceToken(sessionManager.getFCMToken());
        request.setDeviceDetails(deviceDetail);

        final Call<LoginResponse> call = apiInterface.loginUser(request);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                CustomProgressDialog.getInstance().dismissDialog();
                final LoginResponse loginResponse = response.body();
                if (loginResponse != null) {
                    final ErrorStatus status = loginResponse.getError();

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        AlertUtils.showAlerterSuccess(requireActivity(), status.getMessage());
                        SessionManagerUtils.setUserDetailsInSession(sessionManager, loginResponse, password_et.getText().toString());
                        AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), DashboardActivity.class, true, false, false, null);
                    } else {
                        AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                    }
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
