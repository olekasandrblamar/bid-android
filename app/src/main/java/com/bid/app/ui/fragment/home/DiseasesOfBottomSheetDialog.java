package com.bid.app.ui.fragment.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.OptionsAdapter;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.request.BIDRequestModel;
import com.bid.app.model.response.BIDResponse;
import com.bid.app.model.response.Disease;
import com.bid.app.model.response.DiseaseResponse;
import com.bid.app.model.response.Questionnaire;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Symptoms;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.fragment.triage.TriageShowFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiseasesOfBottomSheetDialog extends BaseFullScreenBottomSheetDialog{

    private SessionManager sessionManager;
    private TextView header_text;
    private APIInterface apiInterface;
    public static final String TAG = DiseasesOfBottomSheetDialog.class.getSimpleName();
    private static DiseasesOfBottomSheetDialog diseasesOfBottomSheetDialog;
    private ArrayList<String> reasons = new ArrayList<>();
    private OptionsAdapter optionsAdapter;
    private RecyclerView reasons_rv;
    private ConstraintLayout back_cl;
    private ConstraintLayout cross_cl;
    private TextView next_question;
    private String covid_disease = "COVID-19";
    private String monkey_disease = "Monkey Pox";
    public static DiseasesOfBottomSheetDialog newInstance() {
        Bundle args = new Bundle();
        diseasesOfBottomSheetDialog = new DiseasesOfBottomSheetDialog();
        diseasesOfBottomSheetDialog.setArguments(args);
        return diseasesOfBottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.disease, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initIds(view);
        initFragment();
    }
    private void initFragment() {
        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Objects.requireNonNull(back_cl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Objects.requireNonNull(cross_cl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Objects.requireNonNull(next_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionsAdapter.getSelectedPosition() == RecyclerView.NO_POSITION) {
//                    AlertUtils.showAlerterWarning(requireActivity(), "Please select any one valid reason");
                    Toast.makeText(DashboardActivity.context, "Please select at least one", Toast.LENGTH_LONG).show();
                }
                else {

                    Questionnaire questionnaire = new Questionnaire();
                    questionnaire.setReasonForTest(reasons.get(optionsAdapter.getSelectedPosition()));

                    if (reasons.get(optionsAdapter.getSelectedPosition()).equalsIgnoreCase(covid_disease)) {

                        ReasonForTestBottomSheetDialog reasonForTestBottomSheetDialog = ReasonForTestBottomSheetDialog.newInstance(questionnaire);
                        reasonForTestBottomSheetDialog.show(requireActivity().getSupportFragmentManager(), ReasonForTestBottomSheetDialog.TAG);
                    }
                    else if (reasons.get(optionsAdapter.getSelectedPosition()).equalsIgnoreCase(monkey_disease)) {

                        dismiss();
                        AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TriageShowFragment(), R.id.frame_layout, true, false, false, null);
                    }
                }
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        getDiseaseType();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.context);
        Objects.requireNonNull(reasons_rv).setLayoutManager(layoutManager);
        initAdapter();
    }

    private void initIds(View view) {
        back_cl = view.findViewById(R.id.back_cl);
        cross_cl = view.findViewById(R.id.cross_cl);
        next_question = view.findViewById(R.id.next_question);
        reasons_rv = view.findViewById(R.id.options_rv);
        header_text = view.findViewById(R.id.header_text);
    }

    private void getDiseaseType() {

//        CustomProgressDialog.getInstance().showDialog(this.getContext(), true);
        header_text.setText("Loading...");
        final Call<DiseaseResponse> call = apiInterface.getDiseasesType();
        call.enqueue(new Callback<DiseaseResponse>() {
            @Override
            public void onResponse(Call<DiseaseResponse> call, Response<DiseaseResponse> response) {
                final DiseaseResponse diseaseResponse = response.body();
                if (diseaseResponse != null) {
                    reasons = new ArrayList<String>();
                    final ArrayList<Disease> diseaseArrayList = diseaseResponse.getDiseaseArrayList();
                    int i;
                    for(i=0;i<diseaseArrayList.size();i++) {
                        Disease p = diseaseResponse.getDisease(i);
                        reasons.add(p.getName());
                    }
                    initAdapter();
//                    CustomProgressDialog.getInstance().dismissDialog();
                    header_text.setText("Choose a diseases type");
                }
            }

            @Override
            public void onFailure(Call<DiseaseResponse> call, Throwable t) {
                t.printStackTrace();
//                CustomProgressDialog.getInstance().dismissDialog();
                header_text.setText("Loading Failed.");
            }
        });
    }

    public void initAdapter() {
        optionsAdapter = new OptionsAdapter(reasons, position -> {
            optionsAdapter.notifyDataSetChanged();
        });
        reasons_rv.setAdapter(optionsAdapter);
    }
}
