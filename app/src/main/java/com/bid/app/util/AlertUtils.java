package com.bid.app.util;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.bid.app.R;
import com.tapadoo.alerter.Alerter;

public class AlertUtils {


    public static void showAlerterSuccess(AppCompatActivity appCompatActivity, final String message) {
        Alerter.create(appCompatActivity)
                .setText(message)
                .setIcon(R.drawable.ic_tick_round_white)
                .setBackgroundColor(R.color.colorGreen)
                .show();
    }

    public static void showAlerterWarning(AppCompatActivity appCompatActivity, final String message) {
        Alerter.create(appCompatActivity)
                .setTitle(R.string.app_name)
                .setText(message)
                .setIcon(R.drawable.ic_warning_yellow)
                .setBackgroundColor(R.color.colorYellow)
                .show();
    }

    public static void showAlerterFailure(AppCompatActivity appCompatActivity, final String message) {
        Alerter.create(appCompatActivity)
                .setText(message)
                .setIcon(R.drawable.ic_close_round_red)
                .setBackgroundColor(R.color.colorRed)
                .show();
    }

    public static void showAlerterSuccess(FragmentActivity fragmentActivity, final String message) {
        Alerter.create(fragmentActivity)
                .setText(message)
                .setIcon(R.drawable.ic_tick_round_white)
                .setBackgroundColor(R.color.colorGreen)
                .show();
    }

    public static void showAlerterWarning(FragmentActivity fragmentActivity, final String message) {
        Alerter.create(fragmentActivity)
                .setTitle(R.string.app_name)
                .setText(message)
                .setIcon(R.drawable.ic_warning_yellow)
                .setBackgroundColor(R.color.colorYellow)
                .show();
    }

    public static void showAlerterFailure(FragmentActivity fragmentActivity, final String message) {
        if (fragmentActivity != null) {
            Alerter.create(fragmentActivity)
                    .setText(message)
                    .setIcon(R.drawable.ic_close_round_red)
                    .setBackgroundColor(R.color.colorRed)
                    .show();
        }
    }

}
