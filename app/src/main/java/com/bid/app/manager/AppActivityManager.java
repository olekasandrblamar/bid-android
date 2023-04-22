package com.bid.app.manager;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bid.app.R;
import com.bid.app.model.response.CardListInfo;
import com.bid.app.model.response.Questionnaire;

public class AppActivityManager {

    private static String TAG = AppActivityManager.class.getSimpleName();

    public static void redirectTo(AppCompatActivity mActivity, Class<? extends AppCompatActivity> aClass, final Boolean isFinishActivity, final Boolean animation, final Boolean isBundleAdded, Bundle bundle) {
        Intent intent = new Intent(mActivity, aClass);
        if (isBundleAdded) {
            intent.putExtras(bundle);
        }
        if (animation) {
            mActivity.overridePendingTransition(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left);
        }
        mActivity.startActivity(intent);
        if (isFinishActivity) {
            mActivity.finish();
        }
    }

    public static void redirectWithSerializedQuestionnaireObject(AppCompatActivity mActivity, Class<? extends AppCompatActivity> aClass, final Boolean isFinishActivity, final Boolean animation, Bundle bundle, Questionnaire questionnaire, CardListInfo cardListInfo) {
        Intent intent = new Intent(mActivity, aClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (questionnaire != null) {
            intent.putExtra("questionnaire", questionnaire);
        }
        if (cardListInfo != null) {
            intent.putExtra("cardListInfo", cardListInfo);
        }
        if (animation) {
            mActivity.overridePendingTransition(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left);
        }
        mActivity.startActivity(intent);
        if (isFinishActivity) {
            mActivity.finish();
        }
    }

    public static void redirectTo(final AppCompatActivity mActivity, final Fragment fragment, final int container, final Boolean needBackStack, final Boolean animation, final Boolean needBundle, final Bundle bundle) {
        Fragment redirectFragment = fragment;
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();

        if (animation) {
            transaction.setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left, R.anim.anim_enter_from_left, R.anim.anim_exit_to_right);
        }

        transaction.replace(container, redirectFragment);

        if (needBackStack) {
            transaction.addToBackStack(null);
        }

        if (needBundle) {
            redirectFragment.setArguments(bundle);
        }

        transaction.commit();
    }
    public static void popStack(final AppCompatActivity mActivity, int cnt) {
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        for(int i = 0; i < cnt; i++) {
            mActivity.getSupportFragmentManager().popBackStack();
        }
    }
}
