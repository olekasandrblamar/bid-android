package com.bid.app.ui.fragment.wallet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bid.app.R;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.request.AddCardRequest;
import com.bid.app.model.response.AddCardResponse;
import com.bid.app.model.response.CardListInfo;
import com.bid.app.model.response.StatusResponse;
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

public class CardDetailFragment extends BaseFragment { // ST
    TextView cardNumber, cardHolder, expiryDate, Cvv;
    TextView saveCard, deleteCard;
    CardListInfo cardListInfo;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_card_detail, container, false);
        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_card_detail), R.drawable.ic_back_arrow, false);
        getBundle();
        initController(view);

        return view;
    }
    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            cardListInfo = (CardListInfo) bundle.getSerializable(IBundle.BUNDLE_CARD_LIST_INFO);
        }
    }
    public void initController(View view) {
        cardNumber  = view.findViewById(R.id.card_number);
        cardHolder  = view.findViewById(R.id.card_holder);
        expiryDate  = view.findViewById(R.id.expiry_date);
        Cvv         = view.findViewById(R.id.cvv);
        saveCard    = view.findViewById(R.id.save_card);
        deleteCard  = view.findViewById(R.id.delete_card);
        saveCard.setOnClickListener(this);
        deleteCard.setOnClickListener(this);
        cardNumber.setText(cardListInfo.getCard_display_number());
        cardHolder.setText(cardListInfo.getName());
        expiryDate.setText(cardListInfo.getExpiry_date());
//        Cvv.setText(cardListInfo.getCardUserInfo().);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.saved_card:
                saveCard();
                break;
            case R.id.delete_card:
                deleteCard();
                break;
        }
    }

    public void saveCard() {
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
                        AlertUtils.showAlerterFailure(requireActivity(), "Card added failed!");
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

    public void deleteCard() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Delete Confirm");
        builder.setMessage("Really delete this card?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SessionManager sessionManager = new SessionManager(requireActivity());
                APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
                Call<StatusResponse> listResponseCall = apiInterface.deleteACard(cardListInfo.getId(),sessionManager.getAccessToken());
                CustomProgressDialog.getInstance().showDialog(getContext(), true);
                listResponseCall.enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        CustomProgressDialog.getInstance().dismissDialog();
                        StatusResponse addCardResponse = response.body();
                        if(addCardResponse.getError().getCode().equals("0")){
                            AlertUtils.showAlerterSuccess(requireActivity(), "Card deleted!");
                            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new HomeWalletFragment(), R.id.frame_layout, false, false, false, null);

                        }
                        else{
                            AlertUtils.showAlerterFailure(requireActivity(), "Card delete failed!");
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        CustomProgressDialog.getInstance().dismissDialog();
                        t.printStackTrace();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
