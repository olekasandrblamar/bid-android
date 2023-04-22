package com.bid.app.ui.fragment.discover.transit.driver;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bid.app.R;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.GetABStopResponse;
import com.bid.app.model.response.GetAvailableRoutesResponse;
import com.bid.app.model.response.GetRouteByBusResponse;
import com.bid.app.model.response.StatusResponse;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.retrofit.APIClient;
import com.bid.app.service.GPSTracker;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.ScannerFragment;
import com.bid.app.ui.fragment.tripHistory.SOSFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ncorti.slidetoact.SlideToActView;
import com.wildma.idcardcamera.camera.IDCardCamera;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DriverFragment extends BaseFragment {
    private static final String TAG = DriverFragment.class.getSimpleName();
//    private static final int CAMERA_CODE = 0;

    SessionManager sessionManager;
    View root;
    GoogleMap mMap;
    ImageView bus, qr_code, face_recognition, sosBtn;
    ImageView ivProfile;

    LinearLayout pad;
    LinearLayout statusNote, stationPanel;
    RelativeLayout parentLayout;
    Button goOnlineBtn;
    TextView onlineStatus, onlineStatusDetail, fromStation, toStation;
    SlideToActView confirmSlide;
    private int _yDelta;

    Boolean isOnline, isFirstSlide;
    GetAvailableRoutesResponse availableRoute;

    GetRouteByBusResponse getRouteByBusResponse;
    private APIInterface apiInterface;
    GPSTracker gps;
    public CountDownTimer timer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_driver, container, false);
        sessionManager = new SessionManager(requireActivity());
        initIds(root, savedInstanceState);
        initFragment();
        return root;
    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode,resultCode,data);
