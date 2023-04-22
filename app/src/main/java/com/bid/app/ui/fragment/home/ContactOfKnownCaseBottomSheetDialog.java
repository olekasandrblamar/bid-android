package com.bid.app.ui.fragment.home;

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
import com.bid.app.model.response.ContactOfKnownCase;
import com.bid.app.model.response.Questionnaire;
import com.bid.app.ui.activity.DashboardActivity;

import java.util.Objects;

public class ContactOfKnownCaseBottomSheetDialog extends BaseFullScreenBottomSheetDialog {

    public static final String TAG = ContactOfKnownCaseBottomSheetDialog.class.getSimpleName();
    private static ContactOfKnownCaseBottomSheetDialog reasonForTestBottomSheetDialogFragment;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ConstraintLayout back_cl;
    private ConstraintLayout cross_cl;
    private TextView next_question;
    private Questionnaire questionnaire;
    private EditText insta_twitter_et;
    private EditText email_et;
    private EditText mobile_number_et;
    private EditText full_name_et;

    public static ContactOfKnownCaseBottomSheetDialog newInstance(Questionnaire questionnaire) {
        Bundle args = new Bundle();
        reasonForTestBottomSheetDialogFragment = new ContactOfKnownCaseBottomSheetDialog();
        reasonForTestBottomSheetDialogFragment.setArguments(args);
        reasonForTestBottomSheetDialogFragment.questionnaire = questionnaire;
        return reasonForTestBottomSheetDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_of_known_case, container, false);
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
                if (isDataOk()) {
                    ContactOfKnownCase contactOfKnownCase = new ContactOfKnownCase();
                    contactOfKnownCase.setFirstName(full_name_et.getText().toString());
                    contactOfKnownCase.setMobile(mobile_number_et.getText().toString());
                    contactOfKnownCase.setEmail(email_et.getText().toString());
                    contactOfKnownCase.setInstagram(insta_twitter_et.getText().toString());

                    questionnaire.setContactOfKnownCase(contactOfKnownCase);
                    Log.d(TAG, "Setting ContactOfKnownCase:" + contactOfKnownCase.getFirstName());

                    SymptomsBottomSheetDialog symptomsBottomSheetDialog = SymptomsBottomSheetDialog.newInstance(questionnaire);
                    symptomsBottomSheetDialog.show(requireActivity().getSupportFragmentManager(), SymptomsBottomSheetDialog.TAG);
                }
            }
        });
    }

    private boolean isDataOk() {
        if (full_name_et.getText().toString().isEmpty()) {
            Toast.makeText(DashboardActivity.context, "Please enter full name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mobile_number_et.getText().toString().isEmpty()) {
            Toast.makeText(DashboardActivity.context, "Please enter mobile number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (email_et.getText().toString().isEmpty()) {
            Toast.makeText(DashboardActivity.context, "Please enter email id", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!email_et.getText().toString().matches(emailPattern)) {
            Toast.makeText(DashboardActivity.context, "Please enter valid email address", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void initIds(View view) {
        back_cl = view.findViewById(R.id.back_cl);
        cross_cl = view.findViewById(R.id.cross_cl);
        next_question = view.findViewById(R.id.next_question);
        full_name_et = view.findViewById(R.id.full_name_et);
        mobile_number_et = view.findViewById(R.id.mobile_number_et);
        email_et = view.findViewById(R.id.email_et);
        insta_twitter_et = view.findViewById(R.id.insta_twitter_et);
    }
}
