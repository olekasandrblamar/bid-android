package com.bid.app.ui.fragment.profile;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bid.app.R;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.request.AddressRequestEdit;
import com.bid.app.model.request.ProfileRequest;
import com.bid.app.model.response.ProfileResponse;
import com.bid.app.model.view.Country;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Profile;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.onboard.CountryListFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Constants;
import com.bid.app.util.Logger;
import com.bid.app.util.SessionManagerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileUpdateFragment extends BaseFragment {

    private static final String TAG = ProfileUpdateFragment.class.getSimpleName();

    private SessionManager sessionManager;
    private APIInterface apiInterface;
    private EditText etFirstName;
    private EditText et_last_name_profile_view;
    private EditText etDob;
    private EditText etEmail;
    private EditText etMobile;
    private EditText et_address_profile_view;
    private EditText et_address_postal_code_profile_view;
    private EditText etCity;
    //    private EditText etState;
    private TextView etCountry;
    private EditText etGender;
    private RadioGroup employment_status_radio_group;
    private EditText name_of_employer;
//    private EditText etNationality;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private AppCompatButton btnUpdate;
    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile_update, container, false);

        sessionManager = new SessionManager(Objects.requireNonNull(requireActivity()));
        apiInterface = APIClient.getClient().create(APIInterface.class);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_my_bid), R.drawable.ic_back_arrow, false);

        initController(view);

        return view;
    }

    @Override
    public void clickOnCountry(int position, Country c) {
        super.clickOnCountry(position, c);
        sessionManager.setCountry(c.getName());
    }

    private void initController(View view) {

        etFirstName = view.findViewById(R.id.et_first_name_profile_view);
        et_last_name_profile_view = view.findViewById(R.id.et_last_name_profile_view);
        etDob = view.findViewById(R.id.et_dob_profile_view);
        etMobile = view.findViewById(R.id.et_mobile_number_profile_view);
        etEmail = view.findViewById(R.id.et_email_profile_view);
        et_address_profile_view = view.findViewById(R.id.et_address_profile_view);
        et_address_postal_code_profile_view = view.findViewById(R.id.et_address_postal_code_profile_view);
        etCity = view.findViewById(R.id.et_city_profile_view);
        etCountry = view.findViewById(R.id.et_country_profile_view);
        etGender = view.findViewById(R.id.et_gender_profile_view);
        name_of_employer = view.findViewById(R.id.name_of_employer);
        employment_status_radio_group = view.findViewById(R.id.employment_status_radio_group);
        RadioButton part_time = employment_status_radio_group.findViewById(R.id.part_time);
        RadioButton full_time = employment_status_radio_group.findViewById(R.id.full_time);
        RadioButton self_employed = employment_status_radio_group.findViewById(R.id.self_employed);

        if (sessionManager.getEmploymentStatus().equalsIgnoreCase(getResources().getString(R.string.part_time))) {
            part_time.setChecked(true);
        } else if (sessionManager.getEmploymentStatus().equalsIgnoreCase(getResources().getString(R.string.full_time))) {
            full_time.setChecked(true);
        } else if (sessionManager.getEmploymentStatus().equalsIgnoreCase(getResources().getString(R.string.self_employed))) {
            self_employed.setChecked(true);
        }


        btnUpdate = view.findViewById(R.id.btn_update_profile_view);
        btnUpdate.setOnClickListener(this);
        etDob.setOnClickListener(this);

        etFirstName.setText(sessionManager.getFirstName());
        et_last_name_profile_view.setText(sessionManager.getLastName());
        Logger.e(TAG, "DOB" + sessionManager.getDob());
        etDob.setText(sessionManager.getDob());
        etEmail.setText(sessionManager.getEmail() != null ? sessionManager.getEmail() : "");
        et_address_profile_view.setText(sessionManager.getAddressOne() + " " + sessionManager.getAddressTwo());
        et_address_postal_code_profile_view.setText(sessionManager.getZipCode());
        etCity.setText(sessionManager.getCity());
        etCountry.setText(sessionManager.getCountry());
        etMobile.setText(sessionManager.getMobileNumber());
        etGender.setText(sessionManager.getGender());
        name_of_employer.setText(sessionManager.getDescription());


        etCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle bundle = new Bundle();
                bundle.putString(IBundle.BUNDLE_FRAGMENT_FROM, Constants.PROFILE_UPDATE_FRAGMENT);
                AppActivityManager.redirectTo((AppCompatActivity) Objects.requireNonNull(requireActivity()), new CountryListFragment(ProfileUpdateFragment.this), R.id.frame_layout, true, false, true, bundle);
            }
        });

        etGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = (LayoutInflater) Objects.requireNonNull(requireActivity()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View popupView = layoutInflater.inflate(R.layout.popup_menu_custom_layout, null);

                TextView male_tv = popupView.findViewById(R.id.male_tv);
                TextView female_tv = popupView.findViewById(R.id.female_tv);
                TextView rather_not_say_tv = popupView.findViewById(R.id.rather_not_say_tv);

                PopupWindow popupWindow = new PopupWindow(popupView, v.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(v);

                male_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etGender.setText(((TextView) v).getText().toString());
                        popupWindow.dismiss();
                    }
                });
                female_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etGender.setText(((TextView) v).getText().toString());
                        popupWindow.dismiss();
                    }
                });
                rather_not_say_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etGender.setText(((TextView) v).getText().toString());
                        popupWindow.dismiss();
                    }
                });

            }
        });

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.btn_update_profile_view:
                if (isDataSetValid()) {
                    callProfileUpdate();
                }
                break;

            case R.id.et_dob_profile_view:
                getDate();
                break;
        }

    }

    private boolean isDataSetValid() {
        if (etGender.getText().toString().isEmpty()) {
            Toast.makeText(DashboardActivity.context, "Please select your Gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void getDate() {

        final Calendar c = Calendar.getInstance();
        String date = etDob.getText().toString().trim();

        if (!date.isEmpty()) {
            String[] date_split = date.split("-");
            c.set(Calendar.YEAR, Integer.parseInt(date_split[0]));
            int month = Integer.parseInt(date_split[1]);
            if (month == 1) {
                month = 12;
            } else {
                month = month - 1;
            }
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date_split[2]));
        }

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etDob.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void callProfileUpdate() {
        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);

        final String fName = !StringUtils.isEmpty(etFirstName.getText().toString()) ? etFirstName.getText().toString() : "";
        final String lName = !StringUtils.isEmpty(et_last_name_profile_view.getText().toString()) ? et_last_name_profile_view.getText().toString() : "";
        final String email = !StringUtils.isEmpty(etEmail.getText().toString()) ? etEmail.getText().toString() : "";
        final String dob = !StringUtils.isEmpty(etDob.getText().toString()) ? etDob.getText().toString() : "";
        final String addressOne = !StringUtils.isEmpty(et_address_profile_view.getText().toString()) ? et_address_profile_view.getText().toString() : "";
        final String postalCode = !StringUtils.isEmpty(et_address_postal_code_profile_view.getText().toString()) ? et_address_postal_code_profile_view.getText().toString() : "";
        final String city = !StringUtils.isEmpty(etCity.getText().toString()) ? etCity.getText().toString() : "";
        final String country = !StringUtils.isEmpty(etCountry.getText().toString()) ? etCountry.getText().toString() : "";
        final String mobile = !StringUtils.isEmpty(etMobile.getText().toString()) ? etMobile.getText().toString() : "";
        final String gender = !StringUtils.isEmpty(etGender.getText().toString()) ? etGender.getText().toString() : "";
        final String description = !StringUtils.isEmpty(name_of_employer.getText().toString()) ? name_of_employer.getText().toString() : "";
//        final String nationality = !StringUtils.isEmpty(etNationality.getText().toString()) ? etNationality.getText().toString() : "";

        ProfileRequest request = new ProfileRequest();
        request.setFirstName(fName);
        request.setLastName(lName);
        request.setDob(dob);
        request.setUsername(sessionManager.getUserName());
        request.setMobile(mobile);
        request.setImage(sessionManager.getUserImage());
        request.setEmail(email);
        request.setDescription(description);
        request.setGender(gender);
        // get selected radio button from radioGroup
        int selectedId = employment_status_radio_group.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        RadioButton radioButton = (RadioButton) view.findViewById(selectedId);
        request.setEmployee_status(radioButton.getText().toString());

        AddressRequestEdit address = new AddressRequestEdit();
        address.setAddressLine1(addressOne);
        address.setCity(city);
        address.setState(country);
        address.setCountry(country);
        address.setZipCode(postalCode);
        request.setAddress(address);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            final String json = objectMapper.writeValueAsString(request);
            Logger.e(TAG, "ProfileRequest " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.e(TAG, "ProfileRequest " + request.toString());

        final Call<ProfileResponse> call = apiInterface.updateUser(sessionManager.getAccessToken(), request);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {

                CustomProgressDialog.getInstance().dismissDialog();
                if (response.body() != null) {
                    final ProfileResponse profileResponse = response.body();
                    final ErrorStatus status = profileResponse.getErrorStatus();

                    Gson gson = new Gson();
                    String loginResponseJson = gson.toJson(profileResponse);
                    Logger.e(TAG, "profileResponse: " + loginResponseJson);

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        AlertUtils.showAlerterSuccess(requireActivity(), status.getMessage());

                        Profile profile = profileResponse.getProfile();
                        profile.setDescription(request.getDescription());
                        profile.setGender(request.getGender());
                        profile.setEmployee_status(request.getEmployee_status());
                        profile.setDob(request.getDob());

                        SessionManagerUtils.setUserDetailsInSession(sessionManager, profile);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Objects.requireNonNull(requireActivity()).getSupportFragmentManager().popBackStack();
                            }
                        }, 1000);
                    } else {
                        AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                    }
                } else {
                    Logger.e(TAG, "response.body() == null");
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }
}