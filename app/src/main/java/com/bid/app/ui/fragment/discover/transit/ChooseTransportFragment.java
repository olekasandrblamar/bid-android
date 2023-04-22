package com.bid.app.ui.fragment.discover.transit;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.SearchAutocompleteAdapter;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.AutoCompleteAdapterClickListener;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.GetWalletBalanceResponse;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.discover.transit.driver.DriverFragment;
import com.bid.app.ui.fragment.home.HomeWalletFragment;
import com.bid.app.ui.fragment.tripHistory.TripHistoryFragment;
import com.bid.app.ui.fragment.wallet.RewardFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChooseTransportFragment extends BaseFragment implements TextWatcher,View.OnClickListener, AutoCompleteAdapterClickListener {

    SessionManager sessionManager;
    RecyclerView suggestionsRecView;
    SearchAutocompleteAdapter autocompleteAdapter;
    Dialog searchDialog;
    EditText search;
    private APIInterface apiInterface;
    ImageView clearSearchText, closeSearch;
//    UrlImageView thumbNail;
    private RoundedImageView ivProfile;

    PlacesClient placesClient;
    Button selBusBtn;
    TextView searchBox, userName;
    LinearLayout ballanceLyt, rewardLyt, totalTripsLyt;
    TextView balance;
    TextView rewards;
    TextView totalTrips;


    View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_choose_transport, container, false);
        sessionManager = new SessionManager(requireActivity());
        initIds(root);
        initFragment();
        loadWalletInfo();
        return root;
    }



    private void initFragment() {
        ((DashboardActivity) requireActivity()).setTitleAndImage("Ride the bus", R.drawable.ic_back_arrow, false);
        userName.setText("Hello,\n" + sessionManager.getFirstName() + " " + sessionManager.getLastName());
        new Handler().post(new Runnable() {
            @Override
            public void run() {
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
        });

    }


    private void initIds(View view) {
        //Maps Fragment layouts
//        searchBox = view.findViewById(R.id.search_edit_text);
        userName = view.findViewById(R.id.user_name);
        selBusBtn = (Button)view.findViewById(R.id.sel_bus_btn);
        ivProfile = view.findViewById(R.id.iv_avatar_bid);
        ballanceLyt = view.findViewById(R.id.ballance_lyt);
        rewardLyt = view.findViewById(R.id.rewards_lyt);
        totalTripsLyt = view.findViewById(R.id.total_trips_lyt);
        //Places Init
        Places.initialize(requireContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(requireContext());
        //Search Dialog
        searchDialog = new Dialog(getContext());
        searchDialog.setContentView(R.layout.search_dialog);
        searchDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchDialog.getWindow().getAttributes().gravity = Gravity.TOP;
        search = searchDialog.findViewById(R.id.search_edit_text);
//        clearSearchText = searchDialog.findViewById(R.id.clear_img_v);
        closeSearch = searchDialog.findViewById(R.id.back_img_v);

        balance = view.findViewById(R.id.balance);
        rewards = view.findViewById(R.id.rewards);
        totalTrips = view.findViewById(R.id.total_trips);

        //search
  //      clearSearchText.setOnClickListener(this);
        closeSearch.setOnClickListener(this);
        ballanceLyt.setOnClickListener(this);
        rewardLyt.setOnClickListener(this);
        totalTripsLyt.setOnClickListener(this);

        search.addTextChangedListener(this);
        selBusBtn.setOnClickListener(this);
        suggestionsRecView = searchDialog.findViewById(R.id.sug_recycler);

        apiInterface = APIClient.getClient().create(APIInterface.class);

    }

    //Click listener interface
    @Override
    public void onClick(View view) {

//        if (view == searchBox) {
//            searchDialog.show();
//            searchDialog.setCancelable(false);
//            search.requestFocus();
//            setAutocompleteAdapter();
//
//        }
        if (view == closeSearch) {
            searchDialog.dismiss();

        } else if (view == clearSearchText) {
            search.getText().clear();
        } else if(view == ballanceLyt) {
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new HomeWalletFragment(), R.id.frame_layout, false, false, false, null);
        } else if(view == rewardLyt) {
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new RewardFragment(), R.id.frame_layout, true, false, false, null);
        } else if(view == totalTripsLyt) {
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TripHistoryFragment(), R.id.frame_layout, true, false, false, null);
        }
        else if(view == selBusBtn) {
            confirmTransitDlg();
        }

    }


    public void confirmTransitDlg() {
        if(sessionManager.getIsBus().equals("1")) {
            final Dialog dialog = new Dialog(requireActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_confirm_transit);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(true);

            LinearLayout passenger = dialog.findViewById(R.id.passenger);
            LinearLayout driver = dialog.findViewById(R.id.driver);

            passenger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new ScheduleFragment(), R.id.frame_layout, true, false, false, null);
                }
            });
            driver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new DriverFragment(), R.id.frame_layout, true, false, false, null);
                }
            });
            dialog.show();
        }
        else
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new ScheduleFragment(), R.id.frame_layout, true, false, false, null);

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
                addSearchMarker(place.getLatLng(), place.getName());

            }).addOnFailureListener((exception) -> {
//                Log.i(TAG, "Place not found: " + exception.getMessage());
            });

        }

    }

    private void addSearchMarker(LatLng latLng, String address) {
        searchBox.setText(address);
//        mMap.addMarker(new MarkerOptions()
//                .position(latLng)
//                .title(address));
//
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));

    }
    @Override
    public void onSearchedItemClick(int position) {
        getSearchedPlace(position);
        search.setText("");
        searchDialog.dismiss();
    }

    public void loadWalletInfo() {
        CustomProgressDialog.getInstance().showDialog(getContext(), true);
        Call<GetWalletBalanceResponse> listResponseCall = apiInterface.getWalletBalance(sessionManager.getAccessToken());
        listResponseCall.enqueue(new Callback<GetWalletBalanceResponse>() {
            @Override
            public void onResponse(Call<GetWalletBalanceResponse> call, Response<GetWalletBalanceResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                GetWalletBalanceResponse walletBalanceResponse = response.body();

                DecimalFormat moneyFormat = new DecimalFormat("$#,##0.00");
                balance.setText(moneyFormat.format(new Float(walletBalanceResponse.getBalance())));
//                balance.setText("$ " + walletBalanceResponse.getBalance());
                totalTrips.setText(walletBalanceResponse.getTotalTrips());
//                rewards.setText(walletBalanceResponse.getTotalTrips());


            }

            @Override
            public void onFailure(Call<GetWalletBalanceResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });

    }
}
