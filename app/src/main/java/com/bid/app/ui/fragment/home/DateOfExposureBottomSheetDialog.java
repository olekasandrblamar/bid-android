package com.bid.app.ui.fragment.home;

import static com.bid.app.ui.activity.ScheduleTestActivity.changeDateFormat;
import static com.bid.app.ui.fragment.home.ReasonForTestBottomSheetDialog.Exposed_to_Covid_19;
import static com.bid.app.ui.fragment.home.ReasonForTestBottomSheetDialog.Showing_sign_of_symptoms;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bid.app.R;
import com.bid.app.model.response.Questionnaire;
import com.bid.app.ui.activity.ScheduleTestActivity;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DateOfExposureBottomSheetDialog extends BaseFullScreenBottomSheetDialog {

    public static final String TAG = DateOfExposureBottomSheetDialog.class.getSimpleName();
    private static DateOfExposureBottomSheetDialog dateOfExposureBottomSheetDialog;
    private ConstraintLayout back_cl;
    private ConstraintLayout cross_cl;
    private TextView next_question;
    private CalendarView calendarView;
    private TextView header_text_tv;
    private TextView sub_header_text_tv;
    private Questionnaire questionnaire;
    private String selectedDate;

    public static DateOfExposureBottomSheetDialog newInstance(Questionnaire questionnaire) {
        Bundle args = new Bundle();
        dateOfExposureBottomSheetDialog = new DateOfExposureBottomSheetDialog();
        dateOfExposureBottomSheetDialog.setArguments(args);
        dateOfExposureBottomSheetDialog.questionnaire = questionnaire;
        return dateOfExposureBottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.date_of_exposure, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initIds(view);
        initFragment();
    }

    private void initFragment() {
        if (questionnaire.getReasonForTest().equalsIgnoreCase(Exposed_to_Covid_19)) {
            header_text_tv.setText(getString(R.string.date_of_exposure));
            sub_header_text_tv.setText(getString(R.string.select_exposure_date));
        } else {
            header_text_tv.setText(getString(R.string.system_onset));
            sub_header_text_tv.setText(getString(R.string.selct_onset_date));
        }

        try {
            Calendar min = Calendar.getInstance();
            calendarView.setDate(min);
            Date dates_ = Calendar.getInstance().getTime();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormatLocal_temp = new SimpleDateFormat(ScheduleTestActivity.DATE_FORMAT_T);
            selectedDate = changeDateFormat(ScheduleTestActivity.DATE_FORMAT_T, ScheduleTestActivity.DATE_FORMAT_YYYY_MM_DD, dateFormatLocal_temp.format(dates_));
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        calendarView.setOnDayClickListener(eventDay -> {
            Date dates_ = eventDay.getCalendar().getTime();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormatLocal_temp = new SimpleDateFormat(ScheduleTestActivity.DATE_FORMAT_T);
            selectedDate = changeDateFormat(ScheduleTestActivity.DATE_FORMAT_T, ScheduleTestActivity.DATE_FORMAT_YYYY_MM_DD, dateFormatLocal_temp.format(dates_));
        });

        Objects.requireNonNull(back_cl).setOnClickListener(v -> dismiss());

        Objects.requireNonNull(cross_cl).setOnClickListener(v -> dismiss());

        Objects.requireNonNull(next_question).setOnClickListener(v -> {
            if (questionnaire.getReasonForTest().equalsIgnoreCase(Exposed_to_Covid_19)) {
                ExposedToBottomSheetDialog exposedToBottomSheetDialog = ExposedToBottomSheetDialog.newInstance(questionnaire);
                exposedToBottomSheetDialog.show(requireActivity().getSupportFragmentManager(), ExposedToBottomSheetDialog.TAG);
            } else if (questionnaire.getReasonForTest().equalsIgnoreCase(Showing_sign_of_symptoms)) {
                SymptomsBottomSheetDialog symptomsBottomSheetDialog = SymptomsBottomSheetDialog.newInstance(questionnaire);
                symptomsBottomSheetDialog.show(requireActivity().getSupportFragmentManager(), SymptomsBottomSheetDialog.TAG);
            }
        });
    }


    private void initIds(View view) {
        header_text_tv = view.findViewById(R.id.header_text_tv);
        sub_header_text_tv = view.findViewById(R.id.sub_header_text_tv);
        back_cl = view.findViewById(R.id.back_cl);
        cross_cl = view.findViewById(R.id.cross_cl);
        next_question = view.findViewById(R.id.next_question);
        calendarView = view.findViewById(R.id.calendarView);
    }
}
