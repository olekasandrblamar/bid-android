package com.bid.app.ui.fragment.discover.transit;

import static com.adevinta.leku.LocationPickerActivityKt.ADDRESS;
import static com.adevinta.leku.LocationPickerActivityKt.LATITUDE;
import static com.adevinta.leku.LocationPickerActivityKt.LOCATION_ADDRESS;
import static com.adevinta.leku.LocationPickerActivityKt.LONGITUDE;
import static com.adevinta.leku.LocationPickerActivityKt.TIME_ZONE_DISPLAY_NAME;
import static com.adevinta.leku.LocationPickerActivityKt.TIME_ZONE_ID;
import static com.adevinta.leku.LocationPickerActivityKt.TRANSITION_BUNDLE;
import static com.adevinta.leku.LocationPickerActivityKt.ZIPCODE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adevinta.leku.LocationPickerActivity;
import com.bid.app.R;
import com.bid.app.adaper.SearchAutocompleteAdapter;
import com.bid.app.adaper.TransportScheduleSketchAdapter;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.AutoCompleteAdapterClickListener;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.request.BookATripRequest;
import com.bid.app.model.response.ATripResponse;
import com.bid.app.model.response.DirectionResults;
import com.bid.app.model.response.DirectionRoute;
import com.bid.app.model.response.DistanceResponse;
import com.bid.app.model.response.FamilyMembersResponse;
import com.bid.app.model.response.GetAvailableRoutesResponse;
import com.bid.app.model.response.Legs;
import com.bid.app.model.response.Steps;
import com.bid.app.model.view.AvailableRoute;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.FamilyData;
import com.bid.app.model.view.LatLngPosition;
import com.bid.app.model.view.RoutePath;
import com.bid.app.model.view.Step;
import com.bid.app.model.view.Waypoint;
import com.bid.app.retrofit.APIClient;
import com.bid.app.service.GPSTracker;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;
import com.bid.app.util.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleFragment extends BaseFragment implements TextWatcher,View.OnClickListener, AutoCompleteAdapterClickListener {
    private GoogleMap mMap;

    RecyclerView suggestionsRecView;
    RecyclerView method1RecView;
    TextView fromStation, toStation, selectedView;
    EditText search;
    ImageView closeSearch;
    LinearLayout pad;
    LinearLayout fromBtn, toBtn;
    LinearLayout searchResult, noResult;
    Button goMethod1Instruction;
    Spinner spinner;

    Dialog searchDialog;
    GPSTracker gps;

    View root;

    GetAvailableRoutesResponse getAvailableRoutesResponse;
    List<FamilyData> familyDataList;
    /////////////FOR AUTO COMPLEMENT //////////////////
    private static final String TAG = ScheduleFragment.class.getSimpleName();
    private SessionManager sessionManager;
    private APIInterface apiInterface;
    SearchAutocompleteAdapter autocompleteAdapter;

    ArrayList<RouteForTicket> routeForTickets;
    TransportScheduleSketchAdapter method1ScheduleAdapter;
    ActivityResultLauncher<Intent> lekuActivityResultLauncher;
    private int _yDelta;
    PlacesClient placesClient;
    String fromStationStr, toStationStr;
    LatLng fromLatLng, toLatLng;
    Marker originMarker, destinationMarker;


    ArrayList<Waypoint> waypoints;
    ArrayList<RoutePath> routePaths;

    //////////////////For Graph////////////////////////////////
    interface DistanceResponseCallBack {
        void load(DistanceResponse distanceResponse);
    }
    final Double INFINITE = 99999999.99;
    final Double walking_drive = 20.0;
    ArrayList<ArrayList<Double>> walkingPath, drivePath;

    /////////////////////////////////////////////////////

    public class RouteForTicket{
        public static final int WALKING = 0;
        public static final int TRANSIT = 1;
        public int type;
        public Double distance;
        public String routeID;
        public Integer localRouteID;
        public String startBusStop;
        public String startBusStopId;
        public String endBusStop;
        public String endBusStopId;
        public List<Waypoint> waypoints;
        public List<RoutePath> routePaths;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_choose_schedule, container, false);
        getBundle();
        initControllers(root);
        run();
        return root;
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            fromStationStr = bundle.getString("from_station");
            String fromLatLngStr = bundle.getString("fromLatLng");
            fromLatLng = new LatLng(new Double(fromLatLngStr.split(",")[0]),new Double(fromLatLngStr.split(",")[1]));
            toStationStr = bundle.getString("to_station");
            String toLatLngStr = bundle.getString("toLatLng");
            toLatLng = new LatLng(new Double(toLatLngStr.split(",")[0]),new Double(toLatLngStr.split(",")[1]));
        }
    }


    private void initControllers(View view) {
        ((DashboardActivity) requireActivity()).setTitleAndImage("Schedule", R.drawable.ic_back_arrow, false);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.context);
        gps = new GPSTracker(getContext());
        routeForTickets = new ArrayList<>();

        fromBtn = view.findViewById(R.id.from_btn);
        toBtn = view.findViewById(R.id.to_btn);
        fromStation = view.findViewById(R.id.from_station);
        toStation = view.findViewById(R.id.to_station);
        pad = view.findViewById(R.id.pad);
        searchResult = view.findViewById(R.id.search_result);
        noResult = view.findViewById(R.id.no_records);
        method1RecView = view.findViewById(R.id.method1);
        goMethod1Instruction = view.findViewById(R.id.go_method1_instruction);
        spinner = view.findViewById(R.id.spinner);
        method1RecView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

