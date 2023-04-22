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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.OptionsAdapter;
import com.bid.app.model.response.Questionnaire;
import com.bid.app.ui.activity.DashboardActivity;

import java.util.ArrayList;
import java.util.Objects;

public class ReasonForTestBottomSheetDialog extends BaseFullScreenBottomSheetDialog {

    public static final String TAG = ReasonForTestBottomSheetDialog.class.getSimpleName();
    private static ReasonForTestBottomSheetDialog reasonForTestBottomSheetDialogFragment;
    private ArrayList<String> reasons = new ArrayList<>();
    private OptionsAdapter optionsAdapter;
    private RecyclerView reasons_rv;
    private ConstraintLayout back_cl;
    private ConstraintLayout cross_cl;
    private TextView next_question;
    private Questionnaire questionnaire;
    public static final String Physician_Referral = "Physician Referral";
    public static final String Workplace_Employment = "Workplace/Employment";
    private static final String Travel = "Travel";
    public static final String Exposed_to_Covid_19 = "Exposed to Covid-19";
    public static final String Showing_sign_of_symptoms = "Showing sign of symptoms";

    public static ReasonForTestBottomSheetDialog newInstance(Questionnaire questionnaire) {
        Bundle args = new Bundle();
        reasonForTestBottomSheetDialogFragment = new ReasonForTestBottomSheetDialog();
        reasonForTestBottomSheetDialogFragment.setArguments(args);
        reasonForTestBottomSheetDialogFragment.questionnaire = questionnaire;
        return reasonForTestBottomSheetDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.book_a_test_questionnaire, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initIds(view);
        initFragment();
    }

    private void initFragment() {
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
                    Toast.makeText(DashboardActivity.context, "Please select any one valid reason", Toast.LENGTH_LONG).show();
                } else {

                    Questionnaire questionnaire = new Questionnaire();
                    questionnaire.setReasonForTest(reasons.get(optionsAdapter.getSelectedPosition()));
                    Log.d(TAG, "Setting ReasonForTest:" + questionnaire.getReasonForTest());

                    if (questionnaire.getReasonForTest().equalsIgnoreCase(Physician_Referral) || questionnaire.getReasonForTest().equalsIgnoreCase(Workplace_Employment)) {
                        NameOfBottomSheetDialog nameOfBottomSheetDialog = NameOfBottomSheetDialog.newInstance(questionnaire);
                        nameOfBottomSheetDialog.show(requireActivity().getSupportFragmentManager(), NameOfBottomSheetDialog.TAG);
                    } else if (questionnaire.getReasonForTest().equalsIgnoreCase(Travel)) {

                        TravelBottomSheetDialog travelBottomSheetDialog = TravelBottomSheetDialog.newInstance(questionnaire);
                        travelBottomSheetDialog.show(requireActivity().getSupportFragmentManager(), TravelBottomSheetDialog.TAG);
                    } else if (questionnaire.getReasonForTest().equalsIgnoreCase(Exposed_to_Covid_19)) {

                        DateOfExposureBottomSheetDialog dateOfExposureBottomSheetDialog = DateOfExposureBottomSheetDialog.newInstance(questionnaire);
                        dateOfExposureBottomSheetDialog.show(requireActivity().getSupportFragmentManager(), DateOfExposureBottomSheetDialog.TAG);
                    } else if (questionnaire.getReasonForTest().equalsIgnoreCase(Showing_sign_of_symptoms)) {

                        DateOfExposureBottomSheetDialog dateOfExposureBottomSheetDialog = DateOfExposureBottomSheetDialog.newInstance(questionnaire);
                        dateOfExposureBottomSheetDialog.show(requireActivity().getSupportFragmentManager(), DateOfExposureBottomSheetDialog.TAG);
                    }
                }
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        reasons.add(Physician_Referral);
        reasons.add(Workplace_Employment);
        reasons.add(Travel);
        reasons.add(Exposed_to_Covid_19);
        reasons.add(Showing_sign_of_symptoms);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.context);
        Objects.requireNonNull(reasons_rv).setLayoutManager(layoutManager);
        optionsAdapter = new OptionsAdapter(reasons, position -> {
            optionsAdapter.notifyDataSetChanged();
        });
        reasons_rv.setAdapter(optionsAdapter);
    }

    private void initIds(View view) {
        back_cl = view.findViewById(R.id.back_cl);
        cross_cl = view.findViewById(R.id.cross_cl);
        next_question = view.findViewById(R.id.next_question);
        reasons_rv = view.findViewById(R.id.options_rv);
    }
}
