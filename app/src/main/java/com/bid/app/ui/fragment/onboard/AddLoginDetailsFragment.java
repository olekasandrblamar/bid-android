package com.bid.app.ui.fragment.onboard;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bid.app.R;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.model.request.RegisterRequest;
import com.bid.app.retrofit.APIClient;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.custom.ProgressDrawable;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;

import org.apache.commons.lang3.StringUtils;

import params.com.stepprogressview.StepProgressView;

public class AddLoginDetailsFragment extends BaseFragment {

    private static final String TAG = LoginFragment.class.getSimpleName();

    private APIInterface apiInterface;

    private AppCompatEditText etName;
    private AppCompatEditText etAddress;
    private AppCompatEditText etUsername;
    private AppCompatEditText etPassword;

    private ProgressBar progressBar;
    private StepProgressView stepProgressView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_add_login_details, container, false);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        initToolBar(view);
        initController(view);

        return view;
    }

    private String getValue() {
        final Bundle bundle = getArguments();
        return bundle != null ? bundle.getString(IBundle.BUNDLE_EMAIL) : "";
    }


    private void initToolBar(View view) {

        final ImageView ivBack = view.findViewById(R.id.iv_back_tool_bar);
        final AppCompatTextView tvTitle = view.findViewById(R.id.tv_title_tool_bar);
        final ImageView iv_right_tool_bar = view.findViewById(R.id.iv_right_tool_bar);
        iv_right_tool_bar.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.title_add_login_details));

        ivBack.setOnClickListener(this);

    }

    private void initController(View view) {

        etName = view.findViewById(R.id.et_login_name_add);
        etAddress = view.findViewById(R.id.et_address_name_add);
        etUsername = view.findViewById(R.id.et_user_name_add);
        etPassword = view.findViewById(R.id.et_password_add);

        progressBar = view.findViewById(R.id.progress_bar_add_email);
        stepProgressView = view.findViewById(R.id.step_progress_bar_add_email);

        final AppCompatImageView ivPassword = view.findViewById(R.id.iv_password_add);

        AppCompatButton btnSave = view.findViewById(R.id.btn_save_login_add);
        AppCompatButton btnCancel = view.findViewById(R.id.btn_cancel_login_add);

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        ivPassword.setOnClickListener(this);

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ProgressDrawable bgProgress = new ProgressDrawable(R.color.colorText, R.color.colorTextHint);
                progressBar.setProgressDrawable(bgProgress);

                if (charSequence.length() > 3) {
                    stepProgressView.setCurrentProgress(25);
                }

                if (charSequence.length() > 6) {
                    stepProgressView.setCurrentProgress(50);
                }

                if (charSequence.length() > 9) {
                    stepProgressView.setCurrentProgress(75);
                }

                if (charSequence.length() > 12) {
                    stepProgressView.setCurrentProgress(100);
                }

                if (charSequence.length() == 0) {
                    stepProgressView.setCurrentProgress(0);
                }

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

            case R.id.iv_password_add:

                break;

            case R.id.btn_save_login_add:
                validateAddLogin();
                break;

            case R.id.btn_cancel_login_add:
                getFragmentManager().popBackStack();
                break;
        }
    }

    private void validateAddLogin() {

        if (StringUtils.isEmpty(etName.getText().toString())) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter your name!");
        } else if (StringUtils.isEmpty(etAddress.getText().toString())) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter your website address!");
        } else if (StringUtils.isEmpty(etUsername.getText().toString())) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter your username!");
        } else if (StringUtils.isEmpty(etPassword.getText().toString())) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter your password!");
        } else {
            callSignUpApi();
        }
    }

    private void callSignUpApi() {

        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);

        final RegisterRequest request = new RegisterRequest();
/*
        request.setUsername(etUsername.getText().toString());
        request.setFirst_name(etName.getText().toString());
        request.setLast_name("");
        request.setEmail(getValue());
*/
        request.setPassword(etPassword.getText().toString());

        Logger.e(TAG, "request " + request.toString());

       /* final Call<RegisterResponse> call = apiInterface.register(request);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                CustomProgressDialog.getInstance().dismissDialog();

                final RegisterResponse registerResponse = response.body();
                final ErrorStatus status = registerResponse.getErrorStatus();

                if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                    AlertUtils.showAlerterSuccess(requireActivity(), status.getMessage());
                    AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new LoginFragment(), R.id.frame_layout, false, false, false, null);
                } else {
                    AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });*/
    }
}
