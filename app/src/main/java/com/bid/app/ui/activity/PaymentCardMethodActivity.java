package com.bid.app.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bid.app.R;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.request.AddCardRequest;
import com.bid.app.model.response.AddCardResponse;
import com.bid.app.model.response.CardListInfo;
import com.bid.app.model.response.Questionnaire;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.base.BaseActivity;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.ExpiryDateTextWatcher;

import org.apache.commons.lang3.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PaymentCardMethodActivity extends BaseActivity {

    private static final String TAG = PaymentCardMethodActivity.class.getSimpleName();

    private SessionManager sessionManager = null;
    private APIInterface apiInterface;

    private TextView tvTitle;
    private TextView tvConnect;
    private ImageView imgBack;

    private EditText etNameOnCard;
    private EditText etCardNumber;
    private EditText etExpiration;
    private EditText etCVV;
    private Bundle bundle;
    private Questionnaire questionnaire;
    private CardListInfo cardListInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_card_setup_method);
        getBundle();
        sessionManager = new SessionManager(PaymentCardMethodActivity.this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        initToolBar();
        initController();
    }


    private void getBundle() {
        bundle = getIntent().getExtras();
        questionnaire = (Questionnaire) getIntent().getSerializableExtra("questionnaire");
        cardListInfo = (CardListInfo) getIntent().getSerializableExtra("cardListInfo");
        Log.d(TAG, "questionnaire " + questionnaire);
    }

    private void initToolBar() {
        tvTitle = findViewById(R.id.tvTitle);
        imgBack = findViewById(R.id.imgBack);

        tvTitle.setText("Add Card");

        imgBack.setOnClickListener(this);
    }

    private void initController() {

        etNameOnCard = findViewById(R.id.et_name_on_card_setup_payment);
        etExpiration = findViewById(R.id.et_expiration_setup_payment);
        etCardNumber = findViewById(R.id.et_card_number_setup_payment);
        etCVV = findViewById(R.id.et_cvv_setup_payment);
        tvConnect = findViewById(R.id.tv_connect_setup_payment);
        tvConnect.setOnClickListener(this);
        etExpiration.addTextChangedListener(new ExpiryDateTextWatcher());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;

            case R.id.tv_connect_setup_payment:
                validateAddCard();
                break;
        }
    }

    private void validateAddCard() {
        if (StringUtils.isEmpty(etNameOnCard.getText().toString())) {
            AlertUtils.showAlerterWarning(PaymentCardMethodActivity.this, "Card name should not be empty!");
        } else if (StringUtils.isEmpty(etCardNumber.getText().toString())) {
            AlertUtils.showAlerterWarning(PaymentCardMethodActivity.this, "Card number should not be empty!");
        } else if (StringUtils.isEmpty(etExpiration.getText().toString())) {
            AlertUtils.showAlerterWarning(PaymentCardMethodActivity.this, "Card expiration should not be empty!");
        } else if (StringUtils.isEmpty(etCVV.getText().toString())) {
            AlertUtils.showAlerterWarning(PaymentCardMethodActivity.this, "CVV number should not be empty!");
        } else {
//            final AddCardRequestNew request = new AddCardRequestNew();
//            request.setName(etNameOnCard.getText().toString());
//            request.setCard_number(etExpiration.getText().toString());
//            request.setExpiry_date(etCardNumber.getText().toString());
//            request.setCvv(etCVV.getText().toString());
//            callAddCardRequestService(request);

            CardListInfo cardListInfo = new CardListInfo();
            cardListInfo.setName(etNameOnCard.getText().toString().trim());
            cardListInfo.setCard_number(etCardNumber.getText().toString().trim());
            cardListInfo.setExpiry_date(etExpiration.getText().toString().trim());
            cardListInfo.setUser_id(sessionManager.getUserId());

            AppActivityManager.redirectWithSerializedQuestionnaireObject(PaymentCardMethodActivity.this, ScheduleMapActivity.class, false, false, bundle, questionnaire, cardListInfo);
            finish();
        }
    }

    // Add Card Api New Card
    private void callAddCardRequestService(AddCardRequest request) {
        CustomProgressDialog.getInstance().showDialog(PaymentCardMethodActivity.this, true);
        Call<AddCardResponse> listResponseCall = apiInterface.addCard(sessionManager.getAccessToken(), request);
        listResponseCall.enqueue(new Callback<AddCardResponse>() {
            @Override
            public void onResponse(Call<AddCardResponse> call, Response<AddCardResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                final AddCardResponse listResponse = response.body();

                final ErrorStatus status = listResponse.getErrorStatus();

                if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                    AlertUtils.showAlerterSuccess(PaymentCardMethodActivity.this, status.getMessage());
                    // finish();
                    etNameOnCard.setText("");
                    etCardNumber.setText("");
                    etExpiration.setText("");
                    etCVV.setText("");
                } else {
                    AlertUtils.showAlerterFailure(PaymentCardMethodActivity.this, status.getMessage());
                }

            }

            @Override
            public void onFailure(Call<AddCardResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });

    }


}
