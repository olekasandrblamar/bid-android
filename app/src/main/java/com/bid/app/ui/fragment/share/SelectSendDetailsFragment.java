package com.bid.app.ui.fragment.share;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.SelectSendDetailsAdapter;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.view.PersonalDetail;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectSendDetailsFragment extends BaseFragment {

    private static final String TAG = SelectSendDetailsFragment.class.getSimpleName();

    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private RecyclerView recyclerView;
    private AppCompatButton btnContinue;

    private ArrayList<PersonalDetail> selectedSendDetailsList = null;


    private boolean isSelected = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_select_send_details, container, false);

        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_select_details_send), R.drawable.ic_back_arrow, false);

        initController(view);

        return view;
    }

    private void initController(View view) {

        btnContinue = view.findViewById((R.id.btn_continue_select_send_details));

        recyclerView = view.findViewById(R.id.recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.context);
        recyclerView.setLayoutManager(layoutManager);

        btnContinue.setOnClickListener(this);

        getShareDetailsList();
    }

    @Override
    public void onResume() {
        super.onResume();
        selectedSendDetailsList = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_continue_select_send_details:
                if (selectedSendDetailsList.size() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(IBundle.BUNDLE_SELECTED_PERSONAL_DETAILS, selectedSendDetailsList);
                    AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new SendPreviewFragment(), R.id.frame_layout, true, false, true, bundle);
                } else {
                    AlertUtils.showAlerterWarning(requireActivity(), "Please select details to send!");
                }
                break;
        }
    }

    private void initAdapter(final List<PersonalDetail> list) {
        final SelectSendDetailsAdapter personalDetailAdapter = new SelectSendDetailsAdapter(requireActivity(), list, SelectSendDetailsFragment.this);
        recyclerView.setAdapter(personalDetailAdapter);
        personalDetailAdapter.notifyDataSetChanged();
    }

    private void getShareDetailsList() {
        List<PersonalDetail> list = new ArrayList<>();

        final PersonalDetail photo = new PersonalDetail("Photo", "", "", R.drawable.ic_selfie, 0, false);
        final String fullName = !StringUtils.isEmpty(sessionManager.getFirstName()) ? sessionManager.getFirstName() : "";
        final String dob = !StringUtils.isEmpty(sessionManager.getDob()) ? sessionManager.getDob() : "";
        final String mobile = !StringUtils.isEmpty(sessionManager.getMobileNumber()) ? sessionManager.getMobileNumber() : "";
        final String email = !StringUtils.isEmpty(sessionManager.getEmail()) ? sessionManager.getEmail() : "";

        final String addressOne = !StringUtils.isEmpty(sessionManager.getAddressOne()) ? sessionManager.getAddressOne() : "";
        final String addressTwo = !StringUtils.isEmpty(sessionManager.getAddressTwo()) ? sessionManager.getAddressTwo() : "";
        final String address = addressOne + " " + addressTwo;

        final String gender = !StringUtils.isEmpty(sessionManager.getGender()) ? sessionManager.getGender() : "";
        final String nationality = !StringUtils.isEmpty(sessionManager.getNationality()) ? sessionManager.getNationality() : "";

        //  final PersonalDetail photo = new PersonalDetail("Photo", "Update your profile picture", R.drawable.ic_selfie, R.drawable.ic_edit);
        final PersonalDetail fullNamePersonal = new PersonalDetail("Full Name", fullName, "Full Name", R.drawable.ic_fullname, 0, false);
        final PersonalDetail dobPersonal = new PersonalDetail("Date Of Birth", dob, "Date Of Birth", R.drawable.ic_calendar, 0, false);
        final PersonalDetail numberPersonal = new PersonalDetail("Mobile Number", mobile, "Mobile Number", R.drawable.ic_phone, 0, false);
        final PersonalDetail emailPersonal = new PersonalDetail("email", email, "Email", R.drawable.ic_mails, R.drawable.ic_edit, false);
        final PersonalDetail addressPersonal = new PersonalDetail("Address", address, "Address", R.drawable.ic_address, R.drawable.ic_edit, false);
        final PersonalDetail genderPersonal = new PersonalDetail("Gender", gender, "Gender", R.drawable.ic_gender, 0, false);
        final PersonalDetail nationalityPersonal = new PersonalDetail("Nationality", nationality, "Nationality", R.drawable.ic_flag, 0, false);

//        list.add(photo);
        list.add(fullNamePersonal);
        list.add(dobPersonal);
        list.add(numberPersonal);
        list.add(emailPersonal);
        list.add(addressPersonal);
        list.add(genderPersonal);
        list.add(nationalityPersonal);

        initAdapter(list);
    }

    @Override
    public void clickOnPersonalDetail(int position, PersonalDetail personalDetail) {
        super.clickOnPersonalDetail(position, personalDetail);

        if (!selectedSendDetailsList.contains(personalDetail)) {
            selectedSendDetailsList.add(personalDetail);
        } else {
            if (selectedSendDetailsList != null && !selectedSendDetailsList.isEmpty()) {
                personalDetail.setSelected(false);
                selectedSendDetailsList.remove(personalDetail);
            } else {
                Logger.e(TAG, "Data not yet selected!");
            }
        }

        Logger.e(TAG, "selectedSendDetailsList : " + selectedSendDetailsList.size());
    }

}