package com.bid.app.ui.fragment.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bid.app.R;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.request.AddCardRequest;
import com.bid.app.model.response.AddCardResponse;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.home.HomeWalletFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCardFragment extends BaseFragment { // ST
    EditText cardNumber, cardHolder, expiryDate, Cvv;
    TextView addCard;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_add_card, container, false);
        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_add_card), R.drawable.ic_back_arrow, false);
        initController(view);

        return view;
    }

    public void initController(View view) {
        cardNumber  = view.findViewById(R.id.card_number);
        cardHolder  = view.findViewById(R.id.card_holder);
        expiryDate  = view.findViewById(R.id.expiry_date);
        Cvv         = view.findViewById(R.id.cvv);
        addCard     = view.findViewById(R.id.add_card);
        addCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.add_card:
                addCard();
                break;
        }
    }

    public void addCard() {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        if(cardNumber.getText().toString().length() == 0 ) {
            AlertUtils.showAlerterWarning(requireActivity(),"Enter CardNumber!");
        }
        else if(cardHolder.getText().toString().length() == 0 ) {
            AlertUtils.showAlerterWarning(requireActivity(),"Enter CardHolder!");
        }
        else if(expiryDate.getText().toString().length() == 0 ) {
            AlertUtils.showAlerterWarning(requireActivity(),"Enter ExpiryDate!");
        }
        else if(Cvv.getText().toString().length() == 0 ) {
            AlertUtils.showAlerterWarning(requireActivity(),"Enter CVV!");
        }
        else{
            SessionManager sessionManager = new SessionManager(requireActivity());
            AddCardRequest addCardRequest = new AddCardRequest();
            addCardRequest.setCardNumber(cardNumber.getText().toString());
            addCardRequest.setName(cardHolder.getText().toString());
            addCardRequest.setExpiryDate(expiryDate.getText().toString());
            addCardRequest.setCvv(Cvv.getText().toString());
            CustomProgressDialog.getInstance().showDialog(getContext(), true);

            Call<AddCardResponse> listResponseCall = apiInterface.addCard(sessionManager.getAccessToken(), addCardRequest);
            listResponseCall.enqueue(new Callback<AddCardResponse>() {
                @Override
                public void onResponse(Call<AddCardResponse> call, Response<AddCardResponse> response) {
                    CustomProgressDialog.getInstance().dismissDialog();
                    AddCardResponse addCardResponse = response.body();
                    if(addCardResponse.getErrorStatus().getCode().equals("0")){
                        AlertUtils.showAlerterSuccess(requireActivity(), "Card added!");
                        AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new HomeWalletFragment(), R.id.frame_layout, false, false, false, null);

                    }
                    else{
                        AlertUtils.showAlerterFailure(requireActivity(), addCardResponse.getErrorStatus().getMessage());
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
}
