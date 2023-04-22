package com.bid.app.ui.fragment.checkup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.cardview.widget.CardView;

import com.bid.app.R;
import com.bid.app.constants.Global;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.CheckupTest;
import com.bid.app.model.response.DailyTestResponse;
import com.bid.app.model.response.Questionnaire;
import com.bid.app.model.view.DailyTestRequest;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionsSetTwoFragment extends BaseFragment {

    private static final String TAG = QuestionsSetTwoFragment.class.getSimpleName();

    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private CardView cardViewOne;
    private CardView cardViewTwo;
    private CardView cardViewThree;


    private AppCompatCheckBox radioButtonYes;
    private AppCompatCheckBox radioButtonNo;
    private AppCompatCheckBox radioButtonNotSure;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_questions_set_two, container, false);

        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_symptoms), R.drawable.ic_back_arrow, false);

        initController(view);

        return view;
    }

    private void initController(View view) {

        final AppCompatButton btnNext = view.findViewById(R.id.btn_submit_set_two);

        cardViewOne = view.findViewById(R.id.card_view_positive_question_result_one);
        cardViewTwo = view.findViewById(R.id.card_view_negative_question_result_one);
        cardViewThree = view.findViewById(R.id.card_view_no_question_result_one);

        radioButtonYes = view.findViewById(R.id.cb_positive_question_result);
        radioButtonNo = view.findViewById(R.id.cb_negative_question_result);
        radioButtonNotSure = view.findViewById(R.id.cb_no_question_result);

        cardViewOne.setOnClickListener(this);
        cardViewTwo.setOnClickListener(this);
        cardViewThree.setOnClickListener(this);

        radioButtonYes.setOnClickListener(this);
        radioButtonNo.setOnClickListener(this);
        radioButtonNotSure.setOnClickListener(this);

        btnNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.cb_positive_question_result:
                selectOption(true, false, false);
                break;

            case R.id.cb_negative_question_result:
                selectOption(false, true, false);
                break;

            case R.id.cb_no_question_result:
                selectOption(false, false, true);
                break;


            case R.id.card_view_positive_question_result_two:
                selectOption(true, false, false);
                break;

            case R.id.card_view_negative_question_result_two:
                selectOption(false, true, false);
                break;

            case R.id.card_view_no_question_result_two:
                selectOption(false, false, true);
                break;

            case R.id.btn_submit_set_two:
                uploadDailyCheck();
                break;

        }
    }

    private void uploadDailyCheck() {
        Bundle bundle=getArguments();
        CheckupTest checkupTest= (CheckupTest) bundle.getSerializable("Checkup Test");
        String result = radioButtonYes.isChecked() ? "1" : "0" ;
        checkupTest.setResult(result);
        DailyTestRequest dailyTestRequest = new DailyTestRequest();
        dailyTestRequest.setResult(checkupTest.getResult());
        dailyTestRequest.setSymptoms(checkupTest.getSymptomsIds());
        dailyTestRequest.setCovid_contact(checkupTest.getCovidContact());
        bundle.putSerializable("Checkup Test",checkupTest);



        Call<DailyTestResponse> call = apiInterface.setDailyTest(sessionManager.getAccessToken() , dailyTestRequest);
        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);
        call.enqueue(new Callback<DailyTestResponse>() {
            @Override
            public void onResponse(Call<DailyTestResponse> call, Response<DailyTestResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                if (response.body() !=null){
                    DailyTestResponse body=response.body();
                    ErrorStatus status = body.getStatus();

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())){
                        AlertUtils.showAlerterSuccess(requireActivity(),status.getMessage());
                        if (Global.isCheckUpNeeded) {
                            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TestNeededFragment(), R.id.frame_layout, true, false, true, bundle);
                        } else {
                            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new NoTestNeededFragment(), R.id.frame_layout, true, false, false, null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DailyTestResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }


    private void selectOption(final boolean oneChecked, final boolean twoChecked, final boolean threeChecked) {
        radioButtonYes.setChecked(oneChecked);
        radioButtonNo.setChecked(twoChecked);
        radioButtonNotSure.setChecked(threeChecked);
    }
}