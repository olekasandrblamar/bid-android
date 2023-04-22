package com.bid.app.ui.activity;

import static com.bid.app.ui.activity.ScheduleTestActivity.FREE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.bid.app.R;
import com.bid.app.databinding.ActivityScheduleMapBinding;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.BookScheduleTest;
import com.bid.app.model.response.BookScheduleTestResponse;
import com.bid.app.model.response.CardListInfo;
import com.bid.app.model.response.Questionnaire;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;
import com.bid.app.util.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleMapActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityScheduleMapBinding binding;
    private static final String TAG = ScheduleMapActivity.class.getSimpleName();
    private SessionManager sessionManager;
    private APIInterface apiInterface;
    private GoogleMap googleMap;
    private String location_id = "";
    private String Island_id = "";
    private String Reg_date = "";
    private String Test_type_id = "";
    private String name_for_map;
    private Double lat_for_map;
    private Double long_for_map;
    private Questionnaire questionnaire;
    private String medical_provider_id;
    private String time_slot_time;
    private String time_slot_free;
    private String time_slot_paid;
    private String time_slot_holiday;
    private CardListInfo cardListInfo;
    private String test_price;
    private Bundle bundle;
    private String payment_type;
    private String island_name;
    private String medical_provider_name;
    private String location_name;
    private String test_type_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundle();
        initialView();
    }

    private void getBundle() {
        bundle = getIntent().getExtras();

        Reg_date = bundle.getString("reg_date");
        Island_id = bundle.getString("island_id");
        island_name = bundle.getString("island_name");
        medical_provider_id = bundle.getString("medical_provider_id");
        medical_provider_name = bundle.getString("medical_provider_name");
        location_id = bundle.getString("location_id");
        location_name = bundle.getString("location_name");
        Test_type_id = bundle.getString("test_type_id");
        test_type_name = bundle.getString("test_type_name");
        test_price = bundle.getString("test_price");

        time_slot_time = bundle.getString("time_slot_time");
        time_slot_free = bundle.getString("time_slot_free");
        time_slot_paid = bundle.getString("time_slot_paid");
        time_slot_holiday = bundle.getString("time_slot_holiday");
        payment_type = bundle.getString("payment_type");

        name_for_map = bundle.getString("name_for_map");
        lat_for_map = bundle.getDouble("lat_for_map");
        long_for_map = bundle.getDouble("long_for_map");

        if (getIntent().getSerializableExtra("questionnaire") != null) {
            questionnaire = (Questionnaire) getIntent().getSerializableExtra("questionnaire");
        }

        if (getIntent().getSerializableExtra("cardListInfo") != null) {
            cardListInfo = (CardListInfo) getIntent().getSerializableExtra("cardListInfo");
        }

        Log.d(TAG, "questionnaire " + questionnaire);

    }

    // ----- initial View
    private void initialView() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_map);
        sessionManager = new SessionManager(ScheduleMapActivity.this);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        if (payment_type.equalsIgnoreCase(ScheduleTestActivity.FREE)) {
            binding.txtBookAndPay.setText(getResources().getString(R.string.book_now));
            binding.lyoutCardDetails.setVisibility(View.GONE);
        } else if (payment_type.equalsIgnoreCase(ScheduleTestActivity.CARD)) {
            binding.txtBookAndPay.setText(getResources().getString(R.string.book_and_pay));
            binding.lyoutCardDetails.setVisibility(View.VISIBLE);
        }

        binding.tvTitle.setText(R.string.title_my_bid);

        if (Reg_date != null && !Reg_date.isEmpty() && time_slot_time != null && !time_slot_time.isEmpty()) {
            binding.bookingTv.setText(Utils.changeBEDateFormatToFE(Reg_date) + " at " + Utils.convertTimeTo12HourFormat(time_slot_time).toUpperCase());
        }

        binding.islandTv.setText(island_name);
        binding.providerTv.setText(medical_provider_name);
        binding.locationTv.setText(location_name);
        binding.testTypeTv.setText(test_type_name);

        binding.imgBack.setOnClickListener(this);
        binding.txtBookAndPay.setOnClickListener(this);
        binding.lyoutCardDetails.setOnClickListener(this);

        if (lat_for_map != null && long_for_map != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(ScheduleMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        + ActivityCompat.checkSelfPermission(ScheduleMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
                                .ACCESS_COARSE_LOCATION}, 1000);
                    }
                } else {
//                    LoadMap(savedInstanceState);
                }
            } else {
//                LoadMap(savedInstanceState);
            }
        }


    }

    // ---- OnCLick
    @Override
    public void onClick(View view) {
        if (view == binding.imgBack) {
            finish();
        } else if (view == binding.txtBookAndPay) {
            bookAndPay();
        } else if (view == binding.lyoutCardDetails) {
            AppActivityManager.redirectWithSerializedQuestionnaireObject(ScheduleMapActivity.this, PaymentCardMethodActivity.class, false, false, bundle, questionnaire, cardListInfo);
            finish();
        }
    }

    // ------ Load Map
    private void LoadMap(Bundle savedInstanceState) {

        binding.map.onCreate(savedInstanceState);
        binding.map.onResume();
        try {
            MapsInitializer.initialize(ScheduleMapActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if (googleMap != null && binding.map.findViewById(Integer.parseInt("1")) != null) {
                    View locationButton = ((View) binding.map.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                    layoutParams.setMargins(0, 100, 0, 0);
                }

                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                if (ActivityCompat.checkSelfPermission(ScheduleMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(ScheduleMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                Objects.requireNonNull(googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat_for_map, long_for_map))
                        .title(name_for_map)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))).showInfoWindow();

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat_for_map, long_for_map), 15));

            }
        });
    }

    // ----- Book And Pay
    private void bookAndPay() {
        CustomProgressDialog.getInstance().showDialog(ScheduleMapActivity.this, true);
        Logger.e(TAG, "getAccessToken " + sessionManager.getAccessToken());

        final BookScheduleTest request = new BookScheduleTest();
        request.setIsland_id(Island_id);
        request.setCenter_id(location_id);
        request.setTest_type_id(Test_type_id);
        request.setReg_date(Reg_date);
        request.setFrom_timeslot(time_slot_time);
        request.setQuestionnaire(questionnaire);
        request.setMedical_provider_id(medical_provider_id);
        request.setLocation_id(location_id);
        request.setPayment_type(payment_type);
        request.setUser_id(sessionManager.getUserId());
        if (cardListInfo != null && !payment_type.equalsIgnoreCase(FREE)) {
            request.setCard_detail(cardListInfo);
        }

        Logger.e(TAG, "request : " + request);

        Call<BookScheduleTestResponse> listResponseCall = apiInterface.book_schedule_test(sessionManager.getAccessToken(), request);
        listResponseCall.enqueue(new Callback<BookScheduleTestResponse>() {
            @Override
            public void onResponse(Call<BookScheduleTestResponse> call, Response<BookScheduleTestResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                final BookScheduleTestResponse listResponse = response.body();

                if (listResponse != null && listResponse.getErrorStatus() != null) {
                    final ErrorStatus status = listResponse.getErrorStatus();

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        AlertUtils.showAlerterSuccess(ScheduleMapActivity.this, status.getMessage());

                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(DashboardActivity.context, DashboardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }, 2500);

                    } else {

                        AlertUtils.showAlerterFailure(ScheduleMapActivity.this, status.getMessage());

                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2500);

                    }
                } else {
                    AlertUtils.showAlerterFailure(ScheduleMapActivity.this, getResources().getString(R.string.api_default_error));
                }
            }

            @Override
            public void onFailure(Call<BookScheduleTestResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cardListInfo != null) {
            binding.txtCardHolderName.setText(cardListInfo.getName());
            binding.txtCardNumber.setText(cardListInfo.getCard_number());
            binding.txtPrice.setText("Amount: $" + test_price);
        }
    }
}
