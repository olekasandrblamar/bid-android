package com.bid.app.ui.fragment.checkup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.bid.app.R;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.CheckupTest;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;

public class QuestionsSetOneFragment extends BaseFragment {

    private static final String TAG = QuestionsSetOneFragment.class.getSimpleName();

    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private CardView cardViewOne;
    private CardView cardViewTwo;
    private CardView cardViewThree;


    private RadioButton radioButtonYes;
    private RadioButton radioButtonNo;
    private RadioButton radioButtonNotSure;

    private CheckupTest checkupTest;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_questions_set_one, container, false);

        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        checkupTest= (CheckupTest) getArguments().getSerializable("Checkup Test");

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_symptoms), R.drawable.ic_back_arrow, false);

        initController(view);

        return view;
    }

    private void initController(View view) {

        final AppCompatButton btnNext = view.findViewById(R.id.btn_submit_set_one);

        cardViewOne = view.findViewById(R.id.card_view_positive_question_result_two);
        cardViewTwo = view.findViewById(R.id.card_view_negative_question_result_two);
        cardViewThree = view.findViewById(R.id.card_view_no_question_result_two);

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

            case R.id.btn_submit_set_one:
                String covidContact = radioButtonYes.isChecked() ? "1" : "0";
                checkupTest.setCovidContact(covidContact);
                Bundle bundle=new Bundle();
                bundle.putSerializable("Checkup Test",checkupTest);
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new QuestionsSetTwoFragment(), R.id.frame_layout, true, false, true, bundle);
                break;

        }
    }


    private void selectOption(final boolean oneChecked, final boolean twoChecked, final boolean threeChecked) {
        radioButtonYes.setChecked(oneChecked);
        radioButtonNo.setChecked(twoChecked);
        radioButtonNotSure.setChecked(threeChecked);
    }
}