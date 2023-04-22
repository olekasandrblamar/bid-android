package com.bid.app.ui.fragment.notification;

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
import com.bid.app.adaper.NotificationSettingsAdapter;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.NotificationListInfo;
import com.bid.app.model.response.NotificationsResultResponse;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Settings;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.account.AccountSettingsFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends BaseFragment {

    private static final String TAG = NotificationsFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private SessionManager sessionManager;
    private APIInterface apiInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification_settings, container, false);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_notifications), R.drawable.ic_back_arrow, false);

        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

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

    private void initAdapter(final List<NotificationListInfo> list) {
        final NotificationSettingsAdapter settingsAdapter = new NotificationSettingsAdapter(requireActivity(), list, NotificationsFragment.this);
        recyclerView.setAdapter(settingsAdapter);
        settingsAdapter.notifyDataSetChanged();
    }

    private void getSettingsList() {
        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);
        Logger.e(TAG, "getAccessToken " + sessionManager.getAccessToken());

        Call<NotificationsResultResponse> listResponseCall = apiInterface.notifications(sessionManager.getAccessToken());
        listResponseCall.enqueue(new Callback<NotificationsResultResponse>() {
            @Override
            public void onResponse(Call<NotificationsResultResponse> call, Response<NotificationsResultResponse> response) {
                    CustomProgressDialog.getInstance().dismissDialog();
                final NotificationsResultResponse listResponse = response.body();
                final ErrorStatus status = listResponse.getErrorStatus();

                if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                    List<NotificationListInfo> list = listResponse.getNotificationListInfo();
                    initAdapter(list);
                } else {
                    AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                }
            }

            @Override
            public void onFailure(Call<NotificationsResultResponse> call, Throwable t) {
                t.printStackTrace();
                  CustomProgressDialog.getInstance().dismissDialog();
            }
        });

    }

    @Override
    public void clickOnSettings(int position, Settings setting) {
        super.clickOnSettings(position, setting);

        if (getResources().getString(R.string.hint_notifications).equalsIgnoreCase(setting.getName())) {
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new AccountSettingsFragment(), R.id.frame_layout, true, false, false, null);
        } else {

        }
    }
}