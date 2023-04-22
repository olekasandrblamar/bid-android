package com.bid.app.ui.fragment.home;

import android.os.Bundle;
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

public class TravelBottomSheetDialog extends BaseFullScreenBottomSheetDialog {

    public static final String TAG = TravelBottomSheetDialog.class.getSimpleName();
    private static TravelBottomSheetDialog travelBottomSheetDialog;
    private ArrayList<String> reasons = new ArrayList<>();
    private OptionsAdapter optionsAdapter;
    private RecyclerView reasons_rv;
    private ConstraintLayout back_cl;
    private ConstraintLayout cross_cl;
    private TextView next_question;
    public static final String YES = "Yes";
    public static final String NO = "No";
    private Questionnaire questionnaire;

    public static TravelBottomSheetDialog newInstance(Questionnaire questionnaire) {
        Bundle args = new Bundle();
        travelBottomSheetDialog = new TravelBottomSheetDialog();
        travelBottomSheetDialog.setArguments(args);
        travelBottomSheetDialog.questionnaire = questionnaire;
        return travelBottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.travel, container, false);
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
//                    AlertUtils.showAlerterWarning(requireActivity(), "Please select any one valid reason");
                    Toast.makeText(DashboardActivity.context, "Please select at least one", Toast.LENGTH_LONG).show();
                } else {
                    if (reasons.get(optionsAdapter.getSelectedPosition()).equalsIgnoreCase(YES)) {
                        NameOfBottomSheetDialog nameOfBottomSheetDialog = NameOfBottomSheetDialog.newInstance(questionnaire);
                        nameOfBottomSheetDialog.show(requireActivity().getSupportFragmentManager(), NameOfBottomSheetDialog.TAG);
                    } else if (reasons.get(optionsAdapter.getSelectedPosition()).equalsIgnoreCase(NO)) {
                        ExposedToBottomSheetDialog exposedToBottomSheetDialog = ExposedToBottomSheetDialog.newInstance(questionnaire);
                        exposedToBottomSheetDialog.show(requireActivity().getSupportFragmentManager(), ExposedToBottomSheetDialog.TAG);
                    }
                }
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        reasons.add(YES);
        reasons.add(NO);

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
