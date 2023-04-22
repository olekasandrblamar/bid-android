package com.bid.app.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bid.app.R;
import com.bid.app.adaper.PaymentGatewayAdapter;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.CardListInfo;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.activity.PaymentCardMethodActivity;
import com.bid.app.ui.base.BaseFragment;

import java.util.List;

public class PaymentGatewayListFragment extends BaseFragment {

    private static final String TAG = PaymentGatewayListFragment.class.getSimpleName();

    private SessionManager sessionManager = null;

    private PaymentGatewayAdapter paymentGatewayAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayout linearLayoutToolBar;
    private TextView tvTitleToolBar;
    private ImageView ivBackToolBar;

    private TextView tvAddress;
    private TextView tvAddAddress;
    private ConstraintLayout addressLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_payment_list, container, false);

        sessionManager = new SessionManager(requireActivity());

        linearLayoutToolBar = view.findViewById(R.id.linear_layout_tool_bar);

        initToolBar(view);
        initController(view);

        callPaymentService();

        return view;
    }

    /*private String getAmount() {
        Bundle bundle = getArguments();
        return bundle != null ? bundle.getString(IBundleConstants.BUNDLE_FUND_AMOUNT_KEY) : "";
    }*/


    private void initToolBar(View view) {
        //tvTitleToolBar = view.findViewById(R.id.tv_title_toolbar);
        ivBackToolBar = view.findViewById(R.id.iv_back_tool_bar);

      //  tvTitleToolBar.setText(getResources().getString(R.string.title_payment_gateway));

        ivBackToolBar.setVisibility(View.VISIBLE);
        ivBackToolBar.setOnClickListener(this);
    }

    private void initController(View view) {

        tvAddAddress = view.findViewById(R.id.tv_add_address_payment_list);
        tvAddress = view.findViewById(R.id.tv_add_text_address_payment_list);
        addressLayout = view.findViewById(R.id.constraint_layout_address_payment_list);

        initRecyclerView(view);

        swipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setColorSchemeColors(Color.MAGENTA, Color.RED, Color.GREEN);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                callPaymentService();
            }
        });

        callPaymentService();

        tvAddAddress.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back_tool_bar:
                requireActivity().getSupportFragmentManager().popBackStack();
                break;

            case R.id.tv_add_address_payment_list:
                /*sessionManager.putBooleanInSession(IPreferenceConstants.PREF_IS_ADD_ADDRESS, true);

                Bundle bundle = new Bundle();
                if ("Change".equalsIgnoreCase(tvAddAddress.getText().toString())) {
                    bundle.putBoolean(IBundleConstants.BUNDLE_IS_CHANGE_ADDRESS, true);
                } else {
                    bundle.putBoolean(IBundleConstants.BUNDLE_IS_CHANGE_ADDRESS, false);
                }
                appActivityManager.redirectTo(AddressActivity.class, false, true, bundle);*/
                break;
        }
    }

    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.context);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initAdapter(final List<CardListInfo> list) {
        paymentGatewayAdapter = new PaymentGatewayAdapter(requireActivity(), list, this);
        recyclerView.setAdapter(paymentGatewayAdapter);
        paymentGatewayAdapter.notifyDataSetChanged();
    }

    private void callPaymentService() {
        swipeRefreshLayout.setRefreshing(true);
      //  androidNetworkRestCall.callANGETService(WebServices.API_PAYMENT_GATEWAY_LIST(sessionManager.getAccessToken()), 1, false);

       /* final PaymentGatewayResponse gatewayResponse = objectMapper.readValue(response, PaymentGatewayResponse.class);

        if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(gatewayResponse.getErrorStatus().getCode())) {
            List<PaymentGateway> paymentGateways = gatewayResponse.getPaymentGatewayList();
            initAdapter(paymentGateways);
        } else {
            AlertUtils.showAlerterFailure((AppCompatActivity) requireActivity(), gatewayResponse.getErrorStatus().getMessage());
        }*/
    }

    @Override
    public void clickOnAddCard(CardListInfo paymentGateway) {
        super.clickOnAddCard(paymentGateway);
        if ("Add Card".equalsIgnoreCase(paymentGateway.getName())) {
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(),PaymentCardMethodActivity.class, false,false, false, null);
        }
    }

}
