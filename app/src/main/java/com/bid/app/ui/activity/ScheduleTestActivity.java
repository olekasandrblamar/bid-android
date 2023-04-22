package com.bid.app.ui.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.bid.app.R;
import com.bid.app.adaper.IslandAdapter;
import com.bid.app.adaper.LocationsAdapter;
import com.bid.app.adaper.MedicalProviderAdapter;
import com.bid.app.adaper.ScheduleSlotsAdapter;
import com.bid.app.adaper.TestTypesListAdapter;
import com.bid.app.databinding.ActivityScheduletestBinding;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.CenterListResponse;
import com.bid.app.model.response.IslandInfo;
import com.bid.app.model.response.MedicalProvider;
import com.bid.app.model.response.Questionnaire;
import com.bid.app.model.response.SlotDetails;
import com.bid.app.model.view.LocationInfo;
import com.bid.app.model.view.TestType;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.base.BaseActivity;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleTestActivity extends BaseActivity {

    private static final String TAG = ScheduleTestActivity.class.getSimpleName();
    public static final String SELECT_AN_ISLAND = "Select an Island";
    public static final String SELECT_A_LOCATION = "Select a Location";
    public static final String SELECT_A_MEDICAL_PROVIDER = "Select a Medical Provider";
    public static final String SELECT_TEST = "Select a Test";
    public static final String NO_ISLAND_S_FOUND = "No Island(s) Found";
    public static final String ERROR_LOADING_ISLAND_S = "Error Loading Island(s)";
    public static final String DATE_FORMAT_T = "EEE MMM dd HH:mm:ss 'T' yyyy";
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String FREE = "Free";
    public static final String CARD = "Card";
    public static final String CASH = "Cash";
    public static ScheduleTestActivity tempScheduleTestActivity;

    private ActivityScheduletestBinding binding;
    private SessionManager sessionManager;
    private APIInterface apiInterface;
    private String selectionDate = "";
    private CenterListResponse centerListResponse;
    private ScheduleSlotsAdapter scheduleSlotsAdapter;
    private int selectedTimeSlotPosition = RecyclerView.NO_POSITION;
    private Questionnaire questionnaire;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getCenterList();
    }

    // -------- Initialize View
    private void initView() {
        questionnaire = (Questionnaire) getIntent().getSerializableExtra("questionnaire");
        Log.d(TAG, "questionnaire " + questionnaire);

        binding = DataBindingUtil.setContentView(ScheduleTestActivity.this, R.layout.activity_scheduletest);

        sessionManager = new SessionManager(ScheduleTestActivity.this);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        tempScheduleTestActivity = ScheduleTestActivity.this;

        Calendar min = Calendar.getInstance();
        binding.calendarView.setMinimumDate(min);

        try {
            binding.calendarView.setDate(min);
            binding.calendarView.setMinimumDate(min);
            Date dates_ = Calendar.getInstance().getTime();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormatLocal_temp = new SimpleDateFormat(DATE_FORMAT_T);
            selectionDate = changeDateFormat(DATE_FORMAT_T, DATE_FORMAT_YYYY_MM_DD, dateFormatLocal_temp.format(dates_));
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        binding.tvTitle.setText(getResources().getString(R.string.title_schedule_test));
        binding.tvTitleScheduleTest.setText(R.string.schedule_your_covid_date);

        binding.imgBack.setOnClickListener(this);
        binding.imgNext.setOnClickListener(this);

        binding.calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                selectedTimeSlotPosition = RecyclerView.NO_POSITION;

                Date dates_ = eventDay.getCalendar().getTime();
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat dateFormatLocal_temp = new SimpleDateFormat(DATE_FORMAT_T);
                selectionDate = changeDateFormat(DATE_FORMAT_T, DATE_FORMAT_YYYY_MM_DD, dateFormatLocal_temp.format(dates_));

                binding.spinnerTestIsland.setAdapter(null);

                binding.spinnerMedicalProviders.setVisibility(View.GONE);
                binding.spinnerMedicalProviders.setAdapter(null);
                binding.spinnerTestLocation.setVisibility(View.GONE);
                binding.spinnerTestLocation.setAdapter(null);
                binding.spinnerTestType.setVisibility(View.GONE);
                binding.spinnerTestType.setAdapter(null);

                binding.bookingSlotsRv.setVisibility(View.GONE);

                getCenterList();
            }
        });
    }

    // ---- OnClick
    @Override
    public void onClick(View view) {
        if (view == binding.imgBack) {
            finish();
        } else if (view == binding.imgNext) {
            if (canBookATest()) {
                Bundle bundle = new Bundle();
                IslandInfo islandInfo = centerListResponse.getIslands().get(binding.spinnerTestIsland.getSelectedItemPosition());
                MedicalProvider medicalProvider = islandInfo.getMedical_providers().get(binding.spinnerMedicalProviders.getSelectedItemPosition());
                LocationInfo locationInfo = medicalProvider.getLocations().get(binding.spinnerTestLocation.getSelectedItemPosition());
                TestType testType = locationInfo.getTest_types().get(binding.spinnerTestType.getSelectedItemPosition());
                Map<String, SlotDetails> timeslots = locationInfo.getTimeslots().get(0);

                bundle.putString("reg_date", selectionDate);
                bundle.putString("island_id", islandInfo.getIsland_id());
                bundle.putString("island_name", islandInfo.getIsland_name());
                bundle.putString("medical_provider_id", medicalProvider.getMedical_provider_id());
                bundle.putString("medical_provider_name", medicalProvider.getMedical_provider());
                bundle.putString("location_id", locationInfo.getLocation_id());
                bundle.putString("location_name", locationInfo.getLocation_name());
                bundle.putString("test_type_id", testType.getId());
                bundle.putString("test_type_name", testType.getName());
                bundle.putString("test_price", testType.getPrice());

                String time_slot_time = Objects.requireNonNull(timeslots).keySet().toArray()[selectedTimeSlotPosition].toString();
                SlotDetails slotDetails = timeslots.get(time_slot_time);

                bundle.putString("time_slot_time", time_slot_time);
                bundle.putString("time_slot_free", Objects.requireNonNull(slotDetails).getFree());
                bundle.putString("time_slot_paid", Objects.requireNonNull(slotDetails).getPaid());
                bundle.putString("time_slot_holiday", Objects.requireNonNull(slotDetails).getHoliday());

//                if (center.getLat() != null && center.getLon() != null) {
//                    bundle.putString("name_for_map", center.getName());
//                    bundle.putDouble("lat_for_map", Double.parseDouble(center.getLat()));
//                    bundle.putDouble("long_for_map", Double.parseDouble(center.getLon()));
//                } else if (locationInfo.getLatitude() != null && locationInfo.getLongitude() != null) {
//                    bundle.putString("name_for_map", locationInfo.getName());
//                    bundle.putDouble("lat_for_map", Double.parseDouble(locationInfo.getLatitude()));
//                    bundle.putDouble("long_for_map", Double.parseDouble(locationInfo.getLongitude()));
//                }

                if (questionnaire.getSymptoms().size() >= 5) {
//                    FREE
                    bundle.putString("payment_type", FREE);
                    AppActivityManager.redirectWithSerializedQuestionnaireObject(ScheduleTestActivity.this, ScheduleMapActivity.class, false, false, bundle, questionnaire, null);
                } else {
//                    PAID
                    bundle.putString("payment_type", CARD);
                    AppActivityManager.redirectWithSerializedQuestionnaireObject(ScheduleTestActivity.this, PaymentCardMethodActivity.class, false, false, bundle, questionnaire, null);
                }
            }
        }
    }

    private boolean canBookATest() {
        if (centerListResponse != null && centerListResponse.getIslands() != null && centerListResponse.getIslands().size() > 0) {
            if (binding.spinnerTestIsland.getSelectedItemPosition() == 0) {
                AlertUtils.showAlerterWarning(this, "Please select a location");
                return false;
            } else if (binding.spinnerTestLocation.getSelectedItemPosition() == 0) {
                AlertUtils.showAlerterWarning(this, "Please select a center");
                return false;
            } else if (binding.spinnerTestType.getSelectedItemPosition() == 0) {
                AlertUtils.showAlerterWarning(this, "Please select a test");
                return false;
            } else if (selectedTimeSlotPosition == RecyclerView.NO_POSITION) {
                AlertUtils.showAlerterWarning(this, "Please select a time slot");
                return false;
            }
        } else {
            AlertUtils.showAlerterWarning(this, "Please select a location");
            return false;
        }
        return true;
    }

    // ---- Center List
    private void getCenterList() {
        CustomProgressDialog.getInstance().showDialog(this, true);
        Logger.e(TAG, "getAccessToken " + sessionManager.getAccessToken());

        Call<CenterListResponse> listResponseCall = apiInterface.getCOVIDCenters(sessionManager.getAccessToken(), selectionDate);
        listResponseCall.enqueue(new Callback<CenterListResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<CenterListResponse> call, Response<CenterListResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                centerListResponse = response.body();

                if (centerListResponse != null && ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(centerListResponse.getError().getCode())) {
                    if (centerListResponse.getIslands() != null && centerListResponse.getIslands().size() > 0) {

                        final IslandInfo islandInfo = new IslandInfo();
                        islandInfo.setIsland_id("");
                        islandInfo.setIsland_name(SELECT_AN_ISLAND);

                        final MedicalProvider medicalProvider = new MedicalProvider();
                        medicalProvider.setMedical_provider_id("");
                        medicalProvider.setMedical_provider(SELECT_A_MEDICAL_PROVIDER);

                        final LocationInfo locationInfo = new LocationInfo();
                        locationInfo.setLocation_id("");
                        locationInfo.setLocation_name(SELECT_A_LOCATION);

                        TestType testType = new TestType();
                        testType.setId("");
                        testType.setName(SELECT_TEST);

                        for (IslandInfo islandInfo1 : centerListResponse.getIslands()) {
                            islandInfo1.getMedical_providers().add(0, medicalProvider);
                            for (MedicalProvider medicalProvider1 : islandInfo1.getMedical_providers()) {
                                if (medicalProvider1.getLocations() != null) {
                                    medicalProvider1.getLocations().add(0, locationInfo);
                                    for (LocationInfo locationInfo1 : medicalProvider1.getLocations()) {
                                        if (locationInfo1.getTest_types() != null) {
                                            locationInfo1.getTest_types().add(0, testType);
                                        }
                                    }
                                }
                            }
                        }
                        centerListResponse.getIslands().add(0, islandInfo);
                        setData();
                    } else {
                        final ArrayList<IslandInfo> islandInfos = new ArrayList<>();
                        final IslandInfo islandInfo = new IslandInfo();
                        islandInfo.setIsland_id("");
                        islandInfo.setIsland_name(NO_ISLAND_S_FOUND);
                        islandInfos.add(islandInfo);
                        initSpinnerIslandAdapter(islandInfos);
                    }

                } else {
                    final ArrayList<IslandInfo> islandInfos = new ArrayList<>();
                    final IslandInfo islandInfo = new IslandInfo();
                    islandInfo.setIsland_id("");
                    islandInfo.setIsland_name(ERROR_LOADING_ISLAND_S);
                    islandInfos.add(islandInfo);
                    initSpinnerIslandAdapter(islandInfos);
                }
            }

            @Override
            public void onFailure(Call<CenterListResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }

    private void setData() {
        initSpinnerIslandAdapter(centerListResponse.getIslands());
        binding.spinnerTestIsland.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setNextButtonState();
                if (i > 0) {
                    initSpinnerMedicalProviderAdapter(centerListResponse.getIslands().get(binding.spinnerTestIsland.getSelectedItemPosition()).getMedical_providers());
                    binding.spinnerMedicalProviders.setVisibility(View.VISIBLE);

                    binding.spinnerTestLocation.setVisibility(View.GONE);
                    binding.spinnerTestLocation.setAdapter(null);

                    binding.spinnerTestType.setVisibility(View.GONE);
                    binding.spinnerTestType.setAdapter(null);

                    binding.bookingSlotsRv.setVisibility(View.GONE);
                } else {
                    binding.spinnerMedicalProviders.setVisibility(View.GONE);
                    binding.spinnerMedicalProviders.setAdapter(null);

                    binding.spinnerTestLocation.setVisibility(View.GONE);
                    binding.spinnerTestLocation.setAdapter(null);

                    binding.spinnerTestType.setVisibility(View.GONE);
                    binding.spinnerTestType.setAdapter(null);

                    binding.bookingSlotsRv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerMedicalProviders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                setNextButtonState();
                if (i > 0) {
                    initSpinnerTestLocationAdapter(centerListResponse.getIslands().get(binding.spinnerTestIsland.getSelectedItemPosition()).getMedical_providers().get(binding.spinnerMedicalProviders.getSelectedItemPosition()).getLocations());
                    binding.spinnerTestLocation.setVisibility(View.VISIBLE);

                    binding.spinnerTestType.setVisibility(View.GONE);
                    binding.spinnerTestType.setAdapter(null);

                    binding.bookingSlotsRv.setVisibility(View.GONE);

                } else {
                    binding.spinnerTestLocation.setVisibility(View.GONE);
                    binding.spinnerTestLocation.setAdapter(null);

                    binding.spinnerTestType.setVisibility(View.GONE);
                    binding.spinnerTestType.setAdapter(null);

                    binding.bookingSlotsRv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerTestLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setNextButtonState();
                if (i > 0) {
                    initSpinnerTestType(centerListResponse.getIslands().get(binding.spinnerTestIsland.getSelectedItemPosition()).getMedical_providers().get(binding.spinnerMedicalProviders.getSelectedItemPosition()).getLocations().get(binding.spinnerTestLocation.getSelectedItemPosition()).getTest_types());
                    initSlotListAdapter(centerListResponse.getIslands().get(binding.spinnerTestIsland.getSelectedItemPosition()).getMedical_providers().get(binding.spinnerMedicalProviders.getSelectedItemPosition()).getLocations().get(binding.spinnerTestLocation.getSelectedItemPosition()).getTimeslots());

                    binding.spinnerTestType.setVisibility(View.VISIBLE);

                } else {
                    binding.spinnerTestType.setAdapter(null);
                    binding.spinnerTestType.setVisibility(View.GONE);

                    binding.bookingSlotsRv.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerTestType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setNextButtonState();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initSpinnerIslandAdapter(ArrayList<IslandInfo> list) {
        final IslandAdapter islandAdapter = new IslandAdapter(ScheduleTestActivity.this, list);
        binding.spinnerTestIsland.setAdapter(islandAdapter);
    }

    private void initSpinnerMedicalProviderAdapter(ArrayList<MedicalProvider> list) {
        final MedicalProviderAdapter medicalProviderAdapter = new MedicalProviderAdapter(ScheduleTestActivity.this, list);
        binding.spinnerMedicalProviders.setAdapter(medicalProviderAdapter);
    }

    private void initSpinnerTestLocationAdapter(ArrayList<LocationInfo> list) {
        final LocationsAdapter locationsAdapter = new LocationsAdapter(ScheduleTestActivity.this, list);
        binding.spinnerTestLocation.setAdapter(locationsAdapter);
    }

    private void initSpinnerTestType(List<TestType> list) {
        final TestTypesListAdapter testTypesListAdapter = new TestTypesListAdapter(ScheduleTestActivity.this, list);
        binding.spinnerTestType.setAdapter(testTypesListAdapter);
    }

    private void initSlotListAdapter(ArrayList<Map<String, SlotDetails>> timeslotsList) {

        Date currentTime = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM);

        Map<String, SlotDetails> stringSlotDetailsMap = timeslotsList.get(0);
        ArrayList<SlotDetails> slotDetailsList = new ArrayList<>();
        for (int i = 0; i < stringSlotDetailsMap.size(); i++) {
            String time_slot_time = stringSlotDetailsMap.keySet().toArray()[i].toString();
            SlotDetails slotDetails = stringSlotDetailsMap.get(time_slot_time);

            try {
                Date slotTime = sdf.parse(selectionDate + " " + time_slot_time);
                if (Objects.requireNonNull(currentTime).after(slotTime)) {  // true / end time has passed current time.
                    Objects.requireNonNull(slotDetails).setIs_expired(true);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Objects.requireNonNull(slotDetails).setTime(time_slot_time);
            slotDetailsList.add(slotDetails);
        }

        scheduleSlotsAdapter = new ScheduleSlotsAdapter(ScheduleTestActivity.this, slotDetailsList, ScheduleTestActivity.this);
        binding.bookingSlotsRv.setLayoutManager(new GridLayoutManager(this, 2));
        binding.bookingSlotsRv.setAdapter(scheduleSlotsAdapter);
        binding.bookingSlotsRv.setVisibility(View.VISIBLE);
    }

    @Override
    public void clickOnSlot(int timeSlotPosition) {
        if (selectedTimeSlotPosition == timeSlotPosition) {
            selectedTimeSlotPosition = RecyclerView.NO_POSITION;
        } else {
            selectedTimeSlotPosition = timeSlotPosition;
        }
        notifyScheduleSlotsAdapter();
        setNextButtonState();
    }

    private void notifyScheduleSlotsAdapter() {
        scheduleSlotsAdapter.setSelectedPosition(selectedTimeSlotPosition);
        scheduleSlotsAdapter.notifyDataSetChanged();
    }

    // ------- image Next Enable
    private void setNextButtonState() {
        binding.nestedScrollView.post(() -> binding.nestedScrollView.fullScroll(View.FOCUS_DOWN));
        if (centerListResponse != null && centerListResponse.getIslands() != null && !centerListResponse.getIslands().isEmpty() && selectedTimeSlotPosition != -1 && binding.spinnerTestType.getSelectedItemPosition() != 0 && binding.spinnerTestLocation.getSelectedItemPosition() != 0 && binding.spinnerTestIsland.getSelectedItemPosition() != 0) {
            binding.imgNext.setBackgroundResource(R.drawable.ic_check);
        } else {
            binding.imgNext.setBackgroundResource(R.drawable.ic_uncheck);
        }
    }

    // ------ Change Date Format
    public static String changeDateFormat(String currentFormat, String requiredFormat, String dateString) {
        String result = "";
        if (dateString == null || dateString.isEmpty()) {
            return result;
        }
        SimpleDateFormat formatterOld = new SimpleDateFormat(currentFormat, Locale.ENGLISH);
        SimpleDateFormat formatterNew = new SimpleDateFormat(requiredFormat, Locale.ENGLISH);
        Date date = null;
        try {
            date = formatterOld.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            result = formatterNew.format(date);
        }
        return result;
    }

}