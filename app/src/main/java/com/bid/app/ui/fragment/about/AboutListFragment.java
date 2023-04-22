package com.bid.app.ui.fragment.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.AboutAdapter;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.view.About;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class AboutListFragment extends BaseFragment {

    private static final String TAG = AboutListFragment.class.getSimpleName();

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about_list, container, false);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_about), R.drawable.ic_menu, false);

        initController(view);

        return view;
    }

    private void initController(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.context);
        recyclerView.setLayoutManager(layoutManager);

        getSettingsList();

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {


        }
    }

    private void initAdapter(final List<About> list) {
        final AboutAdapter aboutAdapter = new AboutAdapter(requireActivity(), list, AboutListFragment.this);
        recyclerView.setAdapter(aboutAdapter);
        aboutAdapter.notifyDataSetChanged();
    }

    private void getSettingsList() {
        List<About> list = new ArrayList<>();

        final About about = new About("About B-ID");
        final About privacyPolicy = new About("Privacy Policy");
        final About termsConditions = new About("Terms and Conditions");
        final About dataSecurity = new About("Data Security");
        final About libraries = new About("Open-Source Libraries");

        list.add(about);
        list.add(privacyPolicy);
        list.add(termsConditions);
        list.add(dataSecurity);
        list.add(libraries);

        initAdapter(list);
    }

    @Override
    public void clickOnAbout(int position, About about) {
        super.clickOnAbout(position, about);

        if ("About B-ID".equalsIgnoreCase(about.getName())) {
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new AboutFragment(), R.id.frame_layout, true, false, false, null);
        } else if ("Privacy Policy".equalsIgnoreCase(about.getName())) {
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new PrivacyPolicyFragment(), R.id.frame_layout, true, false, false, null);
        } else if ("Terms and Conditions".equalsIgnoreCase(about.getName())) {
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TermsAndConditionsFragment(), R.id.frame_layout, true, false, false, null);
        } else if ("Data Security".equalsIgnoreCase(about.getName())) {
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new DataSecurityAboutFragment(), R.id.frame_layout, true, false, false, null);
        } else if ("Open-Source Libraries".equalsIgnoreCase(about.getName())) {
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new OpenSourceFragment(), R.id.frame_layout, true, false, false, null);
        }
    }
}