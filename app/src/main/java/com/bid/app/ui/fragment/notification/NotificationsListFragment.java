package com.bid.app.ui.fragment.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.NotificationsListAdapter;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.request.AcceptRequestForFamilyMemberRequest;
import com.bid.app.model.response.GetAvailableSeatResponse;
import com.bid.app.model.response.NotificationListInfo;
import com.bid.app.model.response.NotificationsResultResponse;
import com.bid.app.model.response.StatusResponse;
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

public class NotificationsListFragment extends BaseFragment {

    private static final String TAG = NotificationsListFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private ConstraintLayout constraintLayoutNoData;
    List<NotificationListInfo> notificationListInfos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications_list, container, false);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_notifications), R.drawable.ic_back_arrow, false);
        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        initController(view);

        return view;
    }

    private void initController(View view) {

        constraintLayoutNoData = view.findViewById(R.id.constraint_layout_no_data_notification);

        recyclerView = view.findViewById(R.id.recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.context);
        recyclerView.setLayoutManager(layoutManager);

        getNotificationsList();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {


        }
    }

    private void initAdapter(final List<NotificationListInfo> list) {
        final NotificationsListAdapter notificationsListAdapter = new NotificationsListAdapter(requireActivity(), list, NotificationsListFragment.this);
        recyclerView.setAdapter(notificationsListAdapter);
        notificationsListAdapter.notifyDataSetChanged();

        if (notificationsListAdapter.getItemCount() > 0) {
            constraintLayoutNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            constraintLayoutNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

    private void getNotificationsList() {
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
                    notificationListInfos = listResponse.getNotificationListInfo();
                    initAdapter(notificationListInfos);
                } else {
                    constraintLayoutNoData.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
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


    @Override
    public void clickOnNotificationsSettings(int position, NotificationListInfo setting) {
        AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new AccountSettingsFragment(), R.id.frame_layout, true, false, false, null);
    }

    @Override
    public void clickApprove(int position, Boolean decision) {
        super.clickApprove(position, decision);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        CustomProgressDialog.getInstance().showDialog(getContext(), true);
        AcceptRequestForFamilyMemberRequest acceptRequestForFamilyMemberRequest = new AcceptRequestForFamilyMemberRequest();
        if(decision) acceptRequestForFamilyMemberRequest.setApprove("1");
        else acceptRequestForFamilyMemberRequest.setApprove("2");
        acceptRequestForFamilyMemberRequest.setNotificationId(notificationListInfos.get(position).getId());
        Call<StatusResponse> listResponseCall = apiInterface.acceptRequestForFamilyMember(sessionManager.getAccessToken(), acceptRequestForFamilyMemberRequest);
        listResponseCall.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                StatusResponse statusResponse = response.body();
                if(statusResponse.getError().getCode().equals("0")) {
                    AlertUtils.showAlerterSuccess(requireActivity(),statusResponse.getError().getMessage());
                    getNotificationsList();
                }
                else{
                    AlertUtils.showAlerterWarning(requireActivity(),statusResponse.getError().getMessage());
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });

    }
}