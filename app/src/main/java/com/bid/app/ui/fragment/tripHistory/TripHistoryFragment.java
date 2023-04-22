package com.bid.app.ui.fragment.tripHistory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.bid.app.R;
import com.bid.app.adaper.ViewPagerTicketHistoryAdapter;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.model.response.FamilyMembersResponse;
import com.bid.app.model.response.GetTripHistoryResponse;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.FamilyData;
import com.bid.app.model.view.Trip;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;
import com.google.android.material.tabs.TabLayout;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripHistoryFragment extends BaseFragment {

    private static final String TAG = TripHistoryFragment.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;


    private SessionManager sessionManager;
    private APIInterface apiInterface;
    Spinner spinner;
    List<FamilyData> familyDataList;

//    private AppCompatButton btnAddIdDocuments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ticket_history, container, false);

        ((DashboardActivity) requireActivity()).setTitleAndImage("Trip History", R.drawable.ic_back_arrow, true);

        initController(view);
        getFamilyMembers();

        return view;

    }

    private void initController(View views) {
        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        tabLayout = views.findViewById(R.id.tab_layout);
        viewPager = views.findViewById(R.id.view_pager);
        spinner = views.findViewById(R.id.spinner);

//        btnAddIdDocuments = views.findViewById(R.id.btn_add_id_documents);

//        btnAddIdDocuments.setOnClickListener(this);

        tabLayout.setupWithViewPager(viewPager);

    }


    private void initViewPagerAdapter(ArrayList<Trip> allTrip, ArrayList<Trip> upcomingTrip, ArrayList<Trip> pastTrip) {
        final ViewPagerTicketHistoryAdapter viewPagerIdDocumentsAdapter = new ViewPagerTicketHistoryAdapter(getChildFragmentManager(),allTrip, upcomingTrip, pastTrip);
        viewPager.setAdapter(viewPagerIdDocumentsAdapter);
        tabLayout.setupWithViewPager(viewPager);

//        viewPager.invalidate();
        viewPagerIdDocumentsAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
//
//            case R.id.btn_add_id_documents:
//                //FragmentSelectIdDocument
//                final Bundle bundle = new Bundle();
//                bundle.putString(IBundle.BUNDLE_FRAGMENT_FROM, Constants.ADD_DOCUMENT_FRAGMENT);
//                AppActivityManager.redirectTo((AppCompatActivity) Objects.requireNonNull(requireActivity()), new CountryListFragment(TicketHistoryFragment.this), R.id.frame_layout, true, false, true, bundle);
//                break;

        }
    }

    private void getDocuments() {
        String bid = "";
        if(spinner.getSelectedItemPosition() > 0){
            FamilyData familyData = familyDataList.get(spinner.getSelectedItemPosition() -1);
            bid = familyData.getId();
        }
        final Call<GetTripHistoryResponse> call = apiInterface.getTripList(sessionManager.getAccessToken(), bid);
        call.enqueue(new Callback<GetTripHistoryResponse>() {
            @Override
            public void onResponse(Call<GetTripHistoryResponse> call, Response<GetTripHistoryResponse> response) {
                final GetTripHistoryResponse ticketHistoryResponse = response.body();

                if (ticketHistoryResponse != null) {
                    final ErrorStatus status = ticketHistoryResponse.getError();
                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        ArrayList<Trip> allTicketsData = (ArrayList<Trip>) ticketHistoryResponse.getAll();
                        ArrayList<Trip> upcomingTicketsData = new ArrayList<>();
                        ArrayList<Trip> pastTicketsData = new ArrayList<>();
                        for(Trip trip:allTicketsData) {
                            if(trip.getType().equals("upcoming")) upcomingTicketsData.add(trip);
                            else pastTicketsData.add(trip);
                        }
                        initViewPagerAdapter(allTicketsData, upcomingTicketsData, pastTicketsData);
                    } else {
                        AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                    }
                }

//                initViewPagerAdapter();

                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });

            }

            @Override
            public void onFailure(Call<GetTripHistoryResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();


            }
        });
    }

    private void getFamilyMembers() {
//        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);
        final Call<FamilyMembersResponse> call = apiInterface.listFamilyMembers(sessionManager.getAccessToken(), "");
        call.enqueue(new Callback<FamilyMembersResponse>() {
            @Override
            public void onResponse(Call<FamilyMembersResponse> call, Response<FamilyMembersResponse> response) {

                CustomProgressDialog.getInstance().dismissDialog();
                if (response.body() != null) {
                    final FamilyMembersResponse familyMembersResponse = response.body();
                    final ErrorStatus status = familyMembersResponse.getErrorStatus();

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        familyDataList = familyMembersResponse.getData();
                        ArrayList<String> list = new ArrayList<>();
                        list.add("Me");
                        for(FamilyData familyData: familyDataList) {
                            list.add(familyData.getRelationship());
                        }

                        ArrayAdapter adapter = new ArrayAdapter<String>( getContext(), R.layout.support_simple_spinner_dropdown_item, list);
                        spinner.setAdapter(adapter);

                        getDocuments();
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                getDocuments();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    } else {
                        AlertUtils.showAlerterWarning(requireActivity(), status.getMessage());
                    }

                } else {
                    Logger.e(TAG, "response.body() == null");
                }
            }

            @Override
            public void onFailure(Call<FamilyMembersResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }

}
