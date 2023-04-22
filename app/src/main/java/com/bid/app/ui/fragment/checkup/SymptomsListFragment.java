package com.bid.app.ui.fragment.checkup;

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
import com.bid.app.adaper.SymptomsAdapter;
import com.bid.app.constants.Global;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.CheckupTest;
import com.bid.app.model.response.SymptomsResponse;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Symptoms;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SymptomsListFragment extends BaseFragment {

    private static final String TAG = SymptomsListFragment.class.getSimpleName();

    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private RecyclerView recyclerView;

    private List<Symptoms> selectedSymptomsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_symptons_list, container, false);

        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_symptoms), R.drawable.ic_back_arrow, false);

        initController(view);

        return view;
    }

    private void initController(View view) {

        final AppCompatButton btnNext = view.findViewById(R.id.btn_next_symptoms);

        recyclerView = view.findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.context);
        recyclerView.setLayoutManager(layoutManager);

        btnNext.setOnClickListener(this);

        callSymptomsApi();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(selectedSymptomsList != null && !selectedSymptomsList.isEmpty()){
            selectedSymptomsList.clear();
        }

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.btn_next_symptoms:
                if (selectedSymptomsList != null && selectedSymptomsList.isEmpty()) {
                    AlertUtils.showAlerterWarning(requireActivity(), "Please select symptoms");
                } else {
                    int level = 0;
                    for (Symptoms symptom : selectedSymptomsList) {


                            level = level + Integer.parseInt(symptom.getLevel());

                    }

                    if (level >= 5) {
                        Global.isCheckUpNeeded = true;
                    } else {
                        Global.isCheckUpNeeded = false;
                    }

                    Logger.e(TAG, "isCheckUpNeeded : " + Global.isCheckUpNeeded);

                    CheckupTest checkupTest =new CheckupTest();
                    List<String> symptomsIds = new ArrayList<>();

                    for (Symptoms symptoms :selectedSymptomsList) symptomsIds.add(symptoms.getId());
                    checkupTest.setSymptomsIds(symptomsIds);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("Checkup Test" , checkupTest);
                    AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new QuestionsSetOneFragment(), R.id.frame_layout, true, false, true, bundle);
                }
                break;
        }
    }

    private void initAdapter(final List<Symptoms> list) {
        SymptomsAdapter symptomsAdapter = new SymptomsAdapter(requireActivity(), list, SymptomsListFragment.this);
        recyclerView.setAdapter(symptomsAdapter);
        symptomsAdapter.notifyDataSetChanged();
    }

    private void callSymptomsApi() {

        Logger.e(TAG, sessionManager.getAccessToken());

        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);

        final Call<SymptomsResponse> call = apiInterface.getSymptoms();
        call.enqueue(new Callback<SymptomsResponse>() {
            @Override
            public void onResponse(Call<SymptomsResponse> call, Response<SymptomsResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                if (response.body() != null) {
                    final SymptomsResponse body = response.body();
                    final ErrorStatus status = body.getError();

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        final ArrayList<Symptoms> symptomsList = body.getSymptomsList();
                        initAdapter(symptomsList);
                    } else {
                        AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                    }
                } else {
                    Logger.e(TAG, "response.body() == null");
                }
            }

            @Override
            public void onFailure(Call<SymptomsResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });

    }

    @Override
    public void clickOnSymptoms(int position, Symptoms symptoms) {
        super.clickOnSymptoms(position, symptoms);
        Logger.e(TAG, "symptoms getLevel " + symptoms.getLevel());

        if (selectedSymptomsList.contains(symptoms)) {
            selectedSymptomsList.remove(symptoms);
        } else {
            selectedSymptomsList.add(symptoms);
        }

        Logger.e(TAG, "symptoms size " + selectedSymptomsList.size());
    }
}