package com.bid.app.ui.fragment.discover.transit;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.TripListAdapter;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.request.TowardBusesRequest;
import com.bid.app.model.response.ATripResponse;
import com.bid.app.model.response.TowardBusesResponse;
import com.bid.app.model.view.BusLocation;
import com.bid.app.model.view.LatLngPosition;
import com.bid.app.model.view.Location;
import com.bid.app.model.view.Ticket;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.tripHistory.TicketDetailFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Utils;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InstructionFragment extends BaseFragment implements View.OnClickListener {

    public static String TICKET_UNAVILABLE = "-1";
    public static String TICKET_UNUSED = "0";
    public static String TICKET_USED = "1";
    public static String TICKET_EXPIRED = "2";
    public static String TICKET_CANCELED = "3";

    SessionManager sessionManager;
    private APIInterface apiInterface;
    RecyclerView scheduleList;
    TextView fromStation;
    TripListAdapter scheduleAdapter;
    AppCompatButton cancelBtn, completeBtn;
    LinearLayout panel;

    View root;

    String tripId;
    ATripResponse tripResponse;
    Handler handler;
    Runnable runnable;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_instructions, container, false);
        ((DashboardActivity) requireActivity()).setTitleAndImage("Trip", R.drawable.ic_back_arrow, true);
        sessionManager = new SessionManager(requireActivity());
        getBundle();
        initIds(root);
        getTrip();
        return root;
    }


    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            tripId = bundle.getString(IBundle.BUNDLE_TRIP_ID);
        }
    }

    private void initIds(View view) {
        //Maps Fragment layouts
//        searchBox = view.findViewById(R.id.search_edit_text);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        scheduleList = view.findViewById(R.id.schedule_listview);
        fromStation = view.findViewById(R.id.from_station);
        cancelBtn = view.findViewById(R.id.btn_cancel);
        completeBtn = view.findViewById(R.id.btn_complete);
        panel = view.findViewById(R.id.panel);
    }

    private void initFragment() {
        scheduleList.setLayoutManager(new LinearLayoutManager(getContext()));
        fromStation.setText( tripResponse.getFrom());
        fromStation.setSelected(true);

        completeBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        if(tripResponse.getAvailable().equals("0")) {
            panel.setVisibility(View.VISIBLE);
        }
        else panel.setVisibility(View.GONE);
        initAdapter();
    }

    @Override
    public void clickOnSchedule(int position, Ticket ticket) {
        super.clickOnSchedule(position, ticket);
//        AlertUtils.showAlerterWarning(requireActivity(), "Coming Soon!");
    }

    @Override
    public void clickTickets(int position, Ticket ticket, int type) {
        super.clickTickets(position, ticket, type);
        if(type == TripListAdapter.BUY_TICKET) {
            final SelectBusSeatFragment selectBusSeatFragment = new SelectBusSeatFragment(getContext(), ticket);
            selectBusSeatFragment.setListener(new SelectBusSeatFragment.EventListener() {
                @Override
                public void onSelect(int position) {
//                    ticket.setSeatNumber(new Integer(position).toString());
                    for(Ticket tc:tripResponse.getData()){
                        if(tc.getId().equals(ticket.getId())) {
                            tc.setSeatNumber(new Integer(position).toString());
                        }
                    }
                    scheduleAdapter.setList((ArrayList<Ticket>) tripResponse.getData());
                    scheduleAdapter.notifyDataSetChanged();
                    selectBusSeatFragment.dismiss();
                }
            });
            selectBusSeatFragment.show(getFragmentManager(), "dialog");
//
//            handler.removeCallbacks(runnable);
//            Bundle bundle=new Bundle();
//            bundle.putSerializable(IBundle.BUNDLE_TICKET, ticket);
//
//            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new SelectBusSeatFragment(), R.id.frame_layout, true, false, true, bundle);
        }
        else if(type == TripListAdapter.QRCODE) {
            final Bundle bundle1 = new Bundle();
            bundle1.putSerializable(IBundle.BUNDLE_SELECTED_PERSONAL_DETAILS, ticket);
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TicketFragment(), R.id.frame_layout, true, false, true, bundle1);
        }
        else if(type == TripListAdapter.SOS) {

        }
        else if(type == TripListAdapter.TICKET) {
            handler.removeCallbacks(runnable);
            Bundle bundle=new Bundle();
            bundle.putSerializable(IBundle.BUNDLE_TICKET, ticket);
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TicketDetailFragment(), R.id.frame_layout, true, false, true, bundle);
        }

    }



    private void initAdapter() {

        scheduleAdapter = new TripListAdapter(InstructionFragment.this.getContext(), (ArrayList<Ticket>) tripResponse.getData(), InstructionFragment.this);
        scheduleList.setAdapter(scheduleAdapter);
        scheduleAdapter.notifyDataSetChanged();
    }
    private void updateAdapter() {
        scheduleAdapter.setList((ArrayList<Ticket>) tripResponse.getData());
        scheduleAdapter.notifyDataSetChanged();
    }


    //Click listener interface
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.repeat_bus:
                Bundle bundle=new Bundle();
                bundle.putString("from_station", tripResponse.getFrom());
                bundle.putString("to_station", tripResponse.getTo());
                bundle.putString("fromLatLng", tripResponse.getFromLocation());
                bundle.putString("toLatLng", tripResponse.getToLocation());
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new ScheduleFragment(), R.id.frame_layout, false, false, true, bundle);
                break;
            case R.id.btn_cancel:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btn_complete:
                for(Ticket ticket: tripResponse.getData()) {
                    if(ticket.getMode().equals("driving")) {
                        if(ticket.getSeatNumber() ==null || ticket.getSeatNumber().length() == 0){
                            AlertUtils.showAlerterWarning(requireActivity(), "Please select seat");
                            return;
                        }

                    }
                }
                Bundle bundle1=new Bundle();
                //                    bundle1.putSerializable(IBundle.BUNDLE_SEAT_NUMBER, seatNumber.getText().toString());
                bundle1.putSerializable(IBundle.BUNDLE_TICKETS, tripResponse);
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new BuyTicketFragment(), R.id.frame_layout, true, false, true, bundle1);

                break;
        }
    }
    private void run() {
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                getBusLocation();
                handler.postDelayed(this, 3000);
            }
        };
        runnable.run();
    }
    public void getBusLocation() {
//        CustomProgressDialog.getInstance().showDialog(getContext(), true);
        TowardBusesRequest towardBusesRequest = new TowardBusesRequest();
        ArrayList<String> towardBusReqDatas = new ArrayList<>();
        for(Ticket ticket: tripResponse.getData()){
            towardBusReqDatas.add(ticket.getId());
        }
        towardBusesRequest.setData(towardBusReqDatas);
        Call<TowardBusesResponse> listResponseCall = apiInterface.getTowardBuses(sessionManager.getAccessToken(), towardBusesRequest);
        listResponseCall.enqueue(new Callback<TowardBusesResponse>() {
            @Override
            public void onResponse(Call<TowardBusesResponse> call, Response<TowardBusesResponse> response) {
//                CustomProgressDialog.getInstance().dismissDialog();
                TowardBusesResponse towardBusesResponse = response.body();
                if(towardBusesResponse == null) return;
                if(towardBusesResponse.getErrorStatus().getCode().equals("0")){
                    for(int i = 0; i < towardBusesResponse.getLocations().size(); i++ ) {
                        if(tripResponse.getData().get(i).getMode().equals("walking")) continue;
                        BusLocation busLocation = towardBusesResponse.getLocations().get(i);
                        tripResponse.getData().get(i).setBusNo(busLocation.getBus_no());
                        tripResponse.getData().get(i).setBusId(busLocation.getBus_id());
                        try{
                            LatLng busLatLng = new LatLng(new Double(busLocation.getLat()), new Double(busLocation.getLon()));
                            LatLngPosition location = tripResponse.getData().get(i).getStartStopLocation();
                            LatLng stopLatLng = Utils.LatLngPosition2LatLng(location);
                            tripResponse.getData().get(i).setEta(Math.ceil(Utils.getDistance(busLatLng, stopLatLng) / 1000) + " min");
                            updateAdapter();
                        } catch (Exception e) {

                        }

                    }
                }
                else{
                    AlertUtils.showAlerterWarning(requireActivity(), tripResponse.getErrorStatus().getMessage());
                }

            }

            @Override
            public void onFailure(Call<TowardBusesResponse> call, Throwable t) {
                t.printStackTrace();
//                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }
    public void getTrip() {

//        CustomProgressDialog.getInstance().showDialog(getContext(), true);
        //Get A Trip
        Call<ATripResponse> listResponseCall = apiInterface.getATrip(sessionManager.getAccessToken(), tripId);
        listResponseCall.enqueue(new Callback<ATripResponse>() {
            @Override
            public void onResponse(Call<ATripResponse> call, Response<ATripResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                tripResponse = response.body();
                if(tripResponse.getErrorStatus().getCode().equals("0")){
                    initFragment();
                    run();
                }
                else{
                    AlertUtils.showAlerterWarning(requireActivity(), tripResponse.getErrorStatus().getMessage());
                }

            }

            @Override
            public void onFailure(Call<ATripResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });

    }


}
