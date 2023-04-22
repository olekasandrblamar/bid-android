package com.bid.app.ui.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.CardsHorizontalAdapter;
import com.bid.app.adaper.TransactionHistoryAdapter;
import com.bid.app.constants.Global;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.CardListInfo;
import com.bid.app.model.response.CardsListResponse;
import com.bid.app.model.response.GetTicketDetailResponse;
import com.bid.app.model.response.GetWalletBalanceResponse;
import com.bid.app.model.response.TransactionsHistory;
import com.bid.app.model.response.TransactionsResponse;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Ticket;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.ScannerFragment;
import com.bid.app.ui.fragment.TransactionHistoryFragment;
import com.bid.app.ui.fragment.tripHistory.TicketDetailFragment;
import com.bid.app.ui.fragment.wallet.AddCardFragment;
import com.bid.app.ui.fragment.wallet.CardDetailFragment;
import com.bid.app.ui.fragment.wallet.TopUpFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeWalletFragment extends BaseFragment { // ST
    private static final String TAG = HomeWalletFragment.class.getSimpleName();

    //    private ImageView ivScan;
    private BottomAppBar bottomAppBar;
    private LinearLayout wallet_top_up;
    private LinearLayout wallet_send_to;
    private LinearLayout wallet_add_card;
    private LinearLayout wallet_exchange;
    private LinearLayout transaction_history;
    private RecyclerView recyclerView;
    private TextView tvNoData;
    SessionManager sessionManager;
    APIInterface apiInterface;

    private TextView amount;
    private RecyclerView card_list;
    List<CardListInfo> cardListInfoList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_home, container, false);
        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_wallet), R.drawable.ic_menu, true);
        initController(view);
        run();
        return view;
    }

    private void initController(View view) {
        cardListInfoList = new ArrayList<>();
//        ivScan = view.findViewById(R.id.iv_qr_dashboard);
        bottomAppBar = view.findViewById(R.id.bottom_bar);
        wallet_top_up = view.findViewById(R.id.wallet_top_up);
        wallet_send_to = view.findViewById(R.id.wallet_send_to);
        wallet_add_card = view.findViewById(R.id.wallet_add_card);
        wallet_exchange = view.findViewById(R.id.wallet_exchange);
        transaction_history = view.findViewById(R.id.transaction_history);
        amount = view.findViewById(R.id.amount);
        card_list = view.findViewById(R.id.card_list);
        final LinearLayout ivBID = bottomAppBar.findViewById(R.id.lt_bid_dashboard);
        final LinearLayout ivWallet = bottomAppBar.findViewById(R.id.lt_discover_wallet);
        final LinearLayout ivDiscover = bottomAppBar.findViewById(R.id.lt_discover_dashboard);
        ivBID.setOnClickListener(this);
        ivWallet.setOnClickListener(this);
        ivDiscover.setOnClickListener(this);
//        ivScan.setOnClickListener(this);
        wallet_top_up.setOnClickListener(this);
        wallet_send_to.setOnClickListener(this);
        wallet_add_card.setOnClickListener(this);
        wallet_exchange.setOnClickListener(this);
        transaction_history.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.recycler_view);
        tvNoData = view.findViewById(R.id.tvNoData);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.context);
        recyclerView.setLayoutManager(layoutManager);
        sessionManager = new SessionManager(getContext());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        getTransactionHistory();

        getCards();
        loadWalletInfo();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_qr_dashboard:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new ScannerFragment(), R.id.frame_layout, true, false, false, null);
                break;
            case R.id.lt_bid_dashboard:
                Global.isHealthCheckUp = false;
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new HomeBIDFragment(), R.id.frame_layout, false, false, false, null);
                break;
            case R.id.lt_discover_wallet:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new HomeWalletFragment(), R.id.frame_layout, false, false, false, null);
                break;
            case R.id.lt_discover_dashboard:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new DiscoverPlaceFragment(), R.id.frame_layout, false, false, false, null);
                break;
            case R.id.wallet_top_up:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TopUpFragment(), R.id.frame_layout, true, false, false, null);
                break;
            case R.id.wallet_send_to:
                AlertUtils.showAlerterWarning(requireActivity(),"coming up soon!");
                break;
            case R.id.wallet_add_card:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new AddCardFragment(), R.id.frame_layout, true, false, false, null);
                break;
            case R.id.wallet_exchange:
                AlertUtils.showAlerterWarning(requireActivity(),"coming up soon!");
                break;
            case R.id.transaction_history:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TransactionHistoryFragment(), R.id.frame_layout, true, false, false, null);
                break;
        }
    }
    @Override
    public void clickOnCardListInfo(int position, CardListInfo cardListInfo){
        Bundle bundle = new Bundle();
        bundle.putSerializable(IBundle.BUNDLE_CARD_LIST_INFO, cardListInfo);

        AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new CardDetailFragment(), R.id.frame_layout, true, false, true, bundle);
    }
    public void run() {
        CardsHorizontalAdapter cardsVerticalAdapter = new CardsHorizontalAdapter(getContext(),cardListInfoList, this );
        card_list.setAdapter(cardsVerticalAdapter);
        cardsVerticalAdapter.notifyDataSetChanged();
        card_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

    }
    public void getCards() {

        final Call<CardsListResponse> call = apiInterface.getCards(sessionManager.getAccessToken());
        call.enqueue(new Callback<CardsListResponse>() {
            @Override
            public void onResponse(Call<CardsListResponse> call, Response<CardsListResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                try {
                    if (response.body() != null) {
                        final CardsListResponse cardsListResponse = response.body();
                        final ErrorStatus status = cardsListResponse.getErrorStatus();
                        cardListInfoList = cardsListResponse.getCardListInfo();
                        run();
                    } else {
                        Logger.e(TAG, "response.body() == null");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CardsListResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }

    public void loadWalletInfo() {
        CustomProgressDialog.getInstance().showDialog(getContext(), true);
        sessionManager = new SessionManager(getContext());
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<GetWalletBalanceResponse> listResponseCall = apiInterface.getWalletBalance(sessionManager.getAccessToken());
        listResponseCall.enqueue(new Callback<GetWalletBalanceResponse>() {
            @Override
            public void onResponse(Call<GetWalletBalanceResponse> call, Response<GetWalletBalanceResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                GetWalletBalanceResponse walletBalanceResponse = response.body();
                DecimalFormat moneyFormat = new DecimalFormat("$#,##0.00");
                amount.setText(moneyFormat.format(new Float(walletBalanceResponse.getBalance())));
//                totalTrips.setText(walletBalanceResponse.getTotalTrips());
//                rewards.setText(walletBalanceResponse.getTotalTrips());
            }

            @Override
            public void onFailure(Call<GetWalletBalanceResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });

    }


    private void getTransactionHistory() {
        Logger.e(TAG, "getAccessToken " + sessionManager.getAccessToken());

        Call<TransactionsResponse> listResponseCall = apiInterface.transactions(sessionManager.getAccessToken(), "recent");
        listResponseCall.enqueue(new Callback<TransactionsResponse>() {
            @Override
            public void onResponse(Call<TransactionsResponse> call, Response<TransactionsResponse> response) {
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
            }
        });
    }

    private void initAdapter(List<TransactionsHistory> transactionsInfos) {
        try{
            final TransactionHistoryAdapter aboutAdapter = new TransactionHistoryAdapter(requireActivity(), transactionsInfos, HomeWalletFragment.this);
            recyclerView.setAdapter(aboutAdapter);
            aboutAdapter.notifyDataSetChanged();
        } catch(Exception e){

        }
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
