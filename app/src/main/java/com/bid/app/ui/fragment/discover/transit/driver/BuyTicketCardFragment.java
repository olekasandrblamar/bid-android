package com.bid.app.ui.fragment.discover.transit.driver;

import static com.bid.app.ui.activity.DashboardActivity.context;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.TripSummaryListAdapter;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.request.PurchaseTicketByDriverRequest;
import com.bid.app.model.request.PurchaseTicketRequest;
import com.bid.app.model.response.ATripResponse;
import com.bid.app.model.response.GetWalletBalanceResponse;
import com.bid.app.model.response.StatusResponse;
import com.bid.app.model.view.PassengerData;
import com.bid.app.model.view.PurchaseTicketRequestData;
import com.bid.app.model.view.RoutePath;
import com.bid.app.model.view.Ticket;
import com.bid.app.model.view.Waypoint;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.discover.transit.InstructionFragment;
import com.bid.app.ui.fragment.wallet.TopUpFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Utils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.checkerframework.checker.units.qual.A;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyTicketCardFragment extends BaseFragment {
    ImageView qrCode;
    AppCompatButton buyTicket, cancelTicket;
    TextView cardNumber, expDate, cvc, totalFareCost;
    RecyclerView summary;
    SessionManager sessionManager;
    Spinner fromSpinner, toSpinner;

    PassengerData passengerData;
    ArrayList<String> list1, list2;
    ArrayList<String> posList1, posList2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_transport_buy_ticket_card, container, false);

        getBundle();
        init(view);
        run();
        return view;
    }
    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            passengerData = (PassengerData) bundle.getSerializable(IBundle.BUNDLE_PASSENGER);
        }
    }
    public void init(View view) {
        sessionManager = new SessionManager(requireActivity());
        ((DashboardActivity) requireActivity()).setTitleAndImage("Ticket", R.drawable.ic_back_arrow, false);
        cardNumber = view.findViewById(R.id.card_number);
        expDate = view.findViewById(R.id.exp_date);
        cvc = view.findViewById(R.id.cvc);
        totalFareCost = view.findViewById(R.id.total_price);
        summary = view.findViewById(R.id.summary);
        summary.setLayoutManager(new LinearLayoutManager(getContext()));

        buyTicket = view.findViewById(R.id.buy_ticket);
        cancelTicket = view.findViewById(R.id.cancel_ticket);

        fromSpinner = view.findViewById(R.id.from_spinner);
        toSpinner = view.findViewById(R.id.to_spinner);

        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        posList1 = new ArrayList<>();
        posList2 = new ArrayList<>();
        for(Waypoint waypoint: passengerData.getGetRouteByBusResponse().getData().getWaypoints()) {
            list1.add(waypoint.getName());
            posList1.add("(" + waypoint.getPosition().getLat() + "," + waypoint.getPosition().getLng() + ")");
            list2.add(waypoint.getName());
            posList2.add("(" + waypoint.getPosition().getLat() + "," + waypoint.getPosition().getLng() + ")");
        }

        ArrayAdapter adapter1 = new ArrayAdapter<String>( getContext(), R.layout.support_simple_spinner_dropdown_item, list1);
        fromSpinner.setAdapter(adapter1);

        ArrayAdapter adapter2 = new ArrayAdapter<String>( getContext(), R.layout.support_simple_spinner_dropdown_item, list2);
        toSpinner.setAdapter(adapter2);


        buyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validationCheck()) purchaseTicket();
            }
        });
        cancelTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppActivityManager.popStack((AppCompatActivity) requireActivity(),1);
//                Bundle bundle=new Bundle();
//                bundle.putString(IBundle.BUNDLE_TRIP_ID, tripResponse.getTripId());
//                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new InstructionFragment(), R.id.frame_layout, true, false, true, bundle);
            }
        });
    }
    public void run() {
        //showQrCode();

    }
    private Boolean validationCheck() {
        if(cardNumber.getText().toString().length() == 0) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter Card Number");
            return Boolean.FALSE;
        } else if(expDate.getText().toString().length() == 0) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter Expire Date");
            return Boolean.FALSE;
        } else if(cvc.getText().toString().length() == 0) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter CVC");
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
    public void purchaseTicket() {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        PurchaseTicketByDriverRequest purchaseTicketRequests = new PurchaseTicketByDriverRequest();
        purchaseTicketRequests.setFirstName(passengerData.getFirstName());
        purchaseTicketRequests.setLastName(passengerData.getLastName());
        purchaseTicketRequests.setMobile(passengerData.getMobile());
        purchaseTicketRequests.setSeatNo(passengerData.getSeatNumber());
        purchaseTicketRequests.setPaymentMethod("");
        purchaseTicketRequests.setCardNumber(cardNumber.getText().toString());
        purchaseTicketRequests.setCardName("testcard");
        purchaseTicketRequests.setExpiryDate(expDate.getText().toString());
        purchaseTicketRequests.setCvv(cvc.getText().toString());
        int fromPos = fromSpinner.getSelectedItemPosition();
        int toPos = toSpinner.getSelectedItemPosition();
        int i = 0;
        String routePaths = "";
        Double distance = 0.0;
        for(RoutePath routePath: passengerData.getGetRouteByBusResponse().getData().getPaths()) {
            i++;
            if(fromPos<i && i < toPos) {
                for(String path:routePath.getPath()) {
                    routePaths += " " + path;
                }
                distance += Utils.getDistance(routePath);
            }
        }

        purchaseTicketRequests.setFrom(passengerData.getGetRouteByBusResponse().getData().getWaypoints().get(fromPos).getId());
        purchaseTicketRequests.setTo(passengerData.getGetRouteByBusResponse().getData().getWaypoints().get(toPos).getId());
        purchaseTicketRequests.setFromLocation(posList1.get(fromPos));
        purchaseTicketRequests.setToLocation(posList2.get(toPos));
        purchaseTicketRequests.setDistance(distance.toString());
        purchaseTicketRequests.setPolyline(routePaths);

        CustomProgressDialog.getInstance().showDialog(getContext(), true);
        Call<StatusResponse> listResponseCall = apiInterface.purchaseTicketByDriver(sessionManager.getAccessToken(), purchaseTicketRequests);
        listResponseCall.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                   CustomProgressDialog.getInstance().dismissDialog();
                final StatusResponse purchaseTicketResponse = response.body();
                if(purchaseTicketResponse == null){
                    AlertUtils.showAlerterWarning(requireActivity(), "Failed");
//                    AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TopUpFragment(), R.id.frame_layout, true, false, false, null);
                }
                if(purchaseTicketResponse.getError().getCode().equals("0")){
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.purchase_success, null);
                    new AlertDialog.Builder(context)
                            .setView(dialogView)
                            .setTitle("Success!")
                            .setMessage("Buy ticket has succeed!")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Whatever...
                                    AppActivityManager.popStack((AppCompatActivity) requireActivity(),2);
                                }
                            }).show();
                }
                else{
                    AlertUtils.showAlerterWarning(requireActivity(), purchaseTicketResponse.getError().getMessage());
//                    AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TopUpFragment(), R.id.frame_layout, true, false, false, null);
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                t.printStackTrace();
                AlertUtils.showAlerterWarning(requireActivity(), "Net work Connection Problem!");
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }

}