//        if(resultCode==RESULT_OK && requestCode==CAMERA_CODE) {
//
//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
//
//                //if you want to encode the image into base64
//
//                if (imageBitmap != null) {
//                    try {
//                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
//                        File destination = new File(Environment.getExternalStorageDirectory(),"temp.jpg");
//                        FileOutputStream fo;
//                        fo = new FileOutputStream(destination);
//                        fo.write(bytes.toByteArray());
//                        fo.close();
//                        sendFileToServer( destination);
//                    }
//                    catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    //You can send your image to server
//                }
//
//
//        }
//    }

    private void initFragment() {
        Date date=new Date(); // today
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK); // 3
        String dayWeekText = new SimpleDateFormat("EEEE").format(date); //TUESDAY
        //dateTxt.setText(dayWeekText);
        getBusesRoute();
        Glide.with(requireActivity())
                .load(sessionManager.getUserImage())
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == IDCardCamera.RESULT_CODE) {
            final String path = IDCardCamera.getImagePath(data);
            if (!TextUtils.isEmpty(path)) {
                if (requestCode == IDCardCamera.TYPE_IDCARD_FRONT) {
                    scanFace(new File(path));
                }
            }
        }
    }

    public void scanFace(File file) {

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        CustomProgressDialog.getInstance().showDialog(getContext(), true);
        final Call<StatusResponse> call = apiInterface.scanTicketWithFace(sessionManager.getAccessToken(), body);
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();

                final StatusResponse statusResponse = response.body();
                ErrorStatus status = statusResponse.getError();
                String message = status.getMessage();
                if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {

                    AlertUtils.showAlerterSuccess(requireActivity(), message);
                } else {
                    AlertUtils.showAlerterFailure(requireActivity(), message);
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                t.printStackTrace();
//                CustomProgressDialog.getInstance().dismissDialog();
                CustomProgressDialog.getInstance().dismissDialog();

            }
        });
    }


    private void initIds(View view, Bundle savedInstanceState) {
        ((DashboardActivity) requireActivity()).setTitleAndImage("bus No: " + sessionManager.getUserId(), R.drawable.ic_back_arrow, false);
        bus = view.findViewById(R.id.bus);
        qr_code = view.findViewById(R.id.iv_qr_dashboard);
        face_recognition = view.findViewById(R.id.iv_face_dashboard);
        goOnlineBtn = view.findViewById(R.id.go_online_btn);
        onlineStatus = view.findViewById(R.id.online_status);
        onlineStatusDetail = view.findViewById(R.id.online_status_detail);
        //dateTxt = view.findViewById(R.id.date_txt);
        ivProfile = view.findViewById(R.id.iv_personal_profile);
        sosBtn = view.findViewById(R.id.iv_sos);
        confirmSlide = view.findViewById(R.id.confirm_slide);
        statusNote = view.findViewById(R.id.status_note);
        stationPanel = view.findViewById(R.id.station_panel);
        fromStation = view.findViewById(R.id.from_station);
        toStation = view.findViewById(R.id.to_station);
        parentLayout = view.findViewById(R.id.parent_layout);

        qr_code.setOnClickListener(this);
        face_recognition.setOnClickListener(this);
        goOnlineBtn.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        sosBtn.setOnClickListener(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        isOnline = false;
        pad = view.findViewById(R.id.pad);

        gps = new GPSTracker(getContext());
    }

    private void run() {


        pad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                printRoutes();
                final int Y = (int) motionEvent.getRawY();
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        _yDelta = Y - lParams.topMargin;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        RelativeLayout.LayoutParams mapLayoutParams = (RelativeLayout.LayoutParams)  root.findViewById(R.id.map_layout).getLayoutParams();
                        if(Y - _yDelta < 0) break;
                        int height= parentLayout.getHeight();
                        if(Y - _yDelta > height - Utils.convertDpToPixel(100,getContext())) break;
                        if(Y - _yDelta < height - Utils.convertDpToPixel(600,getContext())) break;
                        layoutParams.topMargin = Y - _yDelta;
//                        layoutParams.bottomMargin = -250;
                        view.setLayoutParams(layoutParams);

                        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(mMap.getCameraPosition().target, 13 + 3* (Y-_yDelta)/1000);

                        mMap.animateCamera(cu);
                        mapLayoutParams.height = (int) (Y - _yDelta + Utils.convertDpToPixel(20,getContext()));
                        root.findViewById(R.id.map_layout).setLayoutParams(mapLayoutParams);
                        break;
                }
                root.invalidate();
                return true;
            }
        });

        bus.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                mMap = googleMap;

                getAvailableRoute();
                if(gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    LatLng sydney = new LatLng(latitude, longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
                    mMap.addMarker(new MarkerOptions()
                            .position(sydney)
                            .title("My Position"));
                    if(sessionManager.getDriverOnline().equals("1")) {
                        startService();
                        setCurrentStatus(false);
                        if(sessionManager.getDriverTapTime()!= null && sessionManager.getDriverTapTime().length()>0) {
                            Long currentTime  = System.currentTimeMillis();
                            try {
                                Long savedTime = new Long(sessionManager.getDriverTapTime());
                                Long interval = 120000 - (currentTime - savedTime);
                                if(interval > 0) {

                                    int sec = (int) (interval / 1000);
                                    final int[] cnt = {sec};
                                    if(timer != null) timer.cancel();
                                    fromStation.setText(cnt[0] + "s");
//                                    confirmSlide.setLocked(true);
                                    confirmSlide.setCompleted(true, false);
                                    timer = new CountDownTimer(interval, 1000) {

                                        public void onTick(long millisUntilFinished) {
                                            cnt[0]--;
                                            fromStation.setText(cnt[0] + "s");
//                                    confirmSlide.setCompleteIcon();
                                        }

                                        public void onFinish() {
                                            setCurrentStatus(true);
                                            confirmSlide.setCompleted(false, true);
//                                            confirmSlide.setLocked(false);
                                        }
                                    }.start();
                                }
                            } catch (Exception e) {

                            }

                        }

                    }
                    else {
                        stopService();
                    }                    // \n is for new line
//                    Toast.makeText(getContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // Can't get location.
                    // GPS or network is not enabled.
                    // Ask user to enable GPS/network in settings.
                    gps.showSettingsAlert();
                }

            }
        });
        confirmSlide.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NonNull SlideToActView view) {

                slideAction();


            }
        });

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.bus:
                Bundle bundle = new Bundle();
                bundle.putSerializable(IBundle.BUNDLE_ROUTE, getRouteByBusResponse);
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new ShowBusSeatFragment(), R.id.frame_layout, true, false, true,bundle);
                break;
            case R.id.iv_qr_dashboard:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new ScannerFragment(), R.id.frame_layout, true, false, false, null);
                break;
            case R.id.iv_face_dashboard:
                IDCardCamera.create(this).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);
                break;
            case R.id.iv_personal_profile:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new DriverDetailFragment(), R.id.frame_layout, true, false, false, null);
                break;
            case R.id.iv_sos:
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
                        bundle.putString(IBundle.BUNDLE_BUS_ID , "0");
                        AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new SOSFragment(), R.id.frame_layout, true, false, true, bundle);

                    }
                });
                dialog.show();
                break;
            case R.id.go_online_btn:
                if(!isOnline) {
                    isOnline = true;
                    if(timer != null)
                    {
                        timer.cancel();
                        timer = null;
                        setCurrentStatus(true);
                        confirmSlide.setLocked(false);
                        confirmSlide.setCompleted(false, true);
                    }
                    startService();
                    setCurrentStatus(true);
                }else{
                    isOnline = false;
                    stopService();
                    if(timer != null)
                    {
                        timer.cancel();
                        timer = null;
                        setCurrentStatus(true);
                        confirmSlide.setLocked(false);
                        confirmSlide.setCompleted(false, true);
                    }
                }
                break;



        }
    }

    private void getAvailableRoute() {
        CustomProgressDialog.getInstance().showDialog(getContext(), true);
        Logger.e(TAG, "getAccessToken " + sessionManager.getAccessToken());

        Call<GetAvailableRoutesResponse> listResponseCall = apiInterface.getAvailableRoutesDB(sessionManager.getAccessToken());
        listResponseCall.enqueue(new Callback<GetAvailableRoutesResponse>() {
            @Override
            public void onResponse(Call<GetAvailableRoutesResponse> call, Response<GetAvailableRoutesResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                availableRoute = response.body();
                if (availableRoute != null) {
//                Routes routes = availablePathResponse.getRoutes();
//                    mMap.addMarker(new MarkerOptions()
//                            .position(sydney)
//                            .title("My Position"));
//                schedules = new ArrayList<JSONObject>();
//
//                for (int i = 0; i < availableRoute.getRoutes().size(); i++) {
//
//                    drawRoute((ArrayList) availableRoute.getRoutes().get(i).getBusStops());
////                    schedules.add(newSchedule("10:00","10:30","$5.0", "Lorem Bus Station"));
//                }
//                initScheduleAdapter(schedules);
//                    findShortestPath();
                } else {
                    Toast.makeText(getContext(), "Get Available Route Failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetAvailableRoutesResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }
//    private void sendFileToServer(File file) {
//        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("fileToUpload", file.getName(), requestBody);
//        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);
//
//        final Call<DocumentUploadResponseModel> call = apiInterface.scanDocument(DOC_UPLOAD_URL, DOC_UPLOAD_API_KEY, body);
//        call.enqueue(new Callback<DocumentUploadResponseModel>() {
//            @Override
//            public void onResponse(Call<DocumentUploadResponseModel> call, Response<DocumentUploadResponseModel> response) {
//                final DocumentUploadResponseModel documentUploadResponseModel = response.body();
//                CustomProgressDialog.getInstance().dismissDialog();
//
//                if (documentUploadResponseModel != null) {
//                    final String status = documentUploadResponseModel.getStatus();
//                    final String message = documentUploadResponseModel.getMessage();
//                    if (status.equalsIgnoreCase("success")) {
//                        AlertUtils.showAlerterSuccess(requireActivity(), message);
//                    } else {
//                        AlertUtils.showAlerterFailure(requireActivity(), message);
//                    }
//                } else {
//                    AlertUtils.showAlerterFailure(requireActivity(), "Some Error Occurred!");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DocumentUploadResponseModel> call, Throwable t) {
//                CustomProgressDialog.getInstance().dismissDialog();
//                t.printStackTrace();
//                AlertUtils.showAlerterFailure(requireActivity(), "Some Error Occurred!");
//            }
//        });
//    }
    private void startService() {
        isOnline = true;

        goOnlineBtn.setBackgroundResource(R.drawable.bg_btn_select);
        goOnlineBtn.setText("Go Offline");
        onlineStatus.setText("You are online");
        onlineStatusDetail.setText("You won't receive any ride requests.");
        stationPanel.setVisibility(View.VISIBLE);
        confirmSlide.setVisibility(View.VISIBLE);
        fromStation.setVisibility(View.VISIBLE);
        sessionManager.setDriverOnline("1");
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) pad.getLayoutParams();
        RelativeLayout.LayoutParams mapLayoutParams = (RelativeLayout.LayoutParams)  root.findViewById(R.id.map_layout).getLayoutParams();

        int height= parentLayout.getHeight();
        layoutParams.topMargin = (int) (height - Utils.convertDpToPixel(250,getContext()));
        pad.setLayoutParams(layoutParams);
        mapLayoutParams.height = (int) (height - Utils.convertDpToPixel(250,getContext()) + Utils.convertDpToPixel(20,getContext()));
        root.findViewById(R.id.map_layout).setLayoutParams(mapLayoutParams);

     //   statusNote.setVisibility(View.GONE);
        // Check if GPS enabled
        if(gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LatLng sydney = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
            mMap.addMarker(new MarkerOptions()
                    .position(sydney)
                    .title("My Position"));
            // \n is for new line
//            Toast.makeText(getContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }

    }

    private void stopService(){
        isOnline = false;
        goOnlineBtn.setBackgroundResource(R.drawable.bg_btn_gray);
        stationPanel.setVisibility(View.GONE);
        confirmSlide.setVisibility(View.GONE);
        fromStation.setVisibility(View.GONE);

        //   statusNote.setVisibility(View.VISIBLE);
        goOnlineBtn.setText("Go Online");
        onlineStatus.setText("You are offline");
        onlineStatusDetail.setText("You will receive ride requests soon ");
        sessionManager.setDriverOnline("0");
        int height= parentLayout.getHeight();

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) pad.getLayoutParams();
        RelativeLayout.LayoutParams mapLayoutParams = (RelativeLayout.LayoutParams)  root.findViewById(R.id.map_layout).getLayoutParams();
        layoutParams.topMargin = (int) (height - Utils.convertDpToPixel(250,getContext()));
        pad.setLayoutParams(layoutParams);
        mapLayoutParams.height = (int) (height - Utils.convertDpToPixel(250,getContext()) + Utils.convertDpToPixel(20,getContext()));
        root.findViewById(R.id.map_layout).setLayoutParams(mapLayoutParams);

//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) pad.getLayoutParams();
//        int height= parentLayout.getHeight();
//        layoutParams.topMargin = height - 350;
//        pad.setLayoutParams(layoutParams);

    }

    private void getBusesRoute() {

        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);

        final Call<GetRouteByBusResponse> call = apiInterface.getRouteByBus(sessionManager.getAccessToken(), sessionManager.getBusId(), "route");
        call.enqueue(new Callback<GetRouteByBusResponse>() {
            @Override
            public void onResponse(Call<GetRouteByBusResponse> call, Response<GetRouteByBusResponse> response) {
                getRouteByBusResponse = response.body();
                CustomProgressDialog.getInstance().dismissDialog();

                if (getRouteByBusResponse != null) {
                    final ErrorStatus status = getRouteByBusResponse.getErrorStatus();
                    if (status.getCode().equalsIgnoreCase("0")) {
//                        AlertUtils.showAlerterSuccess(requireActivity(), status.getMessage());
                        run();
                    } else {
                        AlertUtils.showAlerterWarning(requireActivity(), status.getMessage());
                    }
                } else {
                    AlertUtils.showAlerterFailure(requireActivity(), "Some Error Occurred!");
                }
            }

            @Override
            public void onFailure(Call<GetRouteByBusResponse> call, Throwable t) {
                CustomProgressDialog.getInstance().dismissDialog();
                t.printStackTrace();
                AlertUtils.showAlerterFailure(requireActivity(), "Some Error Occurred!");
            }
        });
    }

    public void slideAction() {
        if(timer != null)
        {
            timer.cancel();
            timer = null;
            confirmSlide.setCompleted(false, false);
            setCurrentStatus(true);
        }
        else {
            final Call<GetABStopResponse> call = apiInterface.slide(sessionManager.getAccessToken());
            call.enqueue(new Callback<GetABStopResponse>() {
                @Override
                public void onResponse(Call<GetABStopResponse> call, Response<GetABStopResponse> response) {
                    GetABStopResponse getABStopResponse = response.body();
                    if (getABStopResponse != null) {
                        ErrorStatus status = getABStopResponse.getErrorStatus();
                        if (status.getCode().equalsIgnoreCase("0")) {
                            if (getABStopResponse.getOngoing() != null && getABStopResponse.getOngoing().equals("0")) {
                                sessionManager.setDriverTapTime(new Long(System.currentTimeMillis()).toString());
                                final int[] cnt = {120};
                                if(timer != null) timer.cancel();
                                confirmSlide.setCompleted(true, false);
//                            confirmSlide.setLocked(true);
                                timer = new CountDownTimer(120000, 1000) {

                                    public void onTick(long millisUntilFinished) {
                                        cnt[0]--;
                                        fromStation.setText(cnt[0] + "s");
//                                    confirmSlide.setCompleteIcon();
                                    }

                                    public void onFinish() {
                                        setStatus(getABStopResponse, true);
//                                    confirmSlide.setLocked(false);
                                        confirmSlide.setCompleted(false, true);
                                    }
                                }.start();
                                return;
                            }
                        }
                    }
                    setStatus(getABStopResponse, true);

                    confirmSlide.setCompleted(false, true);

                }

                @Override
                public void onFailure(Call<GetABStopResponse> call, Throwable t) {
                    CustomProgressDialog.getInstance().dismissDialog();
                    t.printStackTrace();
                    AlertUtils.showAlerterFailure(requireActivity(), "Some Error Occurred!");
                }
            });
        }

    }
    public void setCurrentStatus(Boolean showFrom) {

        final Call<GetABStopResponse> call = apiInterface.getABStops(sessionManager.getAccessToken());
        call.enqueue(new Callback<GetABStopResponse>() {
            @Override
            public void onResponse(Call<GetABStopResponse> call, Response<GetABStopResponse> response) {
                GetABStopResponse getABStopResponse = response.body();
                setStatus(getABStopResponse, showFrom);

            }

            @Override
            public void onFailure(Call<GetABStopResponse> call, Throwable t) {
                t.printStackTrace();
                AlertUtils.showAlerterFailure(requireActivity(), "Some Error Occurred!");
            }
        });
    }


    public void setStatus(GetABStopResponse getABStopResponse, Boolean showFrom) {
        if (getABStopResponse != null) {
            ErrorStatus status = getABStopResponse.getErrorStatus();
            if (status.getCode().equalsIgnoreCase("0")) {
                if(showFrom) fromStation.setText("From: " + getABStopResponse.getPassedStop().getBusStop());
                toStation.setText("Toward: " + getABStopResponse.getTowardStop().getBusStop());
                if(getABStopResponse.getOngoing()!=null && getABStopResponse.getOngoing().equals("0")) {
                    confirmSlide.setText("Slide to start");
                    confirmSlide.setTextColor(Color.WHITE);
                }
                else if(getABStopResponse.getOngoing()!=null && getABStopResponse.getOngoing().equals("1")){
                    confirmSlide.setText("Slide to stop");
                    confirmSlide.setTextColor(Color.YELLOW);
                }
                else {
                    confirmSlide.setText("Slide to End");
                    confirmSlide.setTextColor(Color.RED);
                }
            } else {
                stopService();
            }
        } else {
            AlertUtils.showAlerterFailure(requireActivity(), "Some Error Occurred!");
        }
    }

}
