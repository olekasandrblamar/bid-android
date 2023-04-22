package com.bid.app.ui.fragment.onboard;

import android.os.Bundle;
import android.os.Handler;
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
import com.bid.app.model.view.Country;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Constants;
import com.bid.app.util.Logger;

import org.apache.commons.lang3.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyMobileNumberFragment extends BaseFragment {

    private static final String TAG = VerifyMobileNumberFragment.class.getSimpleName();

    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private AppCompatEditText etCountryCode;
    private AppCompatEditText etNumber;

    private AppCompatTextView tvText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_mobile_number, container, false);

        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        initToolBar(view);
        initController(view);

        return view;
    }

    private Country getCountry() {
        final Bundle bundle = getArguments();
        return bundle != null ? bundle.getParcelable(IBundle.BUNDLE_COUNTRY_OBJECT) : new Country();
    }

    private void initToolBar(View view) {

        final ImageView ivBack = view.findViewById(R.id.iv_back_tool_bar);
        final AppCompatTextView tvTitle = view.findViewById(R.id.tv_title_tool_bar);
        final ImageView iv_right_tool_bar = view.findViewById(R.id.iv_right_tool_bar);
        iv_right_tool_bar.setVisibility(View.GONE);

        tvTitle.setText(getResources().getString(R.string.title_b_id));

        ivBack.setOnClickListener(this);

    }

    private void initController(View view) {

        tvText = view.findViewById(R.id.tv_text_mobile_number);

        etCountryCode = view.findViewById(R.id.et_country_code_mobile_number);
        etNumber = view.findViewById(R.id.et_mobile_number);

        final AppCompatButton btnNext = view.findViewById(R.id.btn_next_mobile_number);

        tvText.setText("We'll send a verification code to \n this number");
        etCountryCode.setText(getCountry().getDial_code());

        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.iv_back_tool_bar:
                getFragmentManager().popBackStack();
                break;

            case R.id.btn_next_mobile_number:
                validateFields();
                break;
        }
    }

    private void validateFields() {

        if (StringUtils.isEmpty(etNumber.getText().toString())) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter mobile number!");
//        } else if (etNumber.getText().toString().length() < 7) {
//            AlertUtils.showAlerterWarning(requireActivity(), "Please enter valid mobile number!");
        } else {
            calVerifyPhone();
        }
    }

    private void calVerifyPhone() {

        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);

        final VerifyPhoneRequest request = new VerifyPhoneRequest();
        request.setMobile(getCountry().getDial_code() + etNumber.getText().toString());
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
                                bundle.putString(IBundle.BUNDLE_MOBILE_NUMBER, getCountry().getDial_code() + etNumber.getText().toString());
                                bundle.putString(IBundle.BUNDLE_FRAGMENT_FROM, Constants.MOBILE_NUMBER_FRAGMENT);
                                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new VerifyOTPFragment(), R.id.frame_layout, true, false, true, bundle);
                            }
                        }, Constants.HANDLER_TIMER);

                    } else {
                        AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                    }
                } else {
//                    Logger.e(TAG, "response.body() == null");
                    AlertUtils.showAlerterFailure(requireActivity(), response.message());

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
