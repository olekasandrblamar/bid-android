package com.bid.app.ui.fragment.tripHistory;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bid.app.R;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.GetDirectionRoute;
import com.bid.app.model.request.CancelTicketRequest;
import com.bid.app.model.request.RateToUserRequest;
import com.bid.app.model.response.DirectionResults;
import com.bid.app.model.response.DirectionRoute;
import com.bid.app.model.response.GetBusLocationResponse;
import com.bid.app.model.response.GetDriverProfileResponse;
import com.bid.app.model.response.GetRouteByBusResponse;
import com.bid.app.model.response.GetTicketDetailResponse;
import com.bid.app.model.response.Location;
import com.bid.app.model.response.StatusResponse;
import com.bid.app.model.response.Steps;
import com.bid.app.model.view.AvailableRoute;
import com.bid.app.model.view.BusStop;
import com.bid.app.model.view.CurrentPos;
import com.bid.app.model.view.RoutePath;
import com.bid.app.model.view.Ticket;
import com.bid.app.model.view.Waypoint;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.activity.OnBoardingActivity;
import com.bid.app.ui.activity.ScheduleTestActivity;
import com.bid.app.ui.activity.SplashActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.discover.transit.InstructionFragment;
import com.bid.app.ui.fragment.discover.transit.ScheduleFragment;
import com.bid.app.ui.fragment.discover.transit.TicketFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Constants;
import com.bid.app.util.Logger;
import com.bid.app.util.ProjectionPathInfo;
import com.bid.app.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdate;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TicketDetailFragment extends BaseFragment {
    private static final String TAG = TicketDetailFragment.class.getSimpleName();
    Integer timeInterval = 3000;
    Integer drawInterval = 50;
    long gpsTime;
    SessionManager sessionManager;
    View root;
    GoogleMap mMap;
    private APIInterface apiInterface;

    String busId;
    Boolean isFirst, drawingInitialized;
    Marker marker, realBus;
    PolylineOptions lineOptions;
    ArrayList<LatLng> point_array;
    ProjectionPathInfo endPathInfo;
    LatLng currentBusPos, previousBusPos;

    Button giveRating, reviews;
    TextView fromStation, toStation, pickupTime, dropoffTime;
    RoundedImageView ivProfile;
    RatingBar ratingBar;
    Ticket ticketData;
    ImageView sosBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_ticket_detail, container, false);
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
            ticketData = (Ticket) bundle.getSerializable(IBundle.BUNDLE_TICKET);
            busId = ticketData.getBusId();
        }
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.repeat_bus:
                Bundle bundle=new Bundle();
                bundle.putString("from_station", ticketData.getStartStop());
                bundle.putString("fromLatLng", ticketData.getStartStopLocation().getLat() + "," +ticketData.getStartStopLocation().getLng());
                bundle.putString("to_station", ticketData.getEndStop());
                bundle.putString("toLatLng", ticketData.getEndStopLocation().getLat() + "," +ticketData.getEndStopLocation().getLng());

                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new ScheduleFragment(), R.id.frame_layout, true, false, true, bundle);

                break;
            case R.id.qr_code:

                final Bundle bundle1 = new Bundle();
                bundle1.putSerializable(IBundle.BUNDLE_SELECTED_PERSONAL_DETAILS, ticketData);
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TicketFragment(), R.id.frame_layout, true, false, true, bundle1);
                break;
            case R.id.sos_btn:
                final Dialog dialog = new Dialog(requireActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_confirm_sos);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(true);

                LinearLayout call = dialog.findViewById(R.id.call);
                LinearLayout email = dialog.findViewById(R.id.email);

                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Intent intentDial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "911"));
                        startActivity(intentDial);
                    }
                });
                email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Bundle bundle=new Bundle();
                        bundle.putString(IBundle.BUNDLE_BUS_ID , ticketData.getId());
                        AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new SOSFragment(), R.id.frame_layout, true, false, true, bundle);

                    }
                });
                dialog.show();
                break;
            case R.id.cancel_ticket:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

                // Setting Dialog Title
                alertDialog.setTitle("Cancel Ticket");

                // Setting Dialog Message
                alertDialog.setMessage("If you cancel ticket you get penalty. Do you really want to cancel Ticket?");

                // On pressing the Settings button.
                alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        CancelTicketRequest cancelTicketRequest = new CancelTicketRequest();
                        cancelTicketRequest.setTicketId(ticketData.getId());
                        final Call<StatusResponse> call1 = apiInterface.cancelTicket(sessionManager.getAccessToken(), cancelTicketRequest);
                        call1.enqueue(new Callback<StatusResponse>() {
                            @Override
                            public void onResponse(Call<StatusResponse> call1, Response<StatusResponse> response) {
                                final StatusResponse statusResponse = response.body();
                                if(statusResponse == null) {
                                    AlertUtils.showAlerterWarning(requireActivity(), "connection failed");
                                }
                                else if(statusResponse.getError().getCode().equals("0")) {
                                    AlertUtils.showAlerterSuccess(requireActivity(), statusResponse.getError().getMessage());
                                    getDocuments();
                                }
                                else{
                                    AlertUtils.showAlerterWarning(requireActivity(), statusResponse.getError().getMessage());
                                }
                            }
                            @Override
                            public void onFailure(Call<StatusResponse> call, Throwable t) {
                                t.printStackTrace();
                                CustomProgressDialog.getInstance().dismissDialog();
                            }
                        });
                    }
                });

                // On pressing the cancel button
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
                break;
            case R.id.give_rating:
                final Call<GetDriverProfileResponse> call1 = apiInterface.getDriverProfile(sessionManager.getAccessToken(), ticketData.getDriverId());
                call1.enqueue(new Callback<GetDriverProfileResponse>() {
                    @Override
                    public void onResponse(Call<GetDriverProfileResponse> call1, Response<GetDriverProfileResponse> response) {
                        final GetDriverProfileResponse driverProfileResponse = response.body();

                        final Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.dialog_rating);
                        final RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
                        TextView name = dialog.findViewById(R.id.name);
                        TextView ratingStatus = dialog.findViewById(R.id.rate_status);
                        TextView ratingCnt = dialog.findViewById(R.id.rating_cnt);
                        EditText comment = dialog.findViewById(R.id.comment);
                        final RoundedImageView ivProfile = dialog.findViewById(R.id.iv_avatar_bid);

                        TextView mDialogNo = dialog.findViewById(R.id.confirm);

                        name.setText(driverProfileResponse.getData().getFirstName() + " " + driverProfileResponse.getData().getLastName());
                        ratingBar.setRating(new Float(driverProfileResponse.getData().getRating()));
                        ratingStatus.setText(driverProfileResponse.getData().getRating());
                        ratingCnt.setText("(" + driverProfileResponse.getData().getRatingCnt()+")");
                        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                            @Override
                            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                                ratingStatus.setText((new Float(ratingBar.getRating())).toString());
                            }
                        });
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(requireActivity())
                                        .load(Constants.AWS_IMAGE_URL(driverProfileResponse.getData().getAttachment(), "jpg"))
