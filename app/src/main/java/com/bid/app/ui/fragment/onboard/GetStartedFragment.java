package com.bid.app.ui.fragment.onboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;

public class GetStartedFragment extends BaseFragment {

    private static final String TAG = GetStartedFragment.class.getSimpleName();

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_get_started, container, false);

        initController(view);

        SessionManager sessionManager = new SessionManager(requireActivity());
        sessionManager.clearSession();

        return view;
    }

    private void initController(View view) {

        final AppCompatTextView tvLogin = view.findViewById(R.id.tv_have_an_account_get_started);

        final AppCompatButton btnSignUp = view.findViewById(R.id.btn_sign_in_get_started);

        tvLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.tv_have_an_account_get_started:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new LoginFragment(), R.id.frame_layout, true, false, false, null);
                break;

            case R.id.btn_sign_in_get_started:
                //AddEmailFragment
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new CountryListFragment(GetStartedFragment.this), R.id.frame_layout, true, false, false, null);
                break;
        }
    }
}
