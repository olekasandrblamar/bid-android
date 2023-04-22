package com.bid.app.ui.fragment.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class ExposedToBottomSheetDialog extends BaseFullScreenBottomSheetDialog {

    public static final String TAG = ExposedToBottomSheetDialog.class.getSimpleName();
    private static ExposedToBottomSheetDialog exposedToBottomSheetDialog;
    private ArrayList<String> answers = new ArrayList<>();
    private OptionsAdapter optionsAdapter;
    private RecyclerView reasons_rv;
    private ConstraintLayout back_cl;
    private ConstraintLayout cross_cl;
    private TextView submit;
    private static final String ANSWER_YES = "Yes";
    private static final String ANSWER_NO = "No";
    private static final String ANSWER_NOT_SURE = "Not Sure";
    private Questionnaire questionnaire;

    public static ExposedToBottomSheetDialog newInstance(Questionnaire questionnaire) {
        Bundle args = new Bundle();
        exposedToBottomSheetDialog = new ExposedToBottomSheetDialog();
        exposedToBottomSheetDialog.setArguments(args);
        exposedToBottomSheetDialog.questionnaire = questionnaire;
        return exposedToBottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.exposed_to, container, false);
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

        Objects.requireNonNull(submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answers.get(optionsAdapter.getSelectedPosition()).equalsIgnoreCase(ANSWER_YES)) {
                    questionnaire.setContact(true);
                    ContactOfKnownCaseBottomSheetDialog symptomsBottomSheetDialog = ContactOfKnownCaseBottomSheetDialog.newInstance(questionnaire);
                    symptomsBottomSheetDialog.show(requireActivity().getSupportFragmentManager(), ContactOfKnownCaseBottomSheetDialog.TAG);
                } else {
                    questionnaire.setContact(false);
                    SymptomsBottomSheetDialog symptomsBottomSheetDialog = SymptomsBottomSheetDialog.newInstance(questionnaire);
                    symptomsBottomSheetDialog.show(requireActivity().getSupportFragmentManager(), SymptomsBottomSheetDialog.TAG);
                }
                Log.d(TAG, "Setting Contact:" + questionnaire.getContact());
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        answers.add(ANSWER_YES);
        answers.add(ANSWER_NO);
        answers.add(ANSWER_NOT_SURE);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.context);
        Objects.requireNonNull(reasons_rv).setLayoutManager(layoutManager);
        optionsAdapter = new OptionsAdapter(answers, position -> {
            optionsAdapter.notifyDataSetChanged();
        });
        optionsAdapter.setSelectedPosition(0);
        reasons_rv.setAdapter(optionsAdapter);
    }

    private void initIds(View view) {
        back_cl = view.findViewById(R.id.back_cl);
        cross_cl = view.findViewById(R.id.cross_cl);
        submit = view.findViewById(R.id.submit);
        reasons_rv = view.findViewById(R.id.options_rv);
    }
}