//                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                                    .skipMemoryCache(false)
                                        .centerCrop()
//                                    .placeholder(R.drawable.ic_user)
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
                            }
                        });

                        mDialogNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                CustomProgressDialog.getInstance().showDialog(requireActivity(), true);
                                RateToUserRequest request = new RateToUserRequest();
                                request.setUserId(ticketData.getDriverId());
                                Float rate = ratingBar.getRating();
                                request.setRating(rate.toString());
                                request.setTicketId(ticketData.getId());
                                request.setComment(comment.getText().toString());
                                final Call<StatusResponse> call = apiInterface.rateToUser(sessionManager.getAccessToken(), request);
                                call.enqueue(new Callback<StatusResponse>() {
                                    @Override
                                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                                        CustomProgressDialog.getInstance().dismissDialog();
                                        final StatusResponse statusResponse = response.body();
                                        if(statusResponse == null) {
                                            AlertUtils.showAlerterWarning(requireActivity(), "connection failed");
                                        }
                                        else if(statusResponse.getError().getCode().equals("0")) {
                                            AlertUtils.showAlerterSuccess(requireActivity(), statusResponse.getError().getMessage());
                                            getDocuments();
                                        }
                                        else{
                                            AlertUtils.showAlerterWarning(requireActivity(), statusResponse.getError().getMessage());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                                        t.printStackTrace();
                                        CustomProgressDialog.getInstance().dismissDialog();

                                    }
                                });

                            }
                        });
                        dialog.show();
                    }
                    @Override
                    public void onFailure(Call<GetDriverProfileResponse> call1, Throwable t) {
                        t.printStackTrace();

                    }
                });
                break;
            case R.id.reviews:
                Bundle bundle2 = new Bundle();
                bundle2.putString(IBundle.BUNDLE_DRIVER_ID, ticketData.getDriverId());
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(),new ReviewsFragment(),  R.id.frame_layout, true, false, true, bundle2);

                break;

        }
    }

    private void getDocuments() {
        Call<GetTicketDetailResponse> listResponseCall = apiInterface.getTicketDetail(sessionManager.getAccessToken(), ticketData.getId());
        listResponseCall.enqueue(new Callback<GetTicketDetailResponse>() {
            @Override
            public void onResponse(Call<GetTicketDetailResponse> call, Response<GetTicketDetailResponse> response) {
                Date date = new Date();
                gpsTime = date.getTime();
                GetTicketDetailResponse getTicketResponse = response.body();
                if(getTicketResponse == null) return;
                ticketData = getTicketResponse.getTicket();
                initFragment(root);
            }

            @Override
            public void onFailure(Call<GetTicketDetailResponse> call, Throwable t) {
                t.printStackTrace();
                //            CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }
    private void initFragment(View view) {

//        date.setText(ticketData.getPickDate().substring(0, 10));

        fromStation.setText(ticketData.getStartStop());
        fromStation.setSelected(true);
        toStation.setText(ticketData.getEndStop());
        toStation.setSelected(true);
        ratingBar.setRating(new Float(ticketData.getDriverRating()));

        final Call<GetDriverProfileResponse> call1 = apiInterface.getDriverProfile(sessionManager.getAccessToken(), ticketData.getDriverId());
        call1.enqueue(new Callback<GetDriverProfileResponse>() {
            @Override
            public void onResponse(Call<GetDriverProfileResponse> call1, Response<GetDriverProfileResponse> response) {
                final GetDriverProfileResponse driverProfileResponse = response.body();
                if(driverProfileResponse.getData() == null) return;
                Glide.with(requireActivity())
                        .load(Constants.AWS_IMAGE_URL(driverProfileResponse.getData().getAttachment(), "jpg"))
//                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                                    .skipMemoryCache(false)
                        .centerCrop()
//                                    .placeholder(R.drawable.ic_user)
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
            }
            @Override
            public void onFailure(Call<GetDriverProfileResponse> call1, Throwable t) {
                t.printStackTrace();

            }
        });

    }


    private void initIds(View view, Bundle savedInstanceState) {
        ((DashboardActivity) requireActivity()).setTitleAndImage("bus No: " + sessionManager.getUserId(), R.drawable.ic_back_arrow, false);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        fromStation = view.findViewById(R.id.from_station);
        toStation = view.findViewById(R.id.to_station);
        ratingBar = view.findViewById(R.id.ratingBar);
        ivProfile = view.findViewById(R.id.iv_avatar_bid);
        giveRating = view.findViewById(R.id.give_rating);
        reviews = view.findViewById(R.id.reviews);
        sosBtn = view.findViewById(R.id.sos_btn);
        giveRating.setOnClickListener(this);
        reviews.setOnClickListener(this);
        sosBtn.setOnClickListener(this);
    }

    private void run() {

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                mMap = googleMap;

                getRouteByBus();
                getBusLocation();
            }
        });
//Declare the timer
        drawingInitialized = false;
        isFirst = true;
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                getBusLocation();
                handler.postDelayed(this, timeInterval);
            }
        };
        runnable.run();

        //drawing
        Handler handler1 = new Handler();
        Runnable runnable1 = new Runnable() {
            public void run() {
                if(!drawingInitialized){
                    handler1.postDelayed(this, drawInterval);
                    return;
                }
                if(true) {
                    if(previousBusPos == null){
                        handler1.postDelayed(this, drawInterval);
                        return;
                    }
                    Date date = new Date();
                    long now = date.getTime();
                    double alpha = (now - gpsTime) / (double)timeInterval;
                    double newLat = (alpha * currentBusPos.latitude + (1 - alpha) * previousBusPos.latitude);
                    double newLon = (alpha * currentBusPos.longitude + (1 - alpha) * previousBusPos.longitude);
                    LatLng latLng = new LatLng(newLat, newLon);
                    marker.setPosition(latLng);
                    if(Math.hypot(currentBusPos.longitude - previousBusPos.longitude,currentBusPos.latitude - previousBusPos.latitude) > 0.0000001) {
                        float pi= 3.141592f;
                        float sign = 1;
                        if(currentBusPos.latitude - previousBusPos.latitude > 0) sign = -1;
                        float angle = pi / 2 + sign * (float) Math.acos((currentBusPos.longitude - previousBusPos.longitude) / Math.hypot(currentBusPos.longitude - previousBusPos.longitude,currentBusPos.latitude - previousBusPos.latitude));
                        marker.setRotation(angle * 180 / pi);
                    }
                }
                else {
                    ///////////////////////Move along road/////////////////////////////////////
                    ProjectionPathInfo projectionPathInfo = Utils.getProjectionPoint(point_array, marker.getPosition());
                    double tDistance = Utils.getDistance(point_array, projectionPathInfo, endPathInfo);

                    Date date = new Date();
                    long now = date.getTime();
                    double rDistance = 0;
                    if(tDistance>0) rDistance = tDistance * (now - gpsTime) / timeInterval +
                            Utils.getDistance(point_array.get(projectionPathInfo.getPathId()),
                                    point_array.get(projectionPathInfo.getPathId() + 1))
                                    *  projectionPathInfo.delta;
                    else rDistance = tDistance * (now - gpsTime) / timeInterval +
                            Utils.getDistance(point_array.get(projectionPathInfo.getPathId()),
                                    point_array.get(projectionPathInfo.getPathId() + 1))
                                    *  (1 - projectionPathInfo.delta);
                    double currentDistance = 0;
                    LatLng markerPos;
                    if(tDistance > 0) {
                        markerPos = point_array.get(endPathInfo.getPathId() + 1);
                        for(int i = projectionPathInfo.getPathId(); i <= endPathInfo.getPathId(); i++) {
                            double pathLength = Utils.getDistance(point_array.get(i), point_array.get(i + 1));
                            if(currentDistance + pathLength> rDistance){
                                double delta = (rDistance - currentDistance) / pathLength;
                                markerPos = Utils.getPosition(point_array.get(i), point_array.get(i + 1), delta);
                                break;
                            }
                            else currentDistance += pathLength;
                        }
                    }
                    else{
                        markerPos = point_array.get(endPathInfo.getPathId());
                        for(int i = projectionPathInfo.getPathId(); i >= endPathInfo.getPathId(); i--) {
                            double pathLength = Utils.getDistance(point_array.get(i), point_array.get(i + 1));
                            if(currentDistance + pathLength> rDistance){
                                double delta = (rDistance - currentDistance) / pathLength;
                                markerPos = Utils.getPosition(point_array.get(i), point_array.get(i + 1), 1 - delta);
                                break;
                            }
                            else currentDistance += pathLength;
                        }
                    }
                    marker.setPosition(markerPos);
                    //////////////////end of move load//////////////
                }
                handler1.postDelayed(this, drawInterval);

            }
        };
        runnable1.run();

