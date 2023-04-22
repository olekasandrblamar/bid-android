package com.bid.app.ui.fragment.discover.transit.driver;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bid.app.R;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.GetAvailableRoutesResponse;
import com.bid.app.model.response.GetAvailableSeatResponse;
import com.bid.app.model.response.GetRouteByBusResponse;
import com.bid.app.model.view.Schedule;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.about.AboutFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowBusSeatFragment extends BaseFragment {
    ArrayList<Integer> seat_array;
    TextView busNumber, traveller;
    private SessionManager sessionManager;

    private APIInterface apiInterface;
    ArrayList<Boolean> isAvailable;
    TextView addPassenger;

    Integer selectedSeat;
    GetRouteByBusResponse getRouteByBusResponse;
    View root;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_show_bus_seat, container, false);
        getBundle();
        initController(root);
        run();
        return root;
    }


    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            getRouteByBusResponse = (GetRouteByBusResponse) bundle.getSerializable(IBundle.BUNDLE_ROUTE);
        }
    }

    private void initController(View view){
        traveller = view.findViewById(R.id.traveller);
        busNumber = view.findViewById(R.id.bus_number);
        addPassenger = view.findViewById(R.id.add_passenger);
        addPassenger.setOnClickListener(this);
        sessionManager = new SessionManager(getContext());

        ((DashboardActivity) requireActivity()).setTitleAndImage("Show bus status", R.drawable.ic_back_arrow, false);


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
                        AlertUtils.showAlerterWarning(requireActivity(), "This seat is already reserved!");
                        return;
                    }

                    for(int i = 0; i < 28; i++) {
                        Integer seat = seat_array.get(i);
                        if(isAvailable.get(i)) ((ImageButton)root.findViewById(seat)).setImageResource(R.drawable.seat_pad);
                        else ((ImageButton)root.findViewById(seat)).setImageResource(R.drawable.seat_pad_unavailable);
                    }

//                    seatNumber.setText(((Integer)(I +  1)).toString());
                    selectedSeat = I;
                    ((ImageButton)view).setImageResource(R.drawable.seat_pad_red);
                }
            });
        }
    }
    public void run() {
/*
        startTime.setText(selectedSchedule.getStartTime());
        busNumber.setText(selectedSchedule.getBusId());
        ticketPrice.setText(selectedSchedule.getFarePrice());
*/
//        busNumber.setText(sessionManager.getUserId());
        getAvailableSeat();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.add_passenger:
                if(selectedSeat == null) {
                    AlertUtils.showAlerterWarning(requireActivity(), "Please select seat");
                }
                else
                {
                    Bundle bundle = new Bundle();
                    bundle.putString(IBundle.BUNDLE_SEAT_NUMBER, new Integer(selectedSeat).toString());
                    bundle.putSerializable(IBundle.BUNDLE_ROUTE, getRouteByBusResponse);
                    AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new AddPassengerFragment(), R.id.frame_layout, true, false, true, bundle);
                }
                break;
        }
    }

    public void getAvailableSeat() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        CustomProgressDialog.getInstance().showDialog(getContext(), true);

        Call<GetAvailableSeatResponse> listResponseCall = apiInterface.getAvailableSeat(sessionManager.getAccessToken(),"1", "2", sessionManager.getUserId());
        listResponseCall.enqueue(new Callback<GetAvailableSeatResponse>() {
            @Override
            public void onResponse(Call<GetAvailableSeatResponse> call, Response<GetAvailableSeatResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                GetAvailableSeatResponse getAvailableSeatResponse = response.body();
                for(Integer seat: seat_array) {
                    ((ImageButton)root.findViewById(seat)).setImageResource(R.drawable.seat_pad_unavailable);
                }

                for(String seatIdStr: getAvailableSeatResponse.getAvailableSeat()) {
                    Integer seatId = new Integer(seatIdStr) - 1;
                    if(seatId >= 0 && seatId < 28){
                        isAvailable.set(seatId, true);
                        ((ImageButton)root.findViewById(seat_array.get(seatId))).setImageResource(R.drawable.seat_pad);
                    }
                }
                Integer memberCnt = 28 - getAvailableSeatResponse.getAvailableSeat().size();
                if(memberCnt < 0) memberCnt = 0;
//                traveller.setText(memberCnt.toString());

            }

            @Override
            public void onFailure(Call<GetAvailableSeatResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }

}
