package com.bid.app.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.COVIDTestResultAdapter;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.ScheduleCovidResultResponse;
import com.bid.app.model.view.CovidResult;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class COVIDTestResultsFragment extends BaseFragment {

    private static final String TAG = COVIDTestResultsFragment.class.getSimpleName();

    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private RecyclerView recyclerView;

    private View viewNoData;
    private AppCompatTextView tvNoData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_covid_results, container, false);

        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_b_id), R.drawable.ic_back_arrow, false);

        initController(view);

        return view;
    }

    private void initController(View view) {

        recyclerView = view.findViewById(R.id.recycler_view);
        viewNoData = view.findViewById(R.id.view_no_data);
        tvNoData = view.findViewById(R.id.tv_no_data);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.context);
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    public void onResume() {
        super.onResume();
        callCovidTestResultApi();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

        }
    }

    private void initAdapter(final List<CovidResult> list) {
        Activity activity = getActivity();
        if (activity != null) {
            final COVIDTestResultAdapter covidTestResultAdapter = new COVIDTestResultAdapter(DashboardActivity.context, list, COVIDTestResultsFragment.this);
            recyclerView.setAdapter(covidTestResultAdapter);
            covidTestResultAdapter.notifyDataSetChanged();

            if (covidTestResultAdapter.getItemCount() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                viewNoData.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.GONE);
                viewNoData.setVisibility(View.VISIBLE);
            }

        }

    }

    private void callCovidTestResultApi() {
        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);
        Logger.e(TAG, "getAccessToken " + sessionManager.getAccessToken());

        Call<ScheduleCovidResultResponse> listResponseCall = apiInterface.getCovidScheduleList(sessionManager.getAccessToken());
        listResponseCall.enqueue(new Callback<ScheduleCovidResultResponse>() {
            @Override
            public void onResponse(Call<ScheduleCovidResultResponse> call, Response<ScheduleCovidResultResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();

                if (response.body() != null) {
                    Logger.e(TAG, "response.body() " + response.body().toString());
                    final ScheduleCovidResultResponse scheduleCovidResultResponse = response.body();
                    final ErrorStatus status = scheduleCovidResultResponse.getErrorStatus();

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        initAdapter(scheduleCovidResultResponse.getCovidResultList());
                    } else {
                        AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                    }
                } else {
                    Logger.e(TAG, "response.body() null");
                }
            }

            @Override
            public void onFailure(Call<ScheduleCovidResultResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }

    @Override
    public void clickOnResult(int position, CovidResult covidResult) {
        super.clickOnResult(position, covidResult);

        Logger.e(TAG, "covidResult " + covidResult.getReg_date());
        Bundle bundle = new Bundle();
        bundle.putString(IBundle.BUNDLE_TEST_DATE, covidResult.getReg_date());
        bundle.putString(IBundle.BUNDLE_TEST_TIME, covidResult.getUpdated_at());
        bundle.putString(IBundle.BUNDLE_TEST_LOCATION, covidResult.getCenter().getName());
        bundle.putString(IBundle.BUNDLE_TEST_TYPE, covidResult.getTest_type().getName());
        bundle.putString(IBundle.BUNDLE_TEST_STATUS, covidResult.getStatus());
        bundle.putString(IBundle.BUNDLE_TEST_STATUS_NAME,covidResult.getTest_status().getName());
        bundle.putString(IBundle.BUNDLE_TEST_STATUS_COLOR,covidResult.getTest_status().getColor());
        bundle.putString(IBundle.BUNDLE_TEST_STATUS_FONT_COLOR,covidResult.getTest_status().getFont_color());

        AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new COVIDTestDetailReportFragment(), R.id.frame_layout, true, false, true, bundle);
    }
}
