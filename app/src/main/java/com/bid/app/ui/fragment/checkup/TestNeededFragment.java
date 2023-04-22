package com.bid.app.ui.fragment.checkup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.CheckUpTestCaseAdapter;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.CheckupTest;
import com.bid.app.model.response.Questionnaire;
import com.bid.app.model.view.CheckUpTestCase;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.activity.ScheduleTestActivity;
import com.bid.app.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class TestNeededFragment extends BaseFragment {

    private static final String TAG = TestNeededFragment.class.getSimpleName();

    
    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book_test, container, false);

        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_check_up), R.drawable.ic_back_arrow, false);

        initController(view);

        return view;
    }

    private void initController(View view) {

        final AppCompatButton btnBookTest = view.findViewById(R.id.btn_book_test);

        recyclerView = view.findViewById(R.id.recycler_view);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DashboardActivity.context);
        recyclerView.setLayoutManager(linearLayoutManager);

        btnBookTest.setOnClickListener(this);

        getList();
    }

    private void initAdapter(final List<CheckUpTestCase> list) {
        final CheckUpTestCaseAdapter checkUpTestCaseAdapter = new CheckUpTestCaseAdapter(requireActivity(), list, TestNeededFragment.this);
        recyclerView.setAdapter(checkUpTestCaseAdapter);
        checkUpTestCaseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.btn_book_test:

                Bundle bundle=getArguments();
                CheckupTest checkupTest= (CheckupTest) bundle.getSerializable("Checkup Test");

                Questionnaire questionnaire=new Questionnaire();
                ArrayList<Integer>symptomsIds = new ArrayList<>();
                for (String id:checkupTest.getSymptomsIds()) symptomsIds.add( Integer.parseInt(id));
                questionnaire.setSymptoms(symptomsIds);
                boolean covidContact = checkupTest.getCovidContact().equalsIgnoreCase("1") ;
                questionnaire.setContact(covidContact);

                AppActivityManager.redirectWithSerializedQuestionnaireObject((AppCompatActivity)requireActivity() , ScheduleTestActivity.class,false,false,null,questionnaire,null);
                //AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), , false, false, false, null);
                break;
        }
    }

    private void getList() {

        final List<CheckUpTestCase> checkUpTestCases = new ArrayList<>();

        final CheckUpTestCase upTestCase1 = new CheckUpTestCase(1, getResources().getString(R.string.follow_doctor_orders),getString(R.string.test_need_description_1));
        final CheckUpTestCase upTestCase2 = new CheckUpTestCase(2, getString(R.string.test_needed_title_2), getString(R.string.test_need_description_2));
        final CheckUpTestCase upTestCase3 = new CheckUpTestCase(3, getString(R.string.test_needed_title_3), getString(R.string.test_need_description_3));
        final CheckUpTestCase upTestCase4 = new CheckUpTestCase(4, getString(R.string.test_needed_title_4), getString(R.string.test_need_description_4));
        final CheckUpTestCase upTestCase5 = new CheckUpTestCase(5, getString(R.string.test_needed_title_5), getString(R.string.test_need_description_5));


        checkUpTestCases.add(upTestCase1);
        checkUpTestCases.add(upTestCase2);
        checkUpTestCases.add(upTestCase3);
        checkUpTestCases.add(upTestCase4);
        checkUpTestCases.add(upTestCase5);

        initAdapter(checkUpTestCases);
    }
}
