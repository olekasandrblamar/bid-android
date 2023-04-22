package com.bid.app.ui.fragment.menu;

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
import com.bid.app.adaper.SettingsAdapter;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.view.Settings;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.activity.OnBoardingActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.account.AccountSettingsFragment;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends BaseFragment {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_settings), R.drawable.ic_menu, true);

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

    private void initAdapter(final List<Settings> list) {
        final SettingsAdapter settingsAdapter = new SettingsAdapter(requireActivity(), list, SettingsFragment.this);
        recyclerView.setAdapter(settingsAdapter);
        settingsAdapter.notifyDataSetChanged();
    }

    private void getSettingsList() {
        List<Settings> list = new ArrayList<>();

        final Settings settings = new Settings(getResources().getString(R.string.hint_account_setting), R.drawable.ic_account_setting);
        final Settings delete = new Settings(getResources().getString(R.string.hint_delete_account), R.drawable.ic_delete);

        list.add(settings);
        list.add(delete);

        initAdapter(list);
    }

    @Override
    public void clickOnSettings(int position, Settings setting) {
        super.clickOnSettings(position, setting);

        if (getResources().getString(R.string.hint_account_setting).equalsIgnoreCase(setting.getName())) {
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new AccountSettingsFragment(), R.id.frame_layout, true, false, false, null);
        } else {
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), OnBoardingActivity.class, true, false, false, null);

        }
    }
}