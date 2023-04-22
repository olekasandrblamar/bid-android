package com.bid.app.ui.fragment.checkup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.CheckUpTestCaseAdapter;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.model.view.CheckUpTestCase;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class NoTestNeededFragment extends BaseFragment {

    private static final String TAG = NoTestNeededFragment.class.getSimpleName();

    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_no_test, container, false);

        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_check_up), R.drawable.ic_back_arrow, false);

        initController(view);

        return view;
    }

    private void initController(View view) {

        recyclerView = view.findViewById(R.id.recycler_view);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DashboardActivity.context);
        recyclerView.setLayoutManager(linearLayoutManager);

        getList();
    }

    private void initAdapter(final List<CheckUpTestCase> list) {
        final CheckUpTestCaseAdapter checkUpTestCaseAdapter = new CheckUpTestCaseAdapter(requireActivity(), list, NoTestNeededFragment.this);
        recyclerView.setAdapter(checkUpTestCaseAdapter);
        checkUpTestCaseAdapter.notifyDataSetChanged();
    }

    private void getList() {

        final List<CheckUpTestCase> checkUpTestCases = new ArrayList<>();

        final CheckUpTestCase upTestCase1 = new CheckUpTestCase(1, getResources().getString(R.string.no_test_at_this_time), getResources().getString(R.string.no_test_at_this_time_desc));


        checkUpTestCases.add(upTestCase1);

        initAdapter(checkUpTestCases);
    }
}
