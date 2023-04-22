package com.bid.app.ui.fragment.checkup;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.DailyTestListAdapter;
import com.bid.app.constants.Global;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.CheckupTest;
import com.bid.app.model.response.DailyTestListResponse;
import com.bid.app.model.view.DailyTest;
import com.bid.app.model.view.DailyTestSymptoms;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Symptoms;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyCheckupFragment extends BaseFragment {

    private static final String TAG = DailyCheckupFragment.class.getSimpleName();

    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private RecyclerView recyclerView;
    private List<DailyTest> dailyTests;

    private List<List<String>> testSyptomsIds;


    private CardView isolationCardView;
    private TextView isolationTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialy_check_up, container, false);

        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_check_up), R.drawable.ic_back_arrow, false);
        setupViews(view);
        initController(view);

        return view;
    }

    private void setupViews(View view) {
        ImageView imageView = view.findViewById(R.id.iv_isolate_daily_checkup);
        imageView.setColorFilter(Color.RED);


        isolationCardView = view.findViewById(R.id.card_view_isolate_daily_checkup);
        isolationTextView = view.findViewById(R.id.tv_date_daily_checkup);
    }

    private void initController(View view) {

        final AppCompatTextView tvStart = view.findViewById(R.id.tv_start_daily_checkup);

        recyclerView = view.findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.context);
        recyclerView.setLayoutManager(layoutManager);

        tvStart.setOnClickListener(this);

        callDailyTestListApi();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.tv_start_daily_checkup:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new SymptomsListFragment(), R.id.frame_layout, true, false, false, null);
                break;
        }
    }

    private void callDailyTestListApi() {

        Logger.e(TAG, sessionManager.getAccessToken() );

        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);

        final Call<DailyTestListResponse> call = apiInterface.getDailyTestList(sessionManager.getAccessToken());
        call.enqueue(new Callback<DailyTestListResponse>() {
            @Override
            public void onResponse(Call<DailyTestListResponse> call, Response<DailyTestListResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                try {
                    if (response.body() != null) {
                        final DailyTestListResponse body = response.body();
                        final ErrorStatus status = body.getErrorStatus();

                        final List<Symptoms> list = new ArrayList<>();
                        testSyptomsIds = new ArrayList<>();

                        if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                            dailyTests = body.getDailyTestList();

                            for (int i = 0; i < dailyTests.size(); i++) {
                                List<DailyTestSymptoms> dailyTestSymptoms = dailyTests.get(i).getSymptoms();
                                int levelSum = 0;
                                if (dailyTestSymptoms != null) {
                                    final Symptoms history = new Symptoms();
                                    final StringBuilder stringBuilder = new StringBuilder();
                                    List<String> symptomsIds = new ArrayList<>();

                                    for (int j = 0; j < dailyTestSymptoms.size(); j++) {
                                        Symptoms symptoms = dailyTestSymptoms.get(j).getSymptoms();
                                        symptomsIds.add(symptoms.getId());
                                        if (symptoms != null) {
                                            stringBuilder.append(symptoms.getName() + ", ");
                                            history.setId(symptoms.getId());
                                            history.setName(stringBuilder.toString());
                                            int currentLevel = Integer.parseInt(symptoms.getLevel());
                                            levelSum += currentLevel;
                                            history.setLevel(String.valueOf(levelSum));
                                            String statusName;
                                            if (levelSum >= 5) {
                                                statusName = "Check up on - ";
                                                isolationCardView.setVisibility(View.VISIBLE);
                                                String isolationDate=dailyTests.get(i).getUpdated_at();
                                                isolationTextView.setText(isolationDate);
                                            } else {
                                                statusName = "No test needed - ";
                                            }


                                            history.setCreated_at(statusName + dailyTests.get(i).getCreated_at());
                                        } else {
                                            history.setCreated_at("No test needed " + dailyTests.get(i).getCreated_at());
                                        }

                                    }
                                    testSyptomsIds.add(symptomsIds);
                                    list.add(history);
                                }
                            }
                            initAdapter(list);
                        } else {
                            AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                        }
                    } else {
                        Logger.e(TAG, "response.body() == null");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DailyTestListResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }

    private void initAdapter(final List<Symptoms> list) {
        DailyTestListAdapter symptomsAdapter = new DailyTestListAdapter(requireActivity(), list, DailyCheckupFragment.this);
        recyclerView.setAdapter(symptomsAdapter);
        symptomsAdapter.notifyDataSetChanged();
    }


    @Override
    public void clickOnDailyTest(int position, Symptoms symptoms) {
        super.clickOnDailyTest(position, symptoms);
        List<String> symptomsIds = testSyptomsIds.get(position);
        String result = dailyTests.get(position).getResult();
        String covidContact = dailyTests.get(position).getCovid_contact();
        if (Integer.parseInt(symptoms.getLevel()) >= 5) {
            Bundle bundle = new Bundle();
            CheckupTest checkupTest = new CheckupTest();
            checkupTest.setSymptomsIds(symptomsIds);
            checkupTest.setResult(result);
            checkupTest.setCovidContact(covidContact);
            bundle.putSerializable("Checkup Test", checkupTest);
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TestNeededFragment(), R.id.frame_layout, true, false, true, bundle);
        } else {
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new NoTestNeededFragment(), R.id.frame_layout, true, false, false, null);
        }


    }
}