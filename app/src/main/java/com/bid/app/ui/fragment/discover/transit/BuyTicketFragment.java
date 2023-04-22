package com.bid.app.ui.fragment.discover.transit;

import static com.bid.app.ui.activity.DashboardActivity.context;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.bid.app.model.request.PurchaseTicketRequest;
import com.bid.app.model.response.ATripResponse;
import com.bid.app.model.response.GetWalletBalanceResponse;
import com.bid.app.model.response.StatusResponse;
import com.bid.app.model.view.PurchaseTicketRequestData;
import com.bid.app.model.view.Ticket;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.wallet.TopUpFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
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

public class BuyTicketFragment extends BaseFragment {
    ImageView qrCode;
    AppCompatButton buyTicket, cancelTicket;

    TextView cash, fareCost;
    RecyclerView summary;
    SessionManager sessionManager;

    ATripResponse tripResponse;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_transport_buy_ticket, container, false);

        getBundle();
        init(view);
        run();
        return view;
    }
    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            tripResponse = (ATripResponse) bundle.getSerializable(IBundle.BUNDLE_TICKETS);
        }
    }
    public void init(View view) {
        sessionManager = new SessionManager(requireActivity());
        ((DashboardActivity) requireActivity()).setTitleAndImage("Ticket", R.drawable.ic_back_arrow, false);
        cash = view.findViewById(R.id.cash);
        fareCost = view.findViewById(R.id.fare_cost);
        summary = view.findViewById(R.id.summary);
        summary.setLayoutManager(new LinearLayoutManager(getContext()));

        buyTicket = view.findViewById(R.id.buy_ticket);
        cancelTicket = view.findViewById(R.id.cancel_ticket);
        buyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purchaseTicket();
            }
        });
        cancelTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppActivityManager.popStack((AppCompatActivity) requireActivity(),1);
                Bundle bundle=new Bundle();
                bundle.putString(IBundle.BUNDLE_TRIP_ID, tripResponse.getTripId());
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new InstructionFragment(), R.id.frame_layout, true, false, true, bundle);
            }
        });
    }
    public void run() {
        //showQrCode();
        ArrayList<Ticket> tickets = new ArrayList<>();
        Double totalPrice = 0.0;
        for(Ticket ticket: tripResponse.getData()) {
            if(ticket.getMode().equals("driving")) {
                totalPrice += new Double(ticket.getFarePrice());
                tickets.add(ticket);
            }
        }
        DecimalFormat moneyFormat = new DecimalFormat("$#,##0.00");
        fareCost.setText(moneyFormat.format(totalPrice));
        loadWalletInfo();
        TripSummaryListAdapter tripSummaryListAdapter = new TripSummaryListAdapter(getContext(), (ArrayList<Ticket>) tickets);
        summary.setAdapter(tripSummaryListAdapter);
        tripSummaryListAdapter.notifyDataSetChanged();

    }
    public void showQrCode() {

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            final BitMatrix bitMatrix = qrCodeWriter.encode("ticket Infomation This is This is", BarcodeFormat.QR_CODE, 200, 200);
            int height = bitMatrix.getHeight();
            int width = bitMatrix.getWidth();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++){
                for (int y = 0; y < height; y++){
                    bmp.setPixel(x, y, bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
                }
            }
            qrCode.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void purchaseTicket() {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        PurchaseTicketRequest purchaseTicketRequests = new PurchaseTicketRequest();
        ArrayList<PurchaseTicketRequestData> purchaseTicketRequestDataArrayList = new ArrayList<>();
        for(Ticket ticket: tripResponse.getData()) {
            if(ticket.getMode().equals("driving")) {
                PurchaseTicketRequestData purchaseTicketRequestData = new PurchaseTicketRequestData();
                purchaseTicketRequestData.setTicketId(ticket.getId());
                purchaseTicketRequestData.setSeatNo(ticket.getSeatNumber());
                purchaseTicketRequestData.setBusId(ticket.getBusId());
                purchaseTicketRequestDataArrayList.add(purchaseTicketRequestData);
            }
        }
        purchaseTicketRequests.setTickets(purchaseTicketRequestDataArrayList);
        CustomProgressDialog.getInstance().showDialog(getContext(), true);
        Call<StatusResponse> listResponseCall = apiInterface.purchaseTicket(sessionManager.getAccessToken(), purchaseTicketRequests);
        listResponseCall.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                   CustomProgressDialog.getInstance().dismissDialog();
                final StatusResponse purchaseTicketResponse = response.body();
                if(purchaseTicketResponse == null){
                    AlertUtils.showAlerterWarning(requireActivity(), "Failed");
                    AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TopUpFragment(), R.id.frame_layout, true, false, false, null);
                }
                else if(purchaseTicketResponse.getError().getCode().equals("0")){
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
                                    AppActivityManager.popStack((AppCompatActivity) requireActivity(),1);
                                    Bundle bundle=new Bundle();
                                    bundle.putString(IBundle.BUNDLE_TRIP_ID, tripResponse.getTripId());
                                    AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new InstructionFragment(), R.id.frame_layout, true, false, true, bundle);

//                                    final Bundle bundle = new Bundle();
//                                    bundle.putSerializable(IBundle.BUNDLE_SELECTED_PERSONAL_DETAILS, purchaseTicketResponse.getTicket());
//
////                                    bundle.putString(IBundle.BUNDLE_SELECTED_PERSONAL_DETAILS, purchaseTicketResponse.toString());
//                                    AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TicketFragment(), R.id.frame_layout, true, false, true, bundle);
                                }
                            }).show();
                }
                else{
                    AlertUtils.showAlerterWarning(requireActivity(), purchaseTicketResponse.getError().getMessage());
                    AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TopUpFragment(), R.id.frame_layout, true, false, false, null);
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

    public void loadWalletInfo() {
//        CustomProgressDialog.getInstance().showDialog(getContext(), true);
        sessionManager = new SessionManager(getContext());
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<GetWalletBalanceResponse> listResponseCall = apiInterface.getWalletBalance(sessionManager.getAccessToken());
        listResponseCall.enqueue(new Callback<GetWalletBalanceResponse>() {
            @Override
            public void onResponse(Call<GetWalletBalanceResponse> call, Response<GetWalletBalanceResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                GetWalletBalanceResponse walletBalanceResponse = response.body();
                DecimalFormat moneyFormat = new DecimalFormat("$#,##0.00");
                cash.setText(moneyFormat.format(new Float(walletBalanceResponse.getBalance())));
            }

            @Override
            public void onFailure(Call<GetWalletBalanceResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });

    }
}
