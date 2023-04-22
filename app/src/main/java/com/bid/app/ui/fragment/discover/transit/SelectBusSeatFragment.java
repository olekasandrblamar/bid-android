package com.bid.app.ui.fragment.discover.transit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.bid.app.R;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.CardListInfo;
import com.bid.app.model.response.GetAvailableSeatResponse;
import com.bid.app.model.response.GetScheduleRouteResponse;
import com.bid.app.model.response.Questionnaire;
import com.bid.app.model.view.Schedule;
import com.bid.app.model.view.Ticket;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.activity.PaymentCardMethodActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectBusSeatFragment extends DialogFragment {
    ArrayList<Integer> seat_array;
    Button btn_checkout_otp;
    TextView seatNumber;
    TextView startTime, busNumber, ticketPrice, traveller;
    private SessionManager sessionManager;

    private APIInterface apiInterface;
    ArrayList<Boolean> isAvailable;
    View root;
    Context context;
    int selectedSeat;

    Ticket selectedTicket;

    interface EventListener{
        void onSelect(int position);
    }

    EventListener eventListener;
    public SelectBusSeatFragment( Context context, Ticket selectedTicket) {
        this.context = context;
        this.selectedTicket = selectedTicket;
    }
    public void setListener( EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        root = inflater.inflate(R.layout.fragment_select_bus_seat, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
 //       getBundle();
        initController(root);
        run();

        builder.setView(root);
        return builder.create();
    }

    private void initController(View view){
        seatNumber = view.findViewById(R.id.seat_number);
        startTime = view.findViewById(R.id.start_time);
        ticketPrice = view.findViewById(R.id.ticket_price);
        busNumber = view.findViewById(R.id.bus_number);
        traveller = view.findViewById(R.id.traveller);

        sessionManager = new SessionManager(getContext());

        ((DashboardActivity) requireActivity()).setTitleAndImage("Select Bus Seat", R.drawable.ic_back_arrow, false);
        btn_checkout_otp = (Button)view.findViewById(R.id.btn_checkout_otp);
        btn_checkout_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(seatNumber.getText().toString().length() == 0){
                    AlertUtils.showAlerterWarning(requireActivity(), "Please select seat!");
                    return;
                }
                eventListener.onSelect(selectedSeat);
//                Bundle bundle=new Bundle();
//                bundle.putSerializable(IBundle.BUNDLE_SEAT_NUMBER, seatNumber.getText().toString());
//                bundle.putSerializable(IBundle.BUNDLE_TICKET, selectedTicket);
//                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new BuyTicketFragment(), R.id.frame_layout, true, false, true, bundle);
            }
        });

        ///////////ADDED BY ARROW///////
        isAvailable = new ArrayList<>();
        for(int i = 0; i < 28; i++) isAvailable.add(false);
        seat_array = new ArrayList<>();
        seat_array.add(R.id.seat_btn1);
        seat_array.add(R.id.seat_btn2);
        seat_array.add(R.id.seat_btn3);
        seat_array.add(R.id.seat_btn4);
        seat_array.add(R.id.seat_btn5);
        seat_array.add(R.id.seat_btn6);
        seat_array.add(R.id.seat_btn7);
        seat_array.add(R.id.seat_btn8);
        seat_array.add(R.id.seat_btn9);
        seat_array.add(R.id.seat_btn10);
        seat_array.add(R.id.seat_btn11);
        seat_array.add(R.id.seat_btn12);
        seat_array.add(R.id.seat_btn13);
        seat_array.add(R.id.seat_btn14);
        seat_array.add(R.id.seat_btn15);
        seat_array.add(R.id.seat_btn16);
        seat_array.add(R.id.seat_btn17);
        seat_array.add(R.id.seat_btn18);
        seat_array.add(R.id.seat_btn19);
        seat_array.add(R.id.seat_btn20);
        seat_array.add(R.id.seat_btn21);
        seat_array.add(R.id.seat_btn22);
        seat_array.add(R.id.seat_btn23);
        seat_array.add(R.id.seat_btn24);
        seat_array.add(R.id.seat_btn25);
        seat_array.add(R.id.seat_btn26);
        seat_array.add(R.id.seat_btn27);
        seat_array.add(R.id.seat_btn28);

        for(int i = 0; i < seat_array.size(); i++) {
            final ImageButton button = view.findViewById(seat_array.get(i));
            final Integer I = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isAvailable.get(I)){
                        Toast.makeText(getContext(), "This seat is already reserved!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for(int i = 0; i < 28; i++) {
                        Integer seat = seat_array.get(i);
                        if(isAvailable.get(i)) ((ImageButton)root.findViewById(seat)).setImageResource(R.drawable.seat_pad);
                        else ((ImageButton)root.findViewById(seat)).setImageResource(R.drawable.seat_pad_unavailable);
                    }

                    seatNumber.setText(((Integer)(I +  1)).toString());
                    selectedSeat = I;
                    ((ImageButton)view).setImageResource(R.drawable.seat_pad_red);
                }
            });
        }


    }
    public void run() {
        startTime.setText(selectedTicket.getGetOffTime());
        busNumber.setText(selectedTicket.getBusNo());
        ticketPrice.setText(selectedTicket.getFarePrice());
        traveller.setText(sessionManager.getFirstName() + " " +sessionManager.getLastName());

        getAvailableSeat();
    }


    public void getAvailableSeat() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        CustomProgressDialog.getInstance().showDialog(getContext(), true);

        Call<GetAvailableSeatResponse> listResponseCall = apiInterface.getAvailableSeat(sessionManager.getAccessToken(),"1", "2", selectedTicket.getBusId());
        listResponseCall.enqueue(new Callback<GetAvailableSeatResponse>() {
            @Override
            public void onResponse(Call<GetAvailableSeatResponse> call, Response<GetAvailableSeatResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                GetAvailableSeatResponse getAvailableSeatResponse = response.body();
                if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(getAvailableSeatResponse.getErrorStatus().getCode())) {
                    for(Integer seat: seat_array) {
                        ((ImageButton)root.findViewById(seat)).setImageResource(R.drawable.seat_pad_unavailable);
                    }
                    if(getAvailableSeatResponse.getAvailableSeat() != null) {
                        for(String seatIdStr: getAvailableSeatResponse.getAvailableSeat()) {
                            Integer seatId = new Integer(seatIdStr) - 1;
                            if(seatId >= 0 && seatId < 28){
                                isAvailable.set(seatId, true);
                                ((ImageButton)root.findViewById(seat_array.get(seatId))).setImageResource(R.drawable.seat_pad);
                            }
                        }
                    }

                    if(selectedTicket.getSeatNumber() != null && selectedTicket.getSeatNumber().length() > 0) {
                        selectedSeat = new Integer(selectedTicket.getSeatNumber());
                        seatNumber.setText(((Integer)(selectedSeat +  1)).toString());
                        ((ImageButton)root.findViewById( seat_array.get(selectedSeat))).setImageResource(R.drawable.seat_pad_red);
                    }

                }
                else {
                    AlertUtils.showAlerterWarning(requireActivity(), getAvailableSeatResponse.getErrorStatus().getMessage());
                }
            }

            @Override
            public void onFailure(Call<GetAvailableSeatResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }

}
