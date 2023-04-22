package com.bid.app.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.TransactionHistoryAdapter;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.GetTicketDetailResponse;
import com.bid.app.model.response.TransactionsHistory;
import com.bid.app.model.response.TransactionsResponse;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Ticket;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.tripHistory.TicketDetailFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionHistoryFragment extends BaseFragment {

    private static final String TAG = TransactionHistoryFragment.class.getSimpleName();
    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private RecyclerView recyclerView;
    private TextView tvNoData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_transaction_history), R.drawable.back_arrow_icon, false);

        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        initController(view);

        return view;
    }

    private void initController(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        tvNoData = view.findViewById(R.id.tvNoData);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.context);
        recyclerView.setLayoutManager(layoutManager);

        getTransactionHistory();

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {


        }
    }


    private void getTransactionHistory() {
        Logger.e(TAG, "getAccessToken " + sessionManager.getAccessToken());
        CustomProgressDialog.getInstance().showDialog(getContext(), true);

        Call<TransactionsResponse> listResponseCall = apiInterface.transactions(sessionManager.getAccessToken(), "");
        listResponseCall.enqueue(new Callback<TransactionsResponse>() {
            @Override
            public void onResponse(Call<TransactionsResponse> call, Response<TransactionsResponse> response) {
               CustomProgressDialog.getInstance().dismissDialog();
                final TransactionsResponse listResponse = response.body();
                if (listResponse != null) {
                    final ErrorStatus status = listResponse.getErrorStatus();

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        if (listResponse.getTransactionsInfos() != null && listResponse.getTransactionsInfos().size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            tvNoData.setVisibility(View.GONE);
                            initAdapter(listResponse.getTransactionsInfos());
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);
                        }
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                        AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<TransactionsResponse> call, Throwable t) {
                t.printStackTrace();
                 CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }


    private void initAdapter(List<TransactionsHistory> transactionsInfos) {
        final TransactionHistoryAdapter aboutAdapter = new TransactionHistoryAdapter(requireActivity(), transactionsInfos, TransactionHistoryFragment.this);
        recyclerView.setAdapter(aboutAdapter);
        aboutAdapter.notifyDataSetChanged();
    }


    @Override
    public void clickTransactionHistory(int position, TransactionsHistory transactionHistory) {
        if(transactionHistory.getTicket_id().equals("0")) return;
        CustomProgressDialog.getInstance().showDialog(getContext(), true);
        Call<GetTicketDetailResponse> listResponseCall = apiInterface.getTicketDetail(sessionManager.getAccessToken(), transactionHistory.getTicket_id());
        listResponseCall.enqueue(new Callback<GetTicketDetailResponse>() {
            @Override
            public void onResponse(Call<GetTicketDetailResponse> call, Response<GetTicketDetailResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                GetTicketDetailResponse getTicketResponse = response.body();
                if(getTicketResponse == null) return;
                Ticket ticketData = getTicketResponse.getTicket();
                Bundle bundle=new Bundle();
                bundle.putSerializable(IBundle.BUNDLE_TICKET , ticketData);
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TicketDetailFragment(), R.id.frame_layout, true, false, true, bundle);
            }

            @Override
            public void onFailure(Call<GetTicketDetailResponse> call, Throwable t) {
                CustomProgressDialog.getInstance().dismissDialog();
                t.printStackTrace();
                //            CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }
}