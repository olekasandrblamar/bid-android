package com.bid.app.ui.fragment.discover.transit.driver;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.bid.app.R;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.request.FindSomeOneWithMobileRequest;
import com.bid.app.model.response.FindSomeoneWithMobileResponse;
import com.bid.app.model.response.GetAvailableRoutesResponse;
import com.bid.app.model.response.GetRouteByBusResponse;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.PassengerData;
import com.bid.app.model.view.Profile;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddPassengerFragment extends BaseFragment {
    private static final String TAG = AddPassengerFragment.class.getSimpleName();

    SessionManager sessionManager;
    View root;
    TextView firstName, lastName, searchResult;
    AppCompatEditText mobileValue;
    RoundedImageView roundedImageView;
    LinearLayout creditDebitCard;
    Button mobileWallet, cash, findBtn;
    String seatNo;
    String searchedBid;
    private APIInterface apiInterface;

    GetRouteByBusResponse getRouteByBusResponse;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_driver_add_passenger, container, false);
        sessionManager = new SessionManager(requireActivity());
        getBundle();
        initIds(root, savedInstanceState);
        initFragment();
        run();
        return root;
    }
    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            seatNo = bundle.getString(IBundle.BUNDLE_SEAT_NUMBER);
            getRouteByBusResponse = (GetRouteByBusResponse) bundle.getSerializable(IBundle.BUNDLE_ROUTE);

        }
    }

    private void initFragment() {

//        firstName.setText(sessionManager.getFirstName());
//        lastName.setText(sessionManager.getLastName());
//        mobile.setText(sessionManager.getMobileNumber());
        Glide.with(requireActivity())
                .load(sessionManager.getUserImage())
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .skipMemoryCache(false)
                .centerCrop()
//                        .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(roundedImageView);
    }


    private void initIds(View view, Bundle savedInstanceState) {
        ((DashboardActivity) requireActivity()).setTitleAndImage("bus No: " + sessionManager.getUserId(), R.drawable.ic_back_arrow, false);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        roundedImageView = view.findViewById(R.id.iv_avatar_bid);

        firstName = view.findViewById(R.id.first_name);
        lastName = view.findViewById(R.id.last_name);
        mobileValue = view.findViewById(R.id.mobile_number);
        creditDebitCard = view.findViewById(R.id.credit_debit_card);
        mobileWallet = view.findViewById(R.id.mobile_wallet);
        cash = view.findViewById(R.id.cash);
        searchResult = view.findViewById(R.id.search_result);
        findBtn = view.findViewById(R.id.btn_find);

        findBtn.setOnClickListener(this);
        creditDebitCard.setOnClickListener(this);
        mobileWallet.setOnClickListener(this);
        cash.setOnClickListener(this);

    }

    private void run() {
        mobileValue.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
            //we need to know if the user is erasing or inputing some new character
            private boolean backspacingFlag = false;
            //we need to block the :afterTextChanges method to be called again after we just replaced the EditText text
            private boolean editedFlag = false;
            //we need to mark the cursor position and restore it after the edition
            private int cursorComplement;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //we store the cursor local relative to the end of the string in the EditText before the edition
                cursorComplement = s.length()-mobileValue.getSelectionStart();
                //we check if the user ir inputing or erasing a character
                if (count > after) {
                    backspacingFlag = true;
                } else {
                    backspacingFlag = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // nothing to do here =D
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                //what matters are the phone digits beneath the mask, so we always work with a raw string with only digits
                String phone = string.replaceAll("[^\\d]", "");

                //if the text was just edited, :afterTextChanged is called another time... so we need to verify the flag of edition
                //if the flag is false, this is a original user-typed entry. so we go on and do some magic
                if (!editedFlag) {

                    //we start verifying the worst case, many characters mask need to be added
                    //example: 999999999 <- 6+ digits already typed
                    // masked: (999) 999-999
                    if (phone.length() >= 7 && !backspacingFlag) {
                        //we will edit. next call on this textWatcher will be ignored
                        editedFlag = true;
                        //here is the core. we substring the raw digits and add the mask as convenient
                        String ans = "+" + phone.substring(0, 1) +"(" + phone.substring(1, 4) + ") " + phone.substring(4,7) + "-" + phone.substring(7,Math.min(11, phone.length()));
                        mobileValue.setText(ans);
                        //we deliver the cursor to its original position relative to the end of the string
                        mobileValue.setSelection(mobileValue.getText().length()-cursorComplement);
                        //we end at the most simple case, when just one character mask is needed
                        //example: 99999 <- 3+ digits already typed
                        // masked: (999) 99
                    } else if (phone.length() >= 4 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = "+" + phone.substring(0, 1) + "(" +phone.substring(1, 4) + ") " + phone.substring(4);
                        mobileValue.setText(ans);
                        mobileValue.setSelection(mobileValue.getText().length()-cursorComplement);
                    }
                    // We just edited the field, ignoring this cicle of the watcher and getting ready for the next
                } else {
                    editedFlag = false;
                }
            }
        });
    }
    private Boolean validationCheck() {
        if(firstName.getText().toString().length() == 0) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter firstName");
            return Boolean.FALSE;
        } else if(lastName.getText().toString().length() == 0) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter firstName");
            return Boolean.FALSE;
        } else if(mobileValue.getText().toString().length() == 0) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter Mobile");
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    @Override
    public void onClick(View view) {
        PassengerData passengerData = new PassengerData();
        passengerData.setFirstName(firstName.getText().toString());
        passengerData.setLastName(lastName.getText().toString());
        passengerData.setMobile(mobileValue.getText().toString());
        passengerData.setSeatNumber(seatNo);
        passengerData.setGetRouteByBusResponse(getRouteByBusResponse);
        passengerData.setBid(searchedBid);
        Bundle bundle = new Bundle();
        bundle.putSerializable(IBundle.BUNDLE_PASSENGER, passengerData);
        super.onClick(view);
        switch (view.getId()) {

            case R.id.credit_debit_card:
                if(validationCheck()) AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new BuyTicketCardFragment(), R.id.frame_layout, true, false, true, bundle);
                break;
            case R.id.mobile_wallet:
                if(validationCheck()) AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new BuyTicketWalletFragment(), R.id.frame_layout, true, false, true, bundle);
                break;
            case R.id.cash:
                if(validationCheck()) AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new BuyTicketCashFragment(), R.id.frame_layout, true, false, true, bundle);
                break;
            case R.id.btn_find:
                findUser();
                break;
        }
    }

    public void findUser() {
        String mobileNumber = mobileValue.getText().toString();
        mobileNumber = mobileNumber.replaceAll("\\D+","");
        if(mobileNumber.length() == 0) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter phone number");
            return;
        }
        FindSomeOneWithMobileRequest request = new FindSomeOneWithMobileRequest();
        request.setMobile(mobileNumber);
        final Call<FindSomeoneWithMobileResponse> call = apiInterface.findSomeoneWithMobile(request);
        call.enqueue(new Callback<FindSomeoneWithMobileResponse>() {
            @Override
            public void onResponse(Call<FindSomeoneWithMobileResponse> call, Response<FindSomeoneWithMobileResponse> response) {

                CustomProgressDialog.getInstance().dismissDialog();
                if (response.body() != null) {
                    final FindSomeoneWithMobileResponse body = response.body();
                    final ErrorStatus status = body.getErrorStatus();
                    Profile profile = body.getData();
                    Logger.e(TAG, "callProfileUpdate Response: " + new Gson().toJson(body));
                    searchedBid = "";
                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        if(profile == null) {
                            searchResult.setText("No member found");
                            mobileWallet.setVisibility(View.GONE);
                        }
                        else {
                            searchResult.setText("");
                            firstName.setText(profile.getFirstName());
                            lastName.setText(profile.getLastName());
                            mobileWallet.setVisibility(View.VISIBLE);
                            searchedBid = profile.getB_id();
                        }
                    } else {
                        AlertUtils.showAlerterWarning(requireActivity(), status.getMessage());
                    }
                } else {
                    Logger.e(TAG, "response.body() == null");
                }
            }

            @Override
            public void onFailure(Call<FindSomeoneWithMobileResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });

    }
}
