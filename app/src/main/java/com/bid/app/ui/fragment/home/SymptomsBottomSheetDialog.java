package com.bid.app.ui.fragment.home;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.BookATestSymptomsAdapter;
import com.bid.app.constants.Global;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.Questionnaire;
import com.bid.app.model.response.SymptomsResponse;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Symptoms;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.activity.ScheduleTestActivity;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;
import com.bid.app.util.Utils;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SymptomsBottomSheetDialog extends BaseFullScreenBottomSheetDialog {

    public static final String TAG = SymptomsBottomSheetDialog.class.getSimpleName();
    public static final String NONE_OF_THE_ABOVE = "none of the above";
    private static SymptomsBottomSheetDialog reasonForTestBottomSheetDialogFragment;
    private BookATestSymptomsAdapter symptomsAdapter;
    private RecyclerView reasons_rv;
    private ConstraintLayout back_cl;
    private ConstraintLayout cross_cl;
    private TextView next_question;
    private SessionManager sessionManager;
    private APIInterface apiInterface;
    private ProgressBar progress_bar;
    private ArrayList<Symptoms> symptomsList;
    private Questionnaire questionnaire;

    public static SymptomsBottomSheetDialog newInstance(Questionnaire questionnaire) {
        Bundle args = new Bundle();
        reasonForTestBottomSheetDialogFragment = new SymptomsBottomSheetDialog();
        reasonForTestBottomSheetDialogFragment.setArguments(args);
        reasonForTestBottomSheetDialogFragment.questionnaire = questionnaire;
        return reasonForTestBottomSheetDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.symptoms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initIds(view);
        initFragment();
        callSymptomsApi();

    }

    private void callSymptomsApi() {

        Logger.e(TAG, sessionManager.getAccessToken());

        final Call<SymptomsResponse> call = apiInterface.getSymptoms();
        call.enqueue(new Callback<SymptomsResponse>() {
            @Override
            public void onResponse(Call<SymptomsResponse> call, Response<SymptomsResponse> response) {
                progress_bar.setVisibility(View.GONE);
                if (response.body() != null) {
                    final SymptomsResponse body = response.body();
                    final ErrorStatus status = body.getError();

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        symptomsList = moveNoneOfTheAboveToTheBottom(body.getSymptomsList());
                        initRecyclerView();
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
                progress_bar.setVisibility(View.GONE);
            }
        });

    }

    private ArrayList<Symptoms> moveNoneOfTheAboveToTheBottom(ArrayList<Symptoms> symptomsList) {
        if (symptomsList.size() > 0) {
            Symptoms symptoms = null;
            for (int i = 0; i < symptomsList.size(); i++) {
                if (symptomsList.get(i).getName().equalsIgnoreCase(NONE_OF_THE_ABOVE)) {
                    symptoms = symptomsList.get(i);
                    symptomsList.remove(i);
                    break;
                }
            }
            if (symptoms != null) {
                symptomsList.add(symptoms);
            }
        }

        return symptomsList;
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
                if (symptomsAdapter != null) {
                    if (symptomsAdapter.getSelectedSymptomsPositions().size() == 0) {
                        Toast toast = Toast.makeText(DashboardActivity.context, "Please select a symptom", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {

                        ArrayList<Integer> selectedSymptomsIds = new ArrayList<>();
                        for (int i = 0; i < symptomsAdapter.getSelectedSymptomsPositions().size(); i++) {
                            int selectedSymptomsPosition = Integer.parseInt(symptomsAdapter.getSelectedSymptomsPositions().get(i));
                            selectedSymptomsIds.add(Integer.valueOf(symptomsList.get(selectedSymptomsPosition).getId()));
                            Log.d("Adding Symptom:", symptomsList.get(selectedSymptomsPosition).getName());
                        }

                        questionnaire.setSymptoms(selectedSymptomsIds);
                        Utils.dismissAllDialogs(Objects.requireNonNull(getFragmentManager()));
                        Global.isHealthCheckUp = true;
                        AppActivityManager.redirectWithSerializedQuestionnaireObject((AppCompatActivity) requireActivity(), ScheduleTestActivity.class, false, false, null, questionnaire, null);

                    }
                }
            }
        });

    }

    private void initRecyclerView() {

        final LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.context);
        Objects.requireNonNull(reasons_rv).setLayoutManager(layoutManager);
        symptomsAdapter = new BookATestSymptomsAdapter(symptomsList, position -> {

        });
        reasons_rv.setAdapter(symptomsAdapter);
        reasons_rv.setVisibility(View.VISIBLE);
    }

    private void initIds(View view) {
        back_cl = view.findViewById(R.id.back_cl);
        cross_cl = view.findViewById(R.id.cross_cl);
        next_question = view.findViewById(R.id.next_question);
        reasons_rv = view.findViewById(R.id.options_rv);
        progress_bar = view.findViewById(R.id.progress_bar);
    }
}