//        method1RecView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        method1RecView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });
        //Places Init
        Places.initialize(requireContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(requireContext());
        sessionManager = new SessionManager(getContext());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        //Search Dialog
        searchDialog = new Dialog(getContext());
        searchDialog.setContentView(R.layout.search_dialog);
        searchDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchDialog.getWindow().getAttributes().gravity = Gravity.TOP;
        search = searchDialog.findViewById(R.id.search_edit_text);
        closeSearch = searchDialog.findViewById(R.id.back_img_v);


        lekuActivityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>()  {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            onResult(result.getData());
                        } else {
                            Log.d("RESULT****", "CANCELLED");
                        }

                    }});


        //search
        closeSearch.setOnClickListener(this);
        search.addTextChangedListener(this);
        suggestionsRecView = searchDialog.findViewById(R.id.sug_recycler);
        fromStation.setSelected(true);
        toStation.setSelected(true);
        fromBtn.setOnClickListener(this);
        toBtn.setOnClickListener(this);
        goMethod1Instruction.setOnClickListener(this);
        getAvailableRoutesResponse = null;
    }

    public void run() {
        getFamilyMembers();
        if(fromStationStr != null && fromStationStr.length() > 0)
        {
            fromStation.setText(fromStationStr);
        } else{
            if(gps.canGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                fromLatLng = new LatLng(latitude, longitude);
                fromLatLng = new LatLng(25.06209, -77.33456);

            } else {
                gps.showSettingsAlert();
                fromLatLng = new LatLng(25.06209, -77.33456);
            }
        }
        if(toStationStr != null && toStationStr.length() > 0)
        {
            toStation.setText(toStationStr);
        }

        searchResult.setVisibility(View.GONE);
        noResult.setVisibility(View.VISIBLE);

        getAvailableRoute();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                mMap = googleMap;
                CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(fromLatLng, 14);
                mMap.animateCamera(cu);

            }
        });

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
                        int height= Resources.getSystem().getDisplayMetrics().heightPixels;
                        if(Y - _yDelta > height - 400) break;
                        layoutParams.topMargin = Y - _yDelta;
