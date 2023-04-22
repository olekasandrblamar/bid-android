package com.bid.app.ui.fragment.home;

import static com.bid.app.ui.activity.SplashActivity.tempContext;
import static com.bid.app.ui.fragment.home.ReasonForTestBottomSheetDialog.Physician_Referral;
import static com.bid.app.ui.fragment.home.ReasonForTestBottomSheetDialog.Workplace_Employment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bid.app.R;
import com.bid.app.model.response.Questionnaire;

import java.util.Objects;

public class NameOfBottomSheetDialog extends BaseFullScreenBottomSheetDialog {

    public static final String TAG = NameOfBottomSheetDialog.class.getSimpleName();
    private static NameOfBottomSheetDialog dateOfExposureBottomSheetDialog;
    private ConstraintLayout back_cl;
    private ConstraintLayout cross_cl;
    private TextView next_question;
    private EditText name_of;
    private String header_text_val;
    private TextView header_text;
    private Questionnaire questionnaire;

    public static NameOfBottomSheetDialog newInstance(Questionnaire questionnaire) {
        Bundle args = new Bundle();
        dateOfExposureBottomSheetDialog = new NameOfBottomSheetDialog();
        dateOfExposureBottomSheetDialog.setArguments(args);
        dateOfExposureBottomSheetDialog.questionnaire = questionnaire;
        return dateOfExposureBottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.name_of, container, false);

        if (questionnaire.getReasonForTest().equalsIgnoreCase(Workplace_Employment)) {
            header_text_val = requireActivity().getResources().getString(R.string.name_of_employer);
        } else if (questionnaire.getReasonForTest().equalsIgnoreCase(Physician_Referral)) {
            header_text_val = requireActivity().getResources().getString(R.string.name_of_referral);
        } else {
            header_text_val = requireActivity().getResources().getString(R.string.name_of_country_island);
        }

        initIds(view);
        initFragment();

        return view;
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
                if (!name_of.getText().toString().isEmpty()) {

                    if (questionnaire.getReasonForTest().equalsIgnoreCase(Physician_Referral)) {
                        questionnaire.setNameOfReferral(name_of.getText().toString());
                        Log.d(TAG, "Setting NameOfReferral:" + name_of.getText().toString());
                    }

                    ExposedToBottomSheetDialog exposedToBottomSheetDialog = ExposedToBottomSheetDialog.newInstance(questionnaire);
                    exposedToBottomSheetDialog.show(requireActivity().getSupportFragmentManager(), ExposedToBottomSheetDialog.TAG);
                } else {
                    Toast.makeText(tempContext, "Please, enter a name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void initIds(View view) {
        back_cl = view.findViewById(R.id.back_cl);
        cross_cl = view.findViewById(R.id.cross_cl);
        next_question = view.findViewById(R.id.next_question);
        name_of = view.findViewById(R.id.name_of);
        header_text = view.findViewById(R.id.header_text);
        header_text.setText(header_text_val.toUpperCase());
    }
}