//        Timer t2 = new Timer();
//        t2.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//
//
//            }
//
//        }, 0, drawInterval);
    }



    private void getRouteByBus() {
        if(busId == null || busId.length() == 0) return;
        CustomProgressDialog.getInstance().showDialog(getContext(), true);
        Call<GetRouteByBusResponse> listResponseCall = apiInterface.getRouteByBus(sessionManager.getAccessToken(), busId, "route");
        listResponseCall.enqueue(new Callback<GetRouteByBusResponse>() {
            @Override
            public void onResponse(Call<GetRouteByBusResponse> call, Response<GetRouteByBusResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                GetRouteByBusResponse getRouteByBusResponse = response.body();
                AvailableRoute data = getRouteByBusResponse.getData();
//                fetchRoutePath(data);
                if(data!=null) drawRoute(data);
            }

            @Override
            public void onFailure(Call<GetRouteByBusResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });

    }

    private void getBusLocation() {
        if(busId == null || busId.length() == 0) return;
        Call<GetBusLocationResponse> listResponseCall = apiInterface.getBusLocationFake(sessionManager.getAccessToken(), busId);
        listResponseCall.enqueue(new Callback<GetBusLocationResponse>() {
            @Override
            public void onResponse(Call<GetBusLocationResponse> call, Response<GetBusLocationResponse> response) {
                Date date = new Date();
                gpsTime = date.getTime();
                GetBusLocationResponse getBusLocationResponse = response.body();
                if(getBusLocationResponse == null) return;
                CurrentPos busLocation = getBusLocationResponse.getCurrentPos();
                previousBusPos = currentBusPos;
                currentBusPos = new LatLng(busLocation.getLat(), busLocation.getLon());
                if(isFirst) {
                    isFirst = false;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentBusPos, 13));
                    marker = mMap.addMarker(new MarkerOptions()
                            .position(currentBusPos)
                            .title("No:" + busId)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_top)));
//                    realBus = mMap.addMarker(new MarkerOptions()
//                            .position(currentBusPos)
//                            .title("bus")
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_bus_png)));
                } else {
                    drawingInitialized = true;
//                    setBusMovement(marker, currentBusPos);
//                    realBus.setPosition(currentBusPos);
                }


            }

            @Override
            public void onFailure(Call<GetBusLocationResponse> call, Throwable t) {
                t.printStackTrace();
    //            CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }

    void setBusMovement(Marker marker, LatLng newLatLng) {
        if (marker == null) {
            return;
        }
        if(lineOptions == null) return;
        endPathInfo = Utils.getProjectionPoint(point_array, newLatLng);
        drawingInitialized = true;
        if(drawingInitialized) return;
        ProjectionPathInfo newBusPosProjectionPathInfo =  Utils.getProjectionPoint(point_array, newLatLng);
        ProjectionPathInfo currentBusProjectionPathInfo =  Utils.getProjectionPoint(point_array, currentBusPos);
        double speed = Utils.getDistance(point_array, currentBusProjectionPathInfo, newBusPosProjectionPathInfo);
        // find target pos;
        double targetDistance = Utils.getDistance(point_array.get(newBusPosProjectionPathInfo.getPathId()), point_array.get(newBusPosProjectionPathInfo.getPathId() + 1)) * newBusPosProjectionPathInfo.getDelta() + speed;
        double currentDistance = 0;
        ProjectionPathInfo targetPathInfo = new ProjectionPathInfo(0,0.0);
        for(int i = newBusPosProjectionPathInfo.getPathId(); i < point_array.size() - 1; i++) {
            double pathLength = Utils.getDistance(point_array.get(i), point_array.get(i + 1));
            if(currentDistance + pathLength> targetDistance){
                targetPathInfo.setDelta((targetDistance - currentDistance) / pathLength);
                targetPathInfo.setPathId(i);
                break;
            }
            else currentDistance += pathLength;
        }
        ProjectionPathInfo currentMarkProjectionPathInfo = Utils.getProjectionPoint(point_array, marker.getPosition());
        endPathInfo = targetPathInfo;
        drawingInitialized = true;

//        currentBusPos = newLatLng;

//        ValueAnimator animation = ValueAnimator.ofFloat(0f, 100f);
//        final float[] previousStep = {0f};
//        double deltaLatitude = newLatLng.latitude - marker.getPosition().latitude;
//        double deltaLongitude = newLatLng.longitude - marker.getPosition().longitude;
//        animation.setDuration(timeInterval * 2);
//        animation.addUpdateListener(animation1 -> {
//            float deltaStep = (Float) animation1.getAnimatedValue() - previousStep[0];
//            previousStep[0] = (Float) animation1.getAnimatedValue();
//            marker.setPosition(new LatLng(marker.getPosition().latitude + deltaLatitude * deltaStep * 1 / 100, marker.getPosition().longitude + deltaStep * deltaLongitude * 1 / 100));
//        });
//        animation.start();

    }

    public static class RouteDecode {

        public static ArrayList<LatLng> decodePoly(String encoded) {
            ArrayList<LatLng> poly = new ArrayList<LatLng>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;
            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;
                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
                poly.add(position);
            }
            return poly;
        }
    }
//////////////////////////////////////

    public void drawRoute(AvailableRoute availableRoute) {
        for(Waypoint waypoint: availableRoute.getWaypoints()) {
            LatLng latLng = new LatLng(new Double(waypoint.getPosition().getLat()), new Double(waypoint.getPosition().getLng()));
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(waypoint.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop_blue)));

        }
        for(int i = 0; i < availableRoute.getPaths().size(); i++) {
            RoutePath routePath = availableRoute.getPaths().get(i);
            LatLng latLngSt = Utils.getLatLngArray(routePath).get(0);
            for(LatLng latLng:Utils.getLatLngArray(routePath)) {
                LatLng latLngEd = latLng;
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(latLngSt, latLngEd)
                        .width(5)
                        .color(Color.argb(100, 50,50,255)));
                latLngSt = latLngEd;
            }

        }

    }


//////////////////////////////////

}