//                        layoutParams.bottomMargin = 0;
                        view.setLayoutParams(layoutParams);

                        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(mMap.getCameraPosition().target, 13 + 3* (Y-_yDelta)/1000);

                        mMap.animateCamera(cu);
                        mapLayoutParams.height = Y - _yDelta + 20;
                        root.findViewById(R.id.map_layout).setLayoutParams(mapLayoutParams);
                        if(method1ScheduleAdapter!=null && routeForTickets.size()>0) method1ScheduleAdapter.notifyDataSetChanged();

                        break;
                }
                root.invalidate();
                return true;
            }
        });
    }


    private void onResult(Intent data) {
        Log.d("RESULT****", "OK");
        Double latitude = data.getDoubleExtra(LATITUDE, 0.0);
        Log.d("LATITUDE****", latitude.toString());
        Double longitude = data.getDoubleExtra(LONGITUDE, 0.0);
        Log.d("LONGITUDE****", longitude.toString());
        String address = data.getStringExtra(LOCATION_ADDRESS);
        Log.d("ADDRESS****", address);
        String postalcode = data.getStringExtra(ZIPCODE);
        Log.d("POSTALCODE****", postalcode);
        Bundle bundle = data.getBundleExtra(TRANSITION_BUNDLE);
        Log.d("BUNDLE TEXT****", bundle.getString("Selected"));
        Parcelable fullAddress = data.getParcelableExtra(ADDRESS);
        if (fullAddress != null) {
            Log.d("FULL ADDRESS****", fullAddress.toString());
        }
        String timeZoneId = data.getStringExtra(TIME_ZONE_ID);
        if (timeZoneId != null) {
            Log.d("TIME ZONE ID****", timeZoneId);
        }
        String timeZoneDisplayName = data.getStringExtra(TIME_ZONE_DISPLAY_NAME);
        if (timeZoneDisplayName != null) {
            Log.d("TIME ZONE NAME****", timeZoneDisplayName);
        }
        String selected = bundle.getString("Selected");
        if(address == null || address.length() == 0) address = latitude.toString() +"," + longitude.toString();
        if(selected.equals("from")) {
            selectedView = fromStation;
            LatLng latLng = new LatLng(latitude, longitude);
            addEndPoint(latLng, address);
            findShortestPath();
        }
        else if(selected.equals("to")) {
            selectedView = toStation;
            LatLng latLng = new LatLng(latitude, longitude);
            addEndPoint(latLng, address);
            findShortestPath();
        }

    }
    private final double DEMO_LATITUDE = 41.4036299;
    private final double DEMO_LONGITUDE = 2.1743558;
    private final double POI1_LATITUDE = 41.4036339;
    private final double POI1_LONGITUDE = 2.1721618;
    private final double POI2_LATITUDE = 41.4023265;
    private final double POI2_LONGITUDE = 2.1741417;
    @Override
    public void onClick(View view) {
        if (view == fromBtn || view == toBtn) {

            Intent locationPickerIntent = new LocationPickerActivity.Builder()
                    .withLocation(DEMO_LATITUDE, DEMO_LONGITUDE)
                    // .withGeolocApiKey("<PUT API KEY HERE>")
                    // .withGooglePlacesApiKey("<PUT API KEY HERE>")
                    .withSearchZone("BS")
                    // .withSearchZone(SearchZoneRect(LatLng(26.525467, -18.910366), LatLng(43.906271, 5.394197)))
                    .withDefaultLocaleSearchZone()
                    // .shouldReturnOkOnBackPressed()
                    // .withStreetHidden()
                    // .withCityHidden()
                    // .withZipCodeHidden()
                    // .withSatelliteViewHidden()
                    .withGoogleTimeZoneEnabled()
                    // .withVoiceSearchHidden()
                    .withUnnamedRoadHidden()
                    // .withSearchBarHidden()
                    .build(getContext());

            // this is optional if you want to return RESULT_OK if you don't set the
            // latitude/longitude and click back button

            if(view == fromBtn) locationPickerIntent.putExtra("Selected", "from");
            else if(view == toBtn) locationPickerIntent.putExtra("Selected", "to");
            lekuActivityResultLauncher.launch(locationPickerIntent);
            if(true) return;
            if(view == fromBtn) selectedView = fromStation;
            else if(view == toBtn) selectedView = toStation;
            searchDialog.show();
            searchDialog.setCancelable(false);
            search.requestFocus();
            setAutocompleteAdapter();
        } else if (view == closeSearch) {
            searchDialog.dismiss();
        }
        else if (view == goMethod1Instruction) {
            bookMethod1AndGoInstruction();
        }

    }



    public void bookMethod1AndGoInstruction() {

        BookATripRequest bookATripRequest = new BookATripRequest();
        bookATripRequest.setFrom(fromStationStr);
        bookATripRequest.setFromLocation(fromLatLng.latitude + "," +fromLatLng.longitude);
        if(spinner.getSelectedItemPosition() > 0) {
            FamilyData familyData = familyDataList.get(spinner.getSelectedItemPosition() -1);
            bookATripRequest.setFamilyMemberId(familyData.getId());
        }
        bookATripRequest.setTo(toStationStr);
        bookATripRequest.setToLocation(toLatLng.latitude + "," +toLatLng.longitude);

        ArrayList<Step> steps = new ArrayList<>();
        for(RouteForTicket routeForTicket:routeForTickets) {
            Step step = new Step();
            if(routeForTicket.type == RouteForTicket.WALKING) step.setMode("walking");
            else if (routeForTicket.type == RouteForTicket.TRANSIT) step.setMode("driving");
            step.setDistance(routeForTicket.distance.toString());
            step.setRouteId(routeForTicket.routeID);
            step.setTo(routeForTicket.endBusStopId);
            String polyline = "";
            for(RoutePath routePath:routeForTicket.routePaths) {
                polyline += routePath.getPath() + " ";
            }
            step.setPolyline(polyline);
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

    private void setAutocompleteAdapter() {
        autocompleteAdapter = new SearchAutocompleteAdapter(getContext(), this);
        DividerItemDecoration divider = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        suggestionsRecView.addItemDecoration(divider);
        suggestionsRecView.setAdapter(autocompleteAdapter);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!charSequence.equals("")) {
            autocompleteAdapter.getPredictions(charSequence);
            if (suggestionsRecView.getVisibility() == View.GONE) {
                suggestionsRecView.setVisibility(View.VISIBLE);
            }
        } else {
            if (suggestionsRecView.getVisibility() == View.VISIBLE) {
                suggestionsRecView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onSearchedItemClick(int position) {
        getSearchedPlace(position);
        search.setText("");
        searchDialog.dismiss();
    }


    private void getSearchedPlace(int position) {

        if (SearchAutocompleteAdapter.placeIDs != null) {

            final String placeId = SearchAutocompleteAdapter.placeIDs.get(position);

            // Specify the fields to return.
            final List<Place.Field> placeFields = Arrays.asList(
                    Place.Field.LAT_LNG,
                    Place.Field.NAME
            );

            // Construct a request object, passing the place ID and fields array.
            final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

            placesClient.fetchPlace(request).addOnSuccessListener((response) -> {

                Place place = response.getPlace();
                addEndPoint(place.getLatLng(), place.getName());
                findShortestPath();
            }).addOnFailureListener((exception) -> {
                Log.i(TAG, "Place not found: " + exception.getMessage());
            });

        }

    }

    private void addEndPoint(LatLng latLng, String address) {
        if(selectedView == fromStation) {
            fromStation.setText(address);
            fromStationStr = address;
            fromLatLng = latLng;
            if(originMarker != null) originMarker.remove();
            originMarker = mMap.addMarker(new MarkerOptions()
                    .position(fromLatLng)
                    .title(fromStationStr)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_position)));
        }
        else if(selectedView == toStation) {
            toStation.setText(address);
            toStationStr = address;
            toLatLng = latLng;
            if(destinationMarker!=null) destinationMarker.remove();
            destinationMarker = mMap.addMarker(new MarkerOptions()
                    .position(toLatLng)
                    .title(toStationStr)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_position)));
        }


    }

    private void initMethod1Adapter(ArrayList<RouteForTicket> list) {
        method1ScheduleAdapter = new TransportScheduleSketchAdapter(ScheduleFragment.this.getContext(), list, ScheduleFragment.this);

        method1RecView.setAdapter(method1ScheduleAdapter);
        method1ScheduleAdapter.notifyDataSetChanged();
    }

    private void findShortestPath() {
        if(getAvailableRoutesResponse == null) return;
        String fromPosition = fromStation.getText().toString();
        String toPosition = toStation.getText().toString();
        if(fromPosition.length() == 0 || toPosition.length() == 0) return;
        graphProc(null);
    }

    void graphProc(DistanceResponse distanceResponse) {
        initPaths();
        ArrayList<Integer> pathIDs = dijkstra(0, 1);
        routeForTickets = findRoutesForTickets(pathIDs);
        if(routeForTickets.size() > 0)
        {
            searchResult.setVisibility(View.VISIBLE);
            noResult.setVisibility(View.GONE);
        }
        else
        {
            searchResult.setVisibility(View.GONE);
            noResult.setVisibility(View.VISIBLE);
        }
        drawRoute(routeForTickets);
        initMethod1Adapter(routeForTickets);

    }

    ArrayList<RouteForTicket> findRoutesForTickets(ArrayList<Integer> pathIDs) {
        routeForTickets = new ArrayList<>();
        Integer curPos = 0;
        while(curPos < pathIDs.size() - 1) {
            if(pathIDs.get(curPos) <2 || walkingPath.get(pathIDs.get(curPos)).get(pathIDs.get(curPos + 1)) < drivePath.get(pathIDs.get(curPos)).get(pathIDs.get(curPos + 1))) {
                RouteForTicket routeForTicket = new RouteForTicket();
                routeForTicket.type = RouteForTicket.WALKING;
                routeForTicket.waypoints = new ArrayList<>();
                routeForTicket.distance = walkingPath.get(pathIDs.get(curPos)).get(pathIDs.get(curPos + 1));
                routeForTicket.routeID = "0";
                routeForTicket.localRouteID = 0;
                routeForTicket.routePaths = new ArrayList<>();
                if(pathIDs.get(curPos) == 0)
                {
                    routeForTicket.startBusStop = fromStationStr;
                    Waypoint wp = new Waypoint();
                    LatLngPosition latLngPosition = new LatLngPosition();
                    latLngPosition.setLat(new Double(fromLatLng.latitude).toString());
                    latLngPosition.setLng(new Double(fromLatLng.longitude).toString());
                    wp.setPosition(latLngPosition);
                    wp.setName(fromStationStr);
                    routeForTicket.waypoints.add(wp);
                    routeForTicket.startBusStopId = "0";
                    routeForTicket.startBusStop = fromStationStr;
                }
                else if(pathIDs.get(curPos) == 1)
                {
                    routeForTicket.startBusStopId = "0";
                    routeForTicket.startBusStop = toStationStr;
                    Waypoint wp = new Waypoint();
                    LatLngPosition latLngPosition = new LatLngPosition();
                    latLngPosition.setLat(new Double(toLatLng.latitude).toString());
                    latLngPosition.setLng(new Double(toLatLng.longitude).toString());
                    wp.setPosition(latLngPosition);
                    wp.setName(toStationStr);
                    routeForTicket.waypoints.add(wp);

                }
                else{
                    routeForTicket.startBusStopId = waypoints.get(pathIDs.get(curPos) - 2).getId();
                    routeForTicket.startBusStop = waypoints.get(pathIDs.get(curPos) - 2).getName();
                    routeForTicket.waypoints.add(waypoints.get(pathIDs.get(curPos) - 2));
                }

                if(pathIDs.get(curPos + 1) == 0){
                    routeForTicket.endBusStopId = "0";
                    routeForTicket.endBusStop = fromStationStr;
                    Waypoint wp = new Waypoint();
                    LatLngPosition latLngPosition = new LatLngPosition();
                    latLngPosition.setLat(new Double(fromLatLng.latitude).toString());
                    latLngPosition.setLng(new Double(fromLatLng.longitude).toString());
                    wp.setPosition(latLngPosition);
                    wp.setName(fromStationStr);
                }
                else if(pathIDs.get(curPos + 1) == 1){
                    routeForTicket.endBusStopId = "0";
                    routeForTicket.endBusStop = toStationStr;
                    Waypoint wp = new Waypoint();
                    LatLngPosition latLngPosition = new LatLngPosition();
                    latLngPosition.setLat(new Double(toLatLng.latitude).toString());
                    latLngPosition.setLng(new Double(toLatLng.longitude).toString());
                    wp.setPosition(latLngPosition);
                    wp.setName(toStationStr);
                    routeForTicket.waypoints.add(wp);
                }
                else{
                    routeForTicket.endBusStopId = waypoints.get(pathIDs.get(curPos + 1) - 2).getId();
                    routeForTicket.endBusStop = waypoints.get(pathIDs.get(curPos + 1) - 2).getName();
                    routeForTicket.waypoints.add(waypoints.get(pathIDs.get(curPos + 1) - 2));
                }
                if(routeForTicket.distance > 0.001) routeForTickets.add(routeForTicket);
                curPos++;
            }
            else {
                int maxPos =curPos;
                int selectedID = 0;
                int startJ = 0;
                int endJ = 0;
                for(int i = 0; i<getAvailableRoutesResponse.getRoutes().size(); i++) {
                    AvailableRoute availableRoute = getAvailableRoutesResponse.getRoutes().get(i);
                    int j;
                    ArrayList<Integer> matchedJ = new ArrayList<>();
                    for(j = 0; j < availableRoute.getWaypoints().size(); j++)
                    {
                        Waypoint waypoint = availableRoute.getWaypoints().get(j);
                        if(waypoint.getId().equals(waypoints.get(pathIDs.get(curPos) - 2).getId())) {
                            matchedJ.add(j);
                        }
                    }
                    for(Integer J:matchedJ) {
                        j = J;
                        int pos = curPos;
                        for(;j<availableRoute.getWaypoints().size(); j++,pos++){
                            Waypoint waypoint = availableRoute.getWaypoints().get(j);
                            if(pos>= pathIDs.size() - 1 || pathIDs.get(pos)<2) break;
                            if(!waypoint.getId().equals(waypoints.get(pathIDs.get(pos) - 2).getId())) break;

                        }
                        if(maxPos < pos - 1){
                            selectedID = i;
                            maxPos = pos - 1;
                            startJ = J;
                            endJ = j - 1;
                        }
                    }
                }

                if(maxPos> curPos) {
                    RouteForTicket routeForTicket = new RouteForTicket();
                    routeForTicket.type = RouteForTicket.TRANSIT;
                    routeForTicket.startBusStop = waypoints.get(pathIDs.get(curPos) - 2).getName();
                    routeForTicket.startBusStopId = waypoints.get(pathIDs.get(curPos) - 2).getId();
                    routeForTicket.endBusStop = waypoints.get(pathIDs.get(maxPos) - 2).getName();
                    routeForTicket.endBusStopId = waypoints.get(pathIDs.get(maxPos) - 2).getId();
                    routeForTicket.distance = 0.0;
                    routeForTicket.routeID = getAvailableRoutesResponse.getRoutes().get(selectedID).getId();
                    routeForTicket.localRouteID = selectedID;
                    routeForTicket.waypoints = new ArrayList<>();
                    routeForTicket.routePaths = new ArrayList<>();
                    for(int i = startJ; i <= endJ; i++) {
                        routeForTicket.waypoints.add(getAvailableRoutesResponse.getRoutes().get(selectedID).getWaypoints().get(i));
                        if(i < endJ)
                        {
                            routeForTicket.routePaths.add(getAvailableRoutesResponse.getRoutes().get(selectedID).getPaths().get(i));
                            routeForTicket.distance += Utils.getDistance(getAvailableRoutesResponse.getRoutes().get(selectedID).getPaths().get(i));
                        }
                    }
                    curPos = maxPos;
                    if(routeForTicket.distance > 0.001) routeForTickets.add(routeForTicket);
                }
                else {
                    RouteForTicket routeForTicket = new RouteForTicket();
                    routeForTicket.type = RouteForTicket.WALKING;
                    routeForTicket.distance = walkingPath.get(pathIDs.get(curPos)).get(pathIDs.get(curPos + 1));
                    routeForTicket.routeID = "0";
                    routeForTicket.localRouteID = 0;
                    routeForTicket.waypoints = new ArrayList<>();
                    routeForTicket.routePaths = new ArrayList<>();

                    if(pathIDs.get(curPos) == 0)
                    {
                        routeForTicket.startBusStop = fromStationStr;
                        routeForTicket.startBusStopId = "0";
                        Waypoint wp = new Waypoint();
                        LatLngPosition latLngPosition = new LatLngPosition();
                        latLngPosition.setLat(new Double(fromLatLng.latitude).toString());
                        latLngPosition.setLng(new Double(fromLatLng.longitude).toString());
                        wp.setPosition(latLngPosition);
                        wp.setName(fromStationStr);
                        routeForTicket.waypoints.add(wp);
                    }
                    else if(pathIDs.get(curPos) == 1)
                    {
                        routeForTicket.startBusStop = toStationStr;
                        routeForTicket.startBusStopId = "0";
                        Waypoint wp = new Waypoint();
                        LatLngPosition latLngPosition = new LatLngPosition();
                        latLngPosition.setLat(new Double(toLatLng.latitude).toString());
                        latLngPosition.setLng(new Double(toLatLng.longitude).toString());
                        wp.setPosition(latLngPosition);
                        wp.setName(toStationStr);
                        routeForTicket.waypoints.add(wp);

                    }
                    else{
                        routeForTicket.startBusStop = waypoints.get(pathIDs.get(curPos) - 2).getName();
                        routeForTicket.startBusStopId = waypoints.get(pathIDs.get(curPos) - 2).getId();
                        routeForTicket.waypoints.add(waypoints.get(pathIDs.get(curPos) - 2));
                    }

                    if(pathIDs.get(curPos + 1) == 0){
                        routeForTicket.endBusStop = fromStationStr;
                        routeForTicket.endBusStopId = "0";
                        Waypoint wp = new Waypoint();
                        LatLngPosition latLngPosition = new LatLngPosition();
                        latLngPosition.setLat(new Double(fromLatLng.latitude).toString());
                        latLngPosition.setLng(new Double(fromLatLng.longitude).toString());
                        wp.setPosition(latLngPosition);
                        wp.setName(fromStationStr);
                    }
                    else if(pathIDs.get(curPos + 1) == 1){
                        routeForTicket.endBusStop = toStationStr;
                        routeForTicket.endBusStopId = "0";
                        Waypoint wp = new Waypoint();
                        LatLngPosition latLngPosition = new LatLngPosition();
                        latLngPosition.setLat(new Double(toLatLng.latitude).toString());
                        latLngPosition.setLng(new Double(toLatLng.longitude).toString());
                        wp.setPosition(latLngPosition);
                        wp.setName(toStationStr);
                        routeForTicket.waypoints.add(wp);
                    }
                    else{
                        routeForTicket.endBusStop = waypoints.get(pathIDs.get(curPos + 1) - 2).getName();
                        routeForTicket.endBusStopId = waypoints.get(pathIDs.get(curPos + 1) - 2).getId();
                        routeForTicket.waypoints.add(waypoints.get(pathIDs.get(curPos + 1) - 2));
                    }
                    if(routeForTicket.distance > 0.001) routeForTickets.add(routeForTicket);
                    curPos++;
                }
            }
        }
        return routeForTickets;
    }

    void drawRoute(ArrayList<RouteForTicket> routeForTickets) {
        mMap.clear();
        if(originMarker != null) originMarker.remove();
        originMarker = mMap.addMarker(new MarkerOptions()
                .position(fromLatLng)
                .title(fromStationStr)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_position)));
        if(destinationMarker!=null) destinationMarker.remove();
        destinationMarker = mMap.addMarker(new MarkerOptions()
                .position(toLatLng)
                .title(toStationStr)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_position)));

        int color1 = Color.argb(100, 50,50,255);
        int color1Stroke = Color.argb(255, 50,50,255);
        int width1 = 5;
        double delta1 = 0;
        int color2 = Color.argb(100, 50,255,50);
        int color2Stroke = Color.argb(255, 50,255,50);
        int width2 = 10;
        double delta2 = -0.0001;
        int k = 0;
        int color;
        int width;
        double delta;
        for(RouteForTicket routeForTicket:routeForTickets) {
            if(routeForTicket.type == RouteForTicket.WALKING) {
                LatLng latLng1 = new LatLng(new Double(routeForTicket.waypoints.get(0).getPosition().getLat()), new Double(routeForTicket.waypoints.get(0).getPosition().getLng()));
                LatLng latLng2 = new LatLng(new Double(routeForTicket.waypoints.get(1).getPosition().getLat()), new Double(routeForTicket.waypoints.get(1).getPosition().getLng()));

                drawWalking(latLng1.latitude + "," + latLng1.longitude, latLng2.latitude + "," + latLng2.longitude);
//                List<PatternItem> pattern = Arrays.<PatternItem>asList(new Dash(30), new Gap(20));
//                Polyline line = mMap.addPolyline(new PolylineOptions()
//                        .add(latLng1, latLng2)
//                        .width(5)
//                        .color(Color.RED)
//                        .pattern(pattern));
                for(Waypoint wp: routeForTicket.waypoints) {
                    LatLng latLng = new LatLng(new Double(wp.getPosition().getLat()), new Double(wp.getPosition().getLng()));
                    if(wp.getName().equals(fromStationStr)) continue;
                    else if(wp.getName().equals(toStationStr)) continue;
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(wp.getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.star_red)));
                }
            }
            else{
                k++;
                if(k % 2 ==0){
                    color = color1Stroke;
                    delta = delta1;
                }
                else{
                    color = color2Stroke;
                    delta = delta2;
                }
                for(RoutePath routePath:routeForTicket.routePaths) {
                    LatLng latLngSt = Utils.getLatLngArray(routePath).get(0);

                    for(LatLng latLng:Utils.getLatLngArray(routePath)) {
                        LatLng latLngEd = latLng;
                        latLngSt = new LatLng(latLngSt.latitude + delta, latLngSt.longitude);
                        latLngEd = new LatLng(latLngEd.latitude + delta, latLngEd.longitude);

                        mMap.addPolyline(new PolylineOptions()
                                .add(latLngSt, latLngEd)
                                .width(8)
                                .color(color));
                        latLngSt = latLngEd;
                    }
                }
            }
        }
        //drawing original bus line.
        Set<Integer> routeSet = new HashSet<>();

        for(RouteForTicket routeForTicket:routeForTickets)
            if(routeForTicket.type == RouteForTicket.TRANSIT) routeSet.add(routeForTicket.localRouteID);
        k = 0;
        for(Integer routeID: routeSet){
            k++;
            for(Waypoint waypoint: getAvailableRoutesResponse.getRoutes().get(routeID).getWaypoints()) {
                LatLng latLng = new LatLng(new Double(waypoint.getPosition().getLat()), new Double(waypoint.getPosition().getLng()));
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(waypoint.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop_blue)));

            }
            for(int i = 0; i < getAvailableRoutesResponse.getRoutes().get(routeID).getPaths().size(); i++) {
                RoutePath routePath = getAvailableRoutesResponse.getRoutes().get(routeID).getPaths().get(i);

                if(k % 2 ==0){
                    color = color1;
                    width = width1;
                    delta = delta1;
                }
                else{
                    color = color2;
                    width = width2;
                    delta = delta2;
                }
                LatLng latLngSt = Utils.getLatLngArray(routePath).get(0);
                for(LatLng latLng:Utils.getLatLngArray(routePath)) {
                    LatLng latLngEd = latLng;
                    latLngSt = new LatLng(latLngSt.latitude + delta, latLngSt.longitude);
                    latLngEd = new LatLng(latLngEd.latitude + delta, latLngEd.longitude);

                    mMap.addPolyline(new PolylineOptions()
                            .add(latLngSt, latLngEd)
                            .width(width)
                            .color(color));
                    latLngSt = latLngEd;
                }

            }
        }
    }
    ArrayList<Integer> dijkstra(int st, int ed) {
        ArrayList<Double> distanceArray =  new ArrayList<>();
        ArrayList<Integer> pre = new ArrayList<>();
        for(int i = 0; i < waypoints.size() + 2; i++)
        {
            pre.add(st);
            distanceArray.add(walkingPath.get(st).get(i));
        }
        distanceArray.set(st, 0.0);
        for(int i = 0; i < waypoints.size() + 2; i++) {
            for(int j = 0; j < waypoints.size() + 2; j++) {
                if(distanceArray.get(j) > distanceArray.get(i) + walkingPath.get(i).get(j)) {
                    distanceArray.set(j, distanceArray.get(i) + walkingPath.get(i).get(j));
                    pre.set(j, i);
                }
                if(distanceArray.get(j) > distanceArray.get(i) + drivePath.get(i).get(j)) {
                    distanceArray.set(j, distanceArray.get(i) + drivePath.get(i).get(j));
                    pre.set(j, i);
                }
            }
        }
        Stack<Integer> shortestPathStack =  new Stack<>();
        int u = ed;
        shortestPathStack.add(u);
        while(u!=st) {
            u = pre.get(u);
            shortestPathStack.add(u);
        }
        ArrayList<Integer> shortestPath = new ArrayList<>();
        while(!shortestPathStack.empty()) {
            shortestPath.add(shortestPathStack.pop());
        }
        return shortestPath;
    }

    public void initPaths() {
        walkingPath =new ArrayList<>();
        drivePath = new ArrayList<>();
        for(int i = 0; i < waypoints.size() + 2; i++) {
            ArrayList<Double> walkingCellArray = new ArrayList<>();
            ArrayList<Double> drivePathCellArray = new ArrayList<>();
            for(int j = 0; j < waypoints.size() + 2; j++) {
                walkingCellArray.add(INFINITE);
                drivePathCellArray.add(INFINITE);
            }
            walkingPath.add(walkingCellArray);
            drivePath.add(drivePathCellArray);
        }

        for(int i = 0; i < waypoints.size() + 2; i++) {
            for(int j = 0; j < waypoints.size() + 2; j++) {
                LatLng latLngi, latLngj;
                if(i ==0) latLngi = fromLatLng;
                else if(i ==1) latLngi = toLatLng;
                else{
                    latLngi = Utils.LatLngPosition2LatLng(waypoints.get(i - 2).getPosition());
                }
                if(j ==0) latLngj = fromLatLng;
                else if(j ==1) latLngj = toLatLng;
                else{
                    latLngj = Utils.LatLngPosition2LatLng(waypoints.get(j - 2).getPosition());
                }
                walkingPath.get(i).set(j, Utils.getDistance(latLngi, latLngj) * walking_drive);
            }
        }

        if(getAvailableRoutesResponse != null) {
            for(AvailableRoute availableRoute: getAvailableRoutesResponse.getRoutes()) {
                Map<String, Integer> waypointID2ID = new HashMap<>();
                for(int i =0; i < waypoints.size(); i++)
                {
                    waypointID2ID.put(waypoints.get(i).getId(), i + 2);
                }
                for(RoutePath routePath:availableRoute.getPaths()) {
                    Integer fromN = waypointID2ID.get(routePath.getFrom());
                    Integer toN = waypointID2ID.get(routePath.getTo());
                    drivePath.get(fromN).set(toN, Utils.getDistance(routePath));
                }
            }
        }
    }
    private void getAvailableRoute() {

        waypoints = new ArrayList<>();
        routePaths = new ArrayList<>();
        CustomProgressDialog.getInstance().showDialog(getContext(), true);
        Call<GetAvailableRoutesResponse> listResponseCall = apiInterface.getAvailableRoutesDB(sessionManager.getAccessToken());
        listResponseCall.enqueue(new Callback<GetAvailableRoutesResponse>() {
            @Override
            public void onResponse(Call<GetAvailableRoutesResponse> call, Response<GetAvailableRoutesResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                getAvailableRoutesResponse = response.body();
                if(getAvailableRoutesResponse != null) {
                    for(AvailableRoute availableRoute: getAvailableRoutesResponse.getRoutes()){
                        if(availableRoute == null) getAvailableRoutesResponse.getRoutes().remove(availableRoute);
                    }
                    for(AvailableRoute availableRoute: getAvailableRoutesResponse.getRoutes()) {
                        for(Waypoint waypoint: availableRoute.getWaypoints()) {
                            waypoints.add(waypoint);
                        }
                    }

                    findShortestPath();


                }
                else{
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

    private void drawWalking(String startStr, String endStr) {

//        CustomProgressDialog.getInstance().showDialog(getContext(), true);
        Call<DirectionResults> listResponseCall = apiInterface.fetchRoutePath(startStr, endStr, "","walking","12345678900123123123");
        listResponseCall.enqueue(new Callback<DirectionResults>() {
            @Override
            public void onResponse(Call<DirectionResults> call, Response<DirectionResults> response) {
//                CustomProgressDialog.getInstance().dismissDialog();
                DirectionResults directionResults = response.body();
                if(directionResults != null) {
                    for(DirectionRoute directionRoute:directionResults.getRoutes()) {
                        for(Legs legs: directionRoute.getLegs()) {
                            for(Steps steps:legs.getSteps()) {
                                List<LatLng> array = Utils.decodePoly(steps.getPolyline().getPoints());
                                LatLng latLngi = array.get(0);
                                for(LatLng latLngj: array) {
                                    List<PatternItem> pattern = Arrays.<PatternItem>asList(new Dash(30), new Gap(20));
                                    Polyline line = mMap.addPolyline(new PolylineOptions()
                                            .add(latLngi, latLngj)
                                            .width(5)
                                            .color(Color.RED)
                                            .pattern(pattern));
                                    latLngi = latLngj;
                                }

                            }
                        }

                    }

                }
                else{
                    Toast.makeText(getContext(), "Drawing walking Failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DirectionResults> call, Throwable t) {
                t.printStackTrace();
//                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }

    private void getDistanceInfo(String origin, String destination, DistanceResponseCallBack callBack) {
        Map<String, String> mapQuery = new HashMap<>();
        mapQuery.put("units", "imperial");
        mapQuery.put("origins", origin);
        mapQuery.put("destinations", destination );
        mapQuery.put("mode", "walking" );
        mapQuery.put("key","12345678900123123123");

        Call<DistanceResponse> call = apiInterface.getDistanceInfo(mapQuery);
        call.enqueue(new Callback<DistanceResponse>() {
            @Override
            public void onResponse(Call<DistanceResponse> call, Response<DistanceResponse> response) {
                callBack.load(response.body());
            }

            @Override
            public void onFailure(Call<DistanceResponse> call, Throwable t) {

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
