package com.bid.app.ui.fragment.wallet;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.CardsVerticalAdapter;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.request.TopUpByCardRequest;
import com.bid.app.model.response.CardListInfo;
import com.bid.app.model.response.CardsListResponse;
import com.bid.app.model.response.StatusResponse;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopUpFragment extends BaseFragment { // ST

    private static final String TAG = TopUpFragment.class.getSimpleName();

    static Integer CARD_METHOD = 0;
    static Integer KANOO_METHOD = 1;
    AppCompatButton proceed;
    LinearLayout savedCard,savedDropDown, kanoo;
    ImageView cardTypeImg;
    TextView cardNumber;
    EditText amount;
    Integer seelctedPaymentMethod;
    List<CardListInfo> cardListInfoList;
    CardListInfo selectedCard;

    private Dialog cameraGalleryDialog = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_top_up, container, false);
        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.topup), R.drawable.ic_back_arrow, false);
        init(view);
        run();
        return view;
    }
    public void init(View view) {
        selectedCard = null;
        cardListInfoList = new ArrayList<>();
        amount = view.findViewById(R.id.amount);
        amount.addTextChangedListener(new NumberTextWatcher(amount, "#,###"));
        proceed = view.findViewById(R.id.proceed);
        savedCard = view.findViewById(R.id.saved_card);
        savedDropDown = view.findViewById(R.id.saved_drop_down);
        kanoo = view.findViewById(R.id.kanoo);
        cardTypeImg = view.findViewById(R.id.card_type_img);
        cardNumber = view.findViewById(R.id.card_number);
        savedCard.setBackgroundColor(Color.LTGRAY);
        kanoo.setBackgroundColor(Color.GRAY);
        seelctedPaymentMethod = CARD_METHOD;
        proceed.setOnClickListener(this);
        savedCard.setOnClickListener(this);
        savedDropDown.setOnClickListener(this);
        kanoo.setOnClickListener(this);
        getCards();
    }
    public void run() {

    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.saved_card:
                savedDropDown.setVisibility(View.VISIBLE);
                savedCard.setBackgroundColor(Color.LTGRAY);
                kanoo.setBackgroundColor(Color.GRAY);
                seelctedPaymentMethod = CARD_METHOD;
                if (cameraGalleryDialog != null) {
                    cameraGalleryDialog.dismiss();
                }

                break;
            case R.id.saved_drop_down:
                showCardDialog();

                break;
            case R.id.kanoo:
                savedDropDown.setVisibility(View.GONE);
                savedCard.setBackgroundColor(Color.GRAY);
                kanoo.setBackgroundColor(Color.LTGRAY);
                seelctedPaymentMethod = KANOO_METHOD;
                if (cameraGalleryDialog != null) {
                    cameraGalleryDialog.dismiss();
                }

                break;
            case R.id.proceed:
                if (cameraGalleryDialog != null) {
                    cameraGalleryDialog.dismiss();
                }

                if(seelctedPaymentMethod == CARD_METHOD){
                    topUpByCard();
                }
                else{
                    AlertUtils.showAlerterWarning(requireActivity(),"coming up soon!");
                }
                break;


        }
    }
    public void topUpByCard() {
        if(amount.getText().toString().length() ==0) {
            AlertUtils.showAlerterWarning(requireActivity(), "Enter amount!");
            return;
        }
        Float intAmount =  Float.parseFloat(amount.getText().toString().replaceAll("[^\\d.]", ""));
        if(selectedCard == null){
            AlertUtils.showAlerterWarning(requireActivity(), "Select card!");
        }
        else if(intAmount<=0) {
            AlertUtils.showAlerterWarning(requireActivity(), "Enter amount!");
        }
        else{
            SessionManager sessionManager = new SessionManager(getContext());
            APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
            TopUpByCardRequest topUpRequest = new TopUpByCardRequest();
            topUpRequest.setAmount(intAmount.toString());
            topUpRequest.setCardId(selectedCard.getId());
            topUpRequest.setCvv("123");
            CustomProgressDialog.getInstance().showDialog(getContext(), true);
            final Call<StatusResponse> call = apiInterface.topUpByCard(sessionManager.getAccessToken(), topUpRequest);
            call.enqueue(new Callback<StatusResponse>() {
                @Override
                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                    CustomProgressDialog.getInstance().dismissDialog();
                    try {
                        if (response.body() != null) {
                            final StatusResponse statusResponse = response.body();
                            Bundle bundle=new Bundle();
                            bundle.putSerializable(IBundle.BUNDLE_STATUS_RESPONSE,statusResponse);
                            if(statusResponse.getError().equals("0")) {
                                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TopUpResultFragment(), R.id.frame_layout, false, false, true, bundle);
                            }else{
                                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TopUpResultFragment(), R.id.frame_layout, false, false, true, bundle);
                            }

                        } else {
                            Toast.makeText(getContext(), "Server response is empty.", Toast.LENGTH_SHORT).show();
                            Logger.e(TAG, "response.body() == null");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
    @Override
    public void clickOnCardListInfo(int position, CardListInfo cardListInfo){
        cameraGalleryDialog.dismiss();
        cardNumber.setText(cardListInfo.getCard_display_number());
        if(cardListInfo.getCard_type().equals("Visa")) {
            cardTypeImg.setImageResource(R.drawable.visa);
        }
        else if(cardListInfo.getCard_type().equals("MasterCard")) {
//            cardTypeImg.setImageResource(R.drawable.visa);
        }
        else if(cardListInfo.getCard_type().equals("Discover")) {
//            cardTypeImg.setImageResource(R.drawable.visa);
        }

    }
    private void showCardDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View optionView = inflater.inflate(R.layout.dialog_vertical_cards, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(optionView);
        final RecyclerView recyclerView = optionView.findViewById(R.id.card_recycler_view);
        CardsVerticalAdapter cardsVerticalAdapter = new CardsVerticalAdapter(getContext(),cardListInfoList, this );
        recyclerView.setAdapter(cardsVerticalAdapter);
        cardsVerticalAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cameraGalleryDialog = builder.create();
        cameraGalleryDialog.show();
    }

    public void getCards() {
        SessionManager sessionManager = new SessionManager(getContext());
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

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
                        if(cardListInfoList.size() >0)
                        {
                            selectedCard = cardListInfoList.get(0);
                            cardNumber.setText(selectedCard.getCard_display_number());
                            if(selectedCard.getCard_type().equals("Visa")) {
                                cardTypeImg.setImageResource(R.drawable.visa);
                            }
                            else if(selectedCard.getCard_type().equals("MasterCard")) {
//            cardTypeImg.setImageResource(R.drawable.visa);
                            }
                            else if(selectedCard.getCard_type().equals("Discover")) {
//            cardTypeImg.setImageResource(R.drawable.visa);
                            }
                        }
                        else {
                            cardNumber.setText("Please Add Card");
                        }


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

    public class NumberTextWatcher implements TextWatcher {

        private final DecimalFormat df;
        private final DecimalFormat dfnd;
        private final EditText et;
        private boolean hasFractionalPart;
        private int trailingZeroCount;

        public NumberTextWatcher(EditText editText, String pattern) {
            df = new DecimalFormat(pattern);
            df.setDecimalSeparatorAlwaysShown(true);
            dfnd = new DecimalFormat("#,##0.00");
            this.et = editText;
            hasFractionalPart = false;
        }

        @Override
        public void afterTextChanged(Editable s) {
            et.removeTextChangedListener(this);

            if (s != null && !s.toString().isEmpty()) {
                try {
                    int inilen, endlen;
                    inilen = et.getText().length();
                    String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "").replace("$","");
                    Number n = df.parse(v);
                    int cp = et.getSelectionStart();
                    if (hasFractionalPart) {
                        StringBuilder trailingZeros = new StringBuilder();
                        while (trailingZeroCount-- > 0)
                            trailingZeros.append('0');
                        et.setText(df.format(n) + trailingZeros.toString());
                    } else {
                        et.setText(dfnd.format(n));
                    }
                    et.setText("$".concat(et.getText().toString()));
                    endlen = et.getText().length();
                    int sel = (cp + (endlen - inilen));
                    if (sel > 0 && sel < et.getText().length()) {
                        et.setSelection(sel);
                    } else if (trailingZeroCount > -1) {
                        et.setSelection(et.getText().length() - 3);
                    } else {
                        et.setSelection(et.getText().length());
                    }
                } catch (NumberFormatException | ParseException e) {
                    e.printStackTrace();
                }
            }

            et.addTextChangedListener(this);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int index = s.toString().indexOf(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()));
            trailingZeroCount = 0;
            if (index > -1) {
                for (index++; index < s.length(); index++) {
                    if (s.charAt(index) == '0')
                        trailingZeroCount++;
                    else {
                        trailingZeroCount = 0;
                    }
                }
                hasFractionalPart = true;
            } else {
                hasFractionalPart = false;
            }
        }
    }
}
