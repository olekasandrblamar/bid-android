package com.bid.app.ui.fragment.tripHistory;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.TripHistoryAdapter;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.request.BookATripRequest;
import com.bid.app.model.response.ATripResponse;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Step;
import com.bid.app.model.view.Ticket;
import com.bid.app.model.view.Trip;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.IdDocuments.CurrentIdDocumentsFragment;
import com.bid.app.ui.fragment.discover.transit.InstructionFragment;
import com.bid.app.ui.fragment.discover.transit.ScheduleFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class TripHistoryPartFragment extends BaseFragment {

    private static final String TAG = CurrentIdDocumentsFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private TextView no_records_tv;
    private ArrayList<Trip> trips = new ArrayList<>();

    private SessionManager sessionManager;
    private APIInterface apiInterface;

    public TripHistoryPartFragment(ArrayList<Trip> trips) {
        this.trips = trips;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_current_id_documents, container, false);


        initController(view);

        return view;

    }
    private void initController(View views) {

        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        recyclerView = views.findViewById(R.id.recycler_view);
        no_records_tv = views.findViewById(R.id.no_records_tv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DashboardActivity.context);
        recyclerView.setLayoutManager(linearLayoutManager);
        initAdapter(trips);
    }


    private void initAdapter(final List<Trip> list) {
        Activity activity = getActivity();
        if (activity != null) {
            if (trips.size() > 0) {
                TripHistoryAdapter ticketHistoryAdapter = new TripHistoryAdapter(requireActivity(), (ArrayList<Trip>) list,  TripHistoryPartFragment.this);
                recyclerView.setAdapter(ticketHistoryAdapter);

                recyclerView.setVisibility(View.VISIBLE);
                no_records_tv.setVisibility(View.GONE);

            } else {
                recyclerView.setVisibility(View.GONE);
                no_records_tv.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void clickTrip(int position, Trip trip, int type) {
        super.clickTrip(position, trip, type);
        if(type == TripHistoryAdapter.TRIP) {

            Bundle bundle=new Bundle();
            bundle.putString(IBundle.BUNDLE_TRIP_ID, trip.getId());
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new InstructionFragment(), R.id.frame_layout, true, false, true, bundle);

//
//            bundle.putSerializable(IBundle.BUNDLE_TICKET , trip);
//            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TicketDetailFragment(), R.id.frame_layout, true, false, true, bundle);
       }
        else  if(type == TripHistoryAdapter.REPEAT_TRIP) {
            Bundle bundle=new Bundle();
            bundle.putString("from_station", trip.getFrom());
            bundle.putString("to_station", trip.getTo());
            bundle.putString("fromLatLng", trip.getFromLocation());
            bundle.putString("toLatLng", trip.getToLocation());
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new ScheduleFragment(), R.id.frame_layout, true, false, true, bundle);

        }

    }

    public void goMethod1Instruction(Trip trip) {

        BookATripRequest bookATripRequest = new BookATripRequest();
        bookATripRequest.setFrom(trip.getFrom());
        bookATripRequest.setFromLocation(trip.getFromLocation());
        bookATripRequest.setTo(trip.getTo());

        bookATripRequest.setToLocation(trip.getToLocation());
        ArrayList<Step> steps = new ArrayList<>();
        for(Ticket ticket:trip.getTickets()) {
            Step step = new Step();
            step.setMode(ticket.getMode());
            step.setDistance(ticket.getDistance());
            step.setRouteId(ticket.getRouteId());
            step.setTo(ticket.getEndStop());
            step.setPolyline(ticket.getPolyline());
            steps.add(step);
        }
        bookATripRequest.setSteps(steps);
        final Call<ATripResponse> call = apiInterface.bookATrip(sessionManager.getAccessToken(), bookATripRequest);
        call.enqueue(new Callback<ATripResponse>() {
            @Override
            public void onResponse(Call<ATripResponse> call, Response<ATripResponse> response) {

                final ATripResponse body = response.body();
                final ErrorStatus status = body.getErrorStatus();

                if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                    Bundle bundle=new Bundle();
                    bundle.putString(IBundle.BUNDLE_TRIP_ID, body.getTripId());
                    AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new InstructionFragment(), R.id.frame_layout, true, false, true, bundle);
                } else {
                    AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
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
