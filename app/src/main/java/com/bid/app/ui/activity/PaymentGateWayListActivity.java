package com.bid.app.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bid.app.R;
import com.bid.app.adaper.PaymentGatewayAdapter;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.CardListInfo;
import com.bid.app.model.response.CardsListResponse;
import com.bid.app.model.response.Questionnaire;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.base.BaseActivity;
import com.bid.app.ui.fragment.PaymentGatewayListFragment;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentGateWayListActivity extends BaseActivity {

    private static final String TAG = PaymentGatewayListFragment.class.getSimpleName();

    private SessionManager sessionManager = null;
    private APIInterface apiInterface;

    private PaymentGatewayAdapter paymentGatewayAdapter;
    private RecyclerView card_list;
    private SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayout linearLayoutToolBar;
    private TextView tvTitleToolBar;
    private ImageView ivBackToolBar;

    private TextView tvAddress;
    private TextView tvAddAddress;
    private ConstraintLayout addressLayout;

    private TextView tvTitle;
    private ImageView imgAddCard, imgBack;
    private Questionnaire questionnaire;
    private Bundle bundle;
    private CardListInfo cardListInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundle();
        setContentView(R.layout.activity_payment_gateway_list);
        initView();
    }

    private void getBundle() {
        bundle = getIntent().getExtras();
        questionnaire = (Questionnaire) getIntent().getSerializableExtra("questionnaire");
        cardListInfo = (CardListInfo) getIntent().getSerializableExtra("cardListInfo");
        Log.d(TAG, "questionnaire " + questionnaire);
    }

    private void initView() {
        sessionManager = new SessionManager(PaymentGateWayListActivity.this);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Card List");
        imgBack = findViewById(R.id.imgBack);
        imgAddCard = findViewById(R.id.imgAddCard);
        imgBack.setOnClickListener(this);
        imgAddCard.setOnClickListener(this);
        tvAddAddress = findViewById(R.id.tv_add_address_payment_list);
        tvAddress = findViewById(R.id.tv_add_text_address_payment_list);
        addressLayout = findViewById(R.id.constraint_layout_address_payment_list);

        card_list = findViewById(R.id.card_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(PaymentGateWayListActivity.this);
        card_list.setLayoutManager(layoutManager);

        swipeRefreshLayout = findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.imgAddCard:
                AppActivityManager.redirectTo(PaymentGateWayListActivity.this, PaymentCardMethodActivity.class, false, false, false, null);
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

    private void callPaymentService() {
        swipeRefreshLayout.setRefreshing(true);

        Logger.e(TAG, "getAccessToken " + sessionManager.getAccessToken());

        Call<CardsListResponse> listResponseCall = apiInterface.cards(sessionManager.getAccessToken(), "1");
        listResponseCall.enqueue(new Callback<CardsListResponse>() {
            @Override
            public void onResponse(Call<CardsListResponse> call, Response<CardsListResponse> response) {
                //    CustomProgressDialog.getInstance().dismissDialog();
                final CardsListResponse listResponse = response.body();
                final ErrorStatus status = listResponse.getErrorStatus();

                if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {

                    List<CardListInfo> paymentGateways = listResponse.getCardListInfo();
                    initAdapter(paymentGateways);
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    AlertUtils.showAlerterFailure(PaymentGateWayListActivity.this, status.getMessage());
                }
            }

            @Override
            public void onFailure(Call<CardsListResponse> call, Throwable t) {
                t.printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
                //  CustomProgressDialog.getInstance().dismissDialog();
            }
        });

    }

    private void initAdapter(final List<CardListInfo> list) {
        paymentGatewayAdapter = new PaymentGatewayAdapter(PaymentGateWayListActivity.this, list, PaymentGateWayListActivity.this);
        card_list.setAdapter(paymentGatewayAdapter);
        paymentGatewayAdapter.notifyDataSetChanged();
    }

    @Override
    public void clickOnAddCard(CardListInfo cardListInfo) {
        AppActivityManager.redirectWithSerializedQuestionnaireObject(PaymentGateWayListActivity.this, ScheduleMapActivity.class, false, true, bundle, questionnaire, cardListInfo);
        finish();
    }
   /* @Override
    public void clickOnAddCard(PaymentGateway paymentGateway) {
        super.clickOnAddCard(paymentGateway);
        if ("Add Card".equalsIgnoreCase(paymentGateway.getName())) {
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(),PaymentCardMethodActivity.class, false,false, false, null);
        }
    }*/

    @Override
    public void onBackPressed() {
        if (cardListInfo != null) {
            AppActivityManager.redirectWithSerializedQuestionnaireObject(PaymentGateWayListActivity.this, ScheduleMapActivity.class, false, false, bundle, questionnaire, null);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        callPaymentService();
    }
}
