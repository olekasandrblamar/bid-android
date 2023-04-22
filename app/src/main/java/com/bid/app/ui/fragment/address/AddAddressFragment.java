package com.bid.app.ui.fragment.address;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bid.app.R;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.model.request.AddressRequest;
import com.bid.app.model.response.AddressAddResponse;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;
import com.bid.app.util.Utils;

import org.apache.commons.lang3.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressFragment extends BaseFragment {

    private static final String TAG = AddAddressFragment.class.getSimpleName();

    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private EditText etName;
    private EditText etAddress1;
    private EditText etAddress2;
    private EditText etCity;
    private EditText etState;
    private EditText etCountry;
    private EditText etZipCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_address, container, false);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.hint_add_address), R.drawable.ic_back_arrow, false);

        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        initController(view);
        return view;
    }

    private void initController(View view) {

        etName = view.findViewById(R.id.et_name_address);
        etAddress1 = view.findViewById(R.id.et_line_1_address);
        etAddress2 = view.findViewById(R.id.et_line_2_address);
        etCity = view.findViewById(R.id.et_city_address);
        etState = view.findViewById(R.id.et_state_address);
        etCountry = view.findViewById(R.id.et_country_address);
        etZipCode = view.findViewById(R.id.et_zip_code_address);

        final Button btnAddAddress = view.findViewById(R.id.btn_add_address);

        btnAddAddress.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_add_address:
                doValidation();
                break;
        }
    }

    private void doValidation() {
        if (StringUtils.isEmpty(etName.getText().toString())) {
            AlertUtils.showAlerterWarning((AppCompatActivity) requireActivity(), "Name should not be empty!");
        } else if (StringUtils.isEmpty(etAddress1.getText().toString())) {
            AlertUtils.showAlerterWarning((AppCompatActivity) requireActivity(), "Address should not be empty!");
        } else if (StringUtils.isEmpty(etCity.getText().toString())) {
            AlertUtils.showAlerterWarning((AppCompatActivity) requireActivity(), "City should not be empty!");
        } else if (StringUtils.isEmpty(etState.getText().toString())) {
            AlertUtils.showAlerterWarning((AppCompatActivity) requireActivity(), "State should not be empty!");
        } else if (StringUtils.isEmpty(etCountry.getText().toString())) {
            AlertUtils.showAlerterWarning((AppCompatActivity) requireActivity(), "Country should not be empty!");
        } else if (StringUtils.isEmpty(etZipCode.getText().toString())) {
            AlertUtils.showAlerterWarning((AppCompatActivity) requireActivity(), "Zipcode should not be empty!");
        } else {
            Utils.hideKeyboard((AppCompatActivity) requireActivity());
            callAddAddressApi();
        }
    }

    private void callAddAddressApi() {
        final AddressRequest request = new AddressRequest();
        request.setName(etName.getText().toString());
        request.setAddressLine1(etAddress1.getText().toString());
        request.setAddressLine2(etAddress2.getText().toString());
        request.setCity(etCity.getText().toString());
        request.setState(etState.getText().toString());
        request.setCountry(etCountry.getText().toString());
        request.setZipCode(etZipCode.getText().toString());
        request.setIsDefault("1");

        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);
        Logger.e(TAG, "getAccessToken " + sessionManager.getAccessToken());

        Call<AddressAddResponse> listResponseCall = apiInterface.addUserAddress(request, sessionManager.getAccessToken());
        listResponseCall.enqueue(new Callback<AddressAddResponse>() {
            @Override
            public void onResponse(Call<AddressAddResponse> call, Response<AddressAddResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();

                Logger.e(TAG, "response.body() " + response.body());
                final AddressAddResponse addressListResponse = response.body();
                final ErrorStatus status = addressListResponse.getErrorStatus();

                if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                    AlertUtils.showAlerterSuccess(requireActivity(), status.getMessage());
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                }
            }

            @Override
            public void onFailure(Call<AddressAddResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }
}
