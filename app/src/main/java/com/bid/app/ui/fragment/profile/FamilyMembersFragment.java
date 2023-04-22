package com.bid.app.ui.fragment.profile;

import android.app.Activity;
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
import com.bid.app.adaper.FamilyListAdapter;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.FamilyMembersResponse;
import com.bid.app.model.view.Country;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.FamilyData;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FamilyMembersFragment extends BaseFragment {

    private static final String TAG = FamilyMembersFragment.class.getSimpleName();

    private SessionManager sessionManager;
    private APIInterface apiInterface;
//    private EditText etNationality;
    RecyclerView recyclerView;
    private AppCompatButton btnAdd;
    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile_familymembers, container, false);

        sessionManager = new SessionManager(Objects.requireNonNull(requireActivity()));
        apiInterface = APIClient.getClient().create(APIInterface.class);

        ((DashboardActivity) requireActivity()).setTitleAndImage("Family members", R.drawable.ic_back_arrow, false);

        initController(view);

        run();

        return view;
    }

    public void run() {
        getFamilyMembers();
    }

    @Override
    public void clickOnCountry(int position, Country c) {
        super.clickOnCountry(position, c);
        sessionManager.setCountry(c.getName());
    }

    private void initController(View view) {
        recyclerView = view.findViewById(R.id.family_list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.context);
        recyclerView.setLayoutManager(layoutManager);
        btnAdd = view.findViewById(R.id.btn_add_family_member);
        btnAdd.setOnClickListener(this);
    }

    private void initAdapter(final ArrayList<FamilyData> idDocumentsData) {
        Activity activity = getActivity();
        if (activity != null) {
            if (idDocumentsData.size() > 0) {
                FamilyListAdapter idDocumentsHistoryAdapter = new FamilyListAdapter(requireActivity(), idDocumentsData, FamilyMembersFragment.this);
                recyclerView.setAdapter(idDocumentsHistoryAdapter);
                recyclerView.setVisibility(View.VISIBLE);

            } else {
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.btn_add_family_member:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new AddEditFamilyMemberFragment(), R.id.frame_layout, true, false, false, null);
                break;
        }

    }

    @Override
    public void clickFamilyData(int position, FamilyData familyData) {
        Bundle bundle =  new Bundle();
        bundle.putSerializable(IBundle.BUNDLE_FAMILY_MEMBER, familyData);
        AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new AddEditFamilyMemberFragment(), R.id.frame_layout, true, false, true, bundle);
    }

    private void getFamilyMembers() {
        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);
        final Call<FamilyMembersResponse> call = apiInterface.listFamilyMembers(sessionManager.getAccessToken(), "profile");
        call.enqueue(new Callback<FamilyMembersResponse>() {
            @Override
            public void onResponse(Call<FamilyMembersResponse> call, Response<FamilyMembersResponse> response) {

                CustomProgressDialog.getInstance().dismissDialog();
                if (response.body() != null) {
                    final FamilyMembersResponse familyMembersResponse = response.body();
                    final ErrorStatus status = familyMembersResponse.getErrorStatus();

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        initAdapter((ArrayList<FamilyData>) familyMembersResponse.getData());
                    } else {
                        AlertUtils.showAlerterWarning(requireActivity(), status.getMessage());
                    }

                } else {
                    Logger.e(TAG, "response.body() == null");
                }
            }

            @Override
            public void onFailure(Call<FamilyMembersResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }
}