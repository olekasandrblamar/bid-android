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

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class SignUpFragment extends BaseFragment {

    private static final String TAG = SignUpFragment.class.getSimpleName();
    private AppCompatEditText etNumber;
    private AppCompatTextView tvText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_mobile_number, container, false);

        initToolBar(view);
        initController(view);

        return view;
    }

    private void initToolBar(View view) {

        final ImageView ivBack = view.findViewById(R.id.iv_back_tool_bar);
        final AppCompatTextView tvTitle = view.findViewById(R.id.tv_title_tool_bar);
        final ImageView iv_right_tool_bar = view.findViewById(R.id.iv_right_tool_bar);
        iv_right_tool_bar.setVisibility(View.GONE);

        tvTitle.setText(getResources().getString(R.string.title_b_id));

        ivBack.setOnClickListener(this);

    }

    private void initController(View view) {

        tvText = view.findViewById(R.id.tv_text_mobile_number);
        etNumber = view.findViewById(R.id.et_mobile_number);
        final AppCompatButton btnNext = view.findViewById(R.id.btn_next_mobile_number);
        tvText.setText("We'll send a verification code to \n this number");
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.iv_back_tool_bar:
                Objects.requireNonNull(getFragmentManager()).popBackStack();
                break;

            case R.id.btn_next_mobile_number:
                validateFields();
                break;
        }
    }

    private void validateFields() {

        if (StringUtils.isEmpty(Objects.requireNonNull(etNumber.getText()).toString())) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter mobile number!");
//        } else if (etNumber.getText().toString().length() < 7) {
//            AlertUtils.showAlerterWarning(requireActivity(), "Please enter valid mobile number!");
        } else {
            final Bundle bundle = new Bundle();
            bundle.putString(IBundle.BUNDLE_MOBILE_NUMBER, etNumber.getText().toString());
            bundle.putString(IBundle.BUNDLE_FRAGMENT_FROM, "MOBILE_NUMBER_FRAGMENT");
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new VerifyOTPFragment(), R.id.frame_layout, true, false, true, bundle);
        }
    }
}
