package com.bid.app.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bid.app.R;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.ui.base.BaseActivity;
import com.bid.app.ui.fragment.onboard.GetStartedFragment;
import com.bid.app.ui.fragment.onboard.VerifyPasswordFragment;
import com.bid.app.util.Constants;

public class OnBoardingActivity extends BaseActivity {

    private static final String TAG = OnBoardingActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        if (Constants.VERIFY_OTP_FRAGMENT.equalsIgnoreCase(getFragmentTag())) {
            AppActivityManager.redirectTo(OnBoardingActivity.this, new VerifyPasswordFragment(), R.id.frame_layout, false, false, false, null);
        } else {
            AppActivityManager.redirectTo(OnBoardingActivity.this, new GetStartedFragment(), R.id.frame_layout, false, false, false, null);
        }
    }

    private String getFragmentTag() {
        final Bundle bundle = getIntent().getExtras();
        return bundle != null ? bundle.getString(IBundle.BUNDLE_FRAGMENT_FROM) : "";
    }
}

