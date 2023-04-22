package com.bid.app.ui.fragment.tripHistory;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.ReviewListAdapter;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.request.CancelTicketRequest;
import com.bid.app.model.request.RateToUserRequest;
import com.bid.app.model.response.GetBusLocationResponse;
import com.bid.app.model.response.GetDriverProfileResponse;
import com.bid.app.model.response.GetReviewListResponse;
import com.bid.app.model.response.GetRouteByBusResponse;
import com.bid.app.model.response.GetTicketDetailResponse;
import com.bid.app.model.response.StatusResponse;
import com.bid.app.model.view.AvailableRoute;
import com.bid.app.model.view.CurrentPos;
import com.bid.app.model.view.GetReviewData;
import com.bid.app.model.view.RoutePath;
import com.bid.app.model.view.Ticket;
import com.bid.app.model.view.Waypoint;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.custom.EndlessRecyclerViewScrollListener;
import com.bid.app.ui.fragment.discover.transit.ScheduleFragment;
import com.bid.app.ui.fragment.discover.transit.TicketFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Constants;
import com.bid.app.util.ProjectionPathInfo;
import com.bid.app.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReviewsFragment extends BaseFragment {
    private static final String TAG = ReviewsFragment.class.getSimpleName();
    SessionManager sessionManager;
    private APIInterface apiInterface;

    RecyclerView recyclerView;
    RoundedImageView ivProfile;
    RatingBar ratingBar;
    TextView name;
    private EndlessRecyclerViewScrollListener scrollListener;

    String driverId;
    ReviewListAdapter reviewListAdapter;
    ArrayList<GetReviewData> getReviewDataArrayList;
    View root;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_reviews, container, false);
        sessionManager = new SessionManager(requireActivity());
        getBundle();
        initIds(root, savedInstanceState);
        initFragment(root);
        run();
        return root;
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            driverId =  bundle.getString(IBundle.BUNDLE_DRIVER_ID);
        }
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

        }
    }

    private void initFragment(View view) {

//        date.setText(ticketData.getPickDate().substring(0, 10));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(scrollListener);
        getReviewDataArrayList = new ArrayList<>();
        reviewListAdapter = new ReviewListAdapter(getContext(), getReviewDataArrayList,this);
        recyclerView.setAdapter(reviewListAdapter);
        loadNextDataFromApi(1);
    }

    private void initIds(View view, Bundle savedInstanceState) {
        ((DashboardActivity) requireActivity()).setTitleAndImage("Reviews", R.drawable.ic_back_arrow, false);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        recyclerView = view.findViewById(R.id.reviews);
        name = view.findViewById(R.id.name);
        ratingBar = view.findViewById(R.id.ratingBar);
        ivProfile = view.findViewById(R.id.iv_avatar_bid);

    }

    private void run() {
        getDriver();
    }

    public void loadNextDataFromApi(int offset) {
        getReviewDataArrayList.add(null);
        reviewListAdapter.setList(getReviewDataArrayList);
        scrollListener.setLoadingAnimation(true);
        reviewListAdapter.notifyItemInserted(getReviewDataArrayList.size() - 1);
        final Call<GetReviewListResponse> call = apiInterface.getReviewList(driverId,(new Integer(offset)).toString());
        call.enqueue(new Callback<GetReviewListResponse>() {
            @Override
            public void onResponse(Call<GetReviewListResponse> call, Response<GetReviewListResponse> response) {
                scrollListener.setLoadingAnimation(false);
                getReviewDataArrayList.remove(getReviewDataArrayList.size() - 1);
                reviewListAdapter.setList(getReviewDataArrayList);
                int scrollPosition = getReviewDataArrayList.size();
                reviewListAdapter.notifyItemRemoved(scrollPosition);

                final GetReviewListResponse getReviewListResponse = response.body();
                if(getReviewListResponse == null) {
                    AlertUtils.showAlerterWarning(requireActivity(), "connection failed");
                }
                else if(getReviewListResponse.getError().getCode().equals("0")) {
//                    AlertUtils.showAlerterSuccess(requireActivity(), getReviewListResponse.getError().getMessage());
                    for(GetReviewData getReviewData: getReviewListResponse.getData()){
                        getReviewDataArrayList.add(getReviewData);
                    }
                    reviewListAdapter.setList(getReviewDataArrayList);
                    reviewListAdapter.notifyDataSetChanged();

                }
                else{
                    AlertUtils.showAlerterWarning(requireActivity(), getReviewListResponse.getError().getMessage());
                }
            }

            @Override
            public void onFailure(Call<GetReviewListResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();

            }
        });


    }
    private void setDriverProfile(GetDriverProfileResponse driverProfileResponse) {
        Glide.with(requireActivity())
                .load(Constants.AWS_IMAGE_URL(driverProfileResponse.getData().getAttachment(), "jpg"))
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .skipMemoryCache(false)
                .centerCrop()
//                        .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(ivProfile);
        name.setText(driverProfileResponse.getData().getFirstName() + driverProfileResponse.getData().getLastName());
        if(driverProfileResponse.getData().getRating().length() > 0) ratingBar.setRating(new Float(driverProfileResponse.getData().getRating()));
    }
    private void getDriver() {
        final Call<GetDriverProfileResponse> call1 = apiInterface.getDriverProfile(sessionManager.getAccessToken(), driverId);
        call1.enqueue(new Callback<GetDriverProfileResponse>() {
            @Override
            public void onResponse(Call<GetDriverProfileResponse> call1, Response<GetDriverProfileResponse> response) {
                final GetDriverProfileResponse driverProfileResponse = response.body();
                if(driverProfileResponse!= null) setDriverProfile(driverProfileResponse);
            }
            @Override
            public void onFailure(Call<GetDriverProfileResponse> call1, Throwable t) {
                t.printStackTrace();

            }
        });

    }

}
