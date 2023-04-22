package com.bid.app.ui.fragment.onboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.bid.app.R;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Utils;

import org.apache.commons.lang3.StringUtils;

public class AddEmailFragment extends BaseFragment {

    private static final String TAG = AddEmailFragment.class.getSimpleName();

    private AppCompatEditText etEmail;

    private AppCompatButton btnSendCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_add_emaill_address, container, false);

        initToolBar(view);
        initController(view);

        return view;
    }

    private void initToolBar(View view) {

        final ImageView ivBack = view.findViewById(R.id.iv_back_tool_bar);
        final AppCompatTextView tvTitle = view.findViewById(R.id.tv_title_tool_bar);
        final ImageView iv_right_tool_bar = view.findViewById(R.id.iv_right_tool_bar);
        iv_right_tool_bar.setVisibility(View.GONE);

        tvTitle.setText(getResources().getString(R.string.title_add_email_address));

        ivBack.setOnClickListener(this);

    }

    private void initController(View view) {

        etEmail = view.findViewById(R.id.et_email_add_email);
        btnSendCode = view.findViewById(R.id.btn_send_code_add_email);

        btnSendCode.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.iv_back_tool_bar:
                getFragmentManager().popBackStack();
                break;

            case R.id.btn_send_code_add_email:
                validateEmail();
                break;
        }
    }

    private void validateEmail() {

        if (StringUtils.isEmpty(etEmail.getText().toString())) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter your email address!");
        } else if (!Utils.isValidEmail(etEmail.getText().toString())) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter valid email address!");
        } else {
            final Bundle bundle = new Bundle();
            bundle.putString(IBundle.BUNDLE_EMAIL, etEmail.getText().toString());
            bundle.putString(IBundle.BUNDLE_FRAGMENT_FROM, "EMAIL_FRAGMENT");
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new VerifyOTPFragment(), R.id.frame_layout, true, false, true, bundle);
        }
    }
}