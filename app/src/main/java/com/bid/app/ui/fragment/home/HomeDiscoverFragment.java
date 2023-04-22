package com.bid.app.ui.fragment.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
//import com.bid.app.model.view.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.DiscoverHomeAdapter;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.model.response.RestaurantResponse;
import com.bid.app.model.view.BIDHome;
import com.bid.app.model.view.Geometry;
import com.bid.app.model.view.Restaurant;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.Logger;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeDiscoverFragment extends BaseFragment {

    private static final String TAG = HomeBIDFragment.class.getSimpleName();

    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_CHECK_SETTINGS = 100;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discover_list, container, false);

        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_discover), R.drawable.ic_menu, true);
        initController(view);

        return view;
    }

    private void initController(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        setLayoutManager();
        initAdapter(getRestaurantList());


        /**
         *    Check location permission
         */

        Dexter.withContext(requireActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Logger.e(TAG, "onPermissionGranted : " + permissionGrantedResponse.getPermissionName());
                        displayLocationSettingsRequest();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Logger.e(TAG, "onPermissionDenied : " + permissionDeniedResponse.getPermissionName());
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        Logger.e(TAG, "onPermissionRationaleSho uldBeShown : ");
                    }
                }).check();


    }

    private void setLayoutManager() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.context);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initAdapter(final List<Restaurant> list) {
        final DiscoverHomeAdapter discoverHomeAdapter = new DiscoverHomeAdapter(requireActivity(), list, HomeDiscoverFragment.this);
        recyclerView.setAdapter(discoverHomeAdapter);
        discoverHomeAdapter.notifyDataSetChanged();
    }

    private List<BIDHome> getHomeList() {
        final List<BIDHome> homeList = new ArrayList<>();
        final BIDHome home1 = new BIDHome("Phasellus Dignissim, Tellus In Pellentesque Mollis, Mauris Orci Dignissim Nisl, Id Gravida Nunc Enim Quis Nibh. Maecenas Convallis Eros A Ante Dignissim, Vitae Elementum Metus Facilisis. Cras In Maximus Sem. Praesent Libero Augue, Ornare Eget Quam Sed, Volutpat Suscipit Arcu. Duis Ut Urna Commodo, Commodo Tellus Ac, Consequat Justo. Maecenas Nec Est Ac Purus Mattis Tristique Vitae Vel Leo. Duis Ac Eros Vel Nunc Aliquet Ultricies Vel At Metus. Praesent A Sagittis Leo. Maecenas Volutpat, Justo In Egestas Mattis, Lectus Dui Venenatis Ex, Consequat Imperdiet Velit Odio Eget Dolor. Mauris Commodo Cursus Metus Ut Lobortis. Nulla Eget Facilisis Nibh, Et Varius Justo. Ut Laoreet Purus At Neque Lacinia Tempus. Cras Venenatis Sed Felis Sed Pulvinar. Mauris Orci Sapien, Convallis Scelerisque Nunc Id, Molestie Mattis Lectus. Vivamus Facilisis Eu Odio At Vestibulum. Mauris Id Odio Ut Libero Ornare Finibus.", R.drawable.discover);
        final BIDHome home2 = new BIDHome("Phasellus Dignissim, Tellus In Pellentesque Mollis, Mauris Orci Dignissim Nisl, Id Gravida Nunc Enim Quis Nibh. Maecenas Convallis Eros A Ante Dignissim, Vitae Elementum Metus Facilisis. Cras In Maximus Sem. Praesent Libero Augue, Ornare Eget Quam Sed, Volutpat Suscipit Arcu. Duis Ut Urna Commodo, Commodo Tellus Ac, Consequat Justo. Maecenas Nec Est Ac Purus Mattis Tristique Vitae Vel Leo. Duis Ac Eros Vel Nunc Aliquet Ultricies Vel At Metus. Praesent A Sagittis Leo. Maecenas Volutpat, Justo In Egestas Mattis, Lectus Dui Venenatis Ex, Consequat Imperdiet Velit Odio Eget Dolor. Mauris Commodo Cursus Metus Ut Lobortis. Nulla Eget Facilisis Nibh, Et Varius Justo. Ut Laoreet Purus At Neque Lacinia Tempus. Cras Venenatis Sed Felis Sed Pulvinar. Mauris Orci Sapien, Convallis Scelerisque Nunc Id, Molestie Mattis Lectus. Vivamus Facilisis Eu Odio At Vestibulum. Mauris Id Odio Ut Libero Ornare Finibus.", R.drawable.discover);
        final BIDHome home3 = new BIDHome("Phasellus Dignissim, Tellus In Pellentesque Mollis, Mauris Orci Dignissim Nisl, Id Gravida Nunc Enim Quis Nibh. Maecenas Convallis Eros A Ante Dignissim, Vitae Elementum Metus Facilisis. Cras In Maximus Sem. Praesent Libero Augue, Ornare Eget Quam Sed, Volutpat Suscipit Arcu. Duis Ut Urna Commodo, Commodo Tellus Ac, Consequat Justo. Maecenas Nec Est Ac Purus Mattis Tristique Vitae Vel Leo. Duis Ac Eros Vel Nunc Aliquet Ultricies Vel At Metus. Praesent A Sagittis Leo. Maecenas Volutpat, Justo In Egestas Mattis, Lectus Dui Venenatis Ex, Consequat Imperdiet Velit Odio Eget Dolor. Mauris Commodo Cursus Metus Ut Lobortis. Nulla Eget Facilisis Nibh, Et Varius Justo. Ut Laoreet Purus At Neque Lacinia Tempus. Cras Venenatis Sed Felis Sed Pulvinar. Mauris Orci Sapien, Convallis Scelerisque Nunc Id, Molestie Mattis Lectus. Vivamus Facilisis Eu Odio At Vestibulum. Mauris Id Odio Ut Libero Ornare Finibus.", R.drawable.discover);
        final BIDHome home4 = new BIDHome("Phasellus Dignissim, Tellus In Pellentesque Mollis, Mauris Orci Dignissim Nisl, Id Gravida Nunc Enim Quis Nibh. Maecenas Convallis Eros A Ante Dignissim, Vitae Elementum Metus Facilisis. Cras In Maximus Sem. Praesent Libero Augue, Ornare Eget Quam Sed, Volutpat Suscipit Arcu. Duis Ut Urna Commodo, Commodo Tellus Ac, Consequat Justo. Maecenas Nec Est Ac Purus Mattis Tristique Vitae Vel Leo. Duis Ac Eros Vel Nunc Aliquet Ultricies Vel At Metus. Praesent A Sagittis Leo. Maecenas Volutpat, Justo In Egestas Mattis, Lectus Dui Venenatis Ex, Consequat Imperdiet Velit Odio Eget Dolor. Mauris Commodo Cursus Metus Ut Lobortis. Nulla Eget Facilisis Nibh, Et Varius Justo. Ut Laoreet Purus At Neque Lacinia Tempus. Cras Venenatis Sed Felis Sed Pulvinar. Mauris Orci Sapien, Convallis Scelerisque Nunc Id, Molestie Mattis Lectus. Vivamus Facilisis Eu Odio At Vestibulum. Mauris Id Odio Ut Libero Ornare Finibus.", R.drawable.discover);

        homeList.add(home1);
        homeList.add(home2);
        homeList.add(home3);
        homeList.add(home4);
        return homeList;
    }

    private List<Restaurant> getRestaurantList() {
        final List<Restaurant> restaurantList = new ArrayList<>();

//        Restaurant restaurant1 = new Restaurant();
//        restaurant1.setVicinity("vicinity");
//        restaurant1.setName("QWER");
//
//        restaurantList.add(restaurant1);
        return restaurantList;
    }

    @Override
    public void clickRestaurant(int position, Restaurant restaurant) {
        super.clickRestaurant(position, restaurant);
        //AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new DiscoverDetailFragment(), R.id.frame_layout, true, false, false, null);
        Intent intent1 = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?q=loc:" + restaurant.getGeometry().getLocation().getLat() + "," + restaurant.getGeometry().getLocation().getLng()));
        startActivity(intent1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            Logger.e(TAG, "Location permission granted");
            //startLocationUpdates();
            displayLocationSettingsRequest();
        } else {
            Logger.e(TAG, "Location permission denied");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        getUserLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        // mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }


    private void displayLocationSettingsRequest() {
        Toast.makeText(HomeDiscoverFragment.this.getContext(),"displayLocationSettingsRequest",Toast.LENGTH_LONG);

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(requireActivity())
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        getUserLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    private void getUserLocation() {
        Toast.makeText(HomeDiscoverFragment.this.getContext(),"getUserLocation",Toast.LENGTH_LONG);

        SmartLocation.with(requireActivity()).location().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                Logger.e(TAG, "location : getLatitude : " + location.getLatitude() + " getLongitude : " + location.getLongitude());

                Toast.makeText(HomeDiscoverFragment.this.getContext(),"location : getLatitude : " + location.getLatitude() + " getLongitude : " + location.getLongitude(),Toast.LENGTH_LONG);

                final String locationLatLng = location.getLatitude() + "," + location.getLongitude();

                callFetchRestaurantsApi(locationLatLng, "5000", "restaurant", "AIzaSyBgdv6LKF4eOl0Ba1KHugHnkTYf0rn2sDs");
            }
        });
    }

    private void callFetchRestaurantsApi(final String location, final String radius, final String type, final String key) {

        Activity activity = getActivity();
        if (activity != null) {
            CustomProgressDialog.getInstance().showDialog(requireActivity(), true);

            final Call<RestaurantResponse> call = apiInterface.fetchRestaurants(location, radius, type, key);
            call.enqueue( new Callback<RestaurantResponse>() {
                @Override
                public void onResponse(Call<RestaurantResponse> call, Response<RestaurantResponse> response) {
                    CustomProgressDialog.getInstance().dismissDialog();
                    if (response.body() != null) {

                        final RestaurantResponse restaurantResponse = response.body();
                        final List<Restaurant> restaurantList = restaurantResponse.getRestaurantList();
                        initAdapter(restaurantList);

                    } else {
                        Toast.makeText(HomeDiscoverFragment.this.getContext(),"Failed.. Try Again!",Toast.LENGTH_LONG);

                    }
                }

                @Override
                public void onFailure(Call<RestaurantResponse> call, Throwable t) {
                    t.printStackTrace();
                    CustomProgressDialog.getInstance().dismissDialog();
                }
            });
        }
    }


}