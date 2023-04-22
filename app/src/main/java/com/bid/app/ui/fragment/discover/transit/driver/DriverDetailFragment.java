package com.bid.app.ui.fragment.discover.transit.driver;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bid.app.R;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.model.response.GetAvailableRoutesResponse;
import com.bid.app.model.response.GetAvailableSeatResponse;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;


public class DriverDetailFragment extends BaseFragment {
    private static final String TAG = DriverDetailFragment.class.getSimpleName();

    SessionManager sessionManager;
    View root;
    TextView name, rides, ratings, years, firstName, lastName, mobile;
    RoundedImageView roundedImageView;
    Button logout;
    private APIInterface apiInterface;
    GetAvailableRoutesResponse availableRoutesResponse;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_driver_detail, container, false);
        sessionManager = new SessionManager(requireActivity());
        getBundle();
        initIds(root, savedInstanceState);
        initFragment();
        run();
        return root;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            availableRoutesResponse = (GetAvailableRoutesResponse) bundle.getSerializable(IBundle.BUNDLE_ROUTE);
        }
    }
    private void initFragment() {
        name.setText(sessionManager.getFirstName() +" " + sessionManager.getLastName());
        rides.setText("0");
        ratings.setText("0");
        years.setText("45.5");
        firstName.setText(sessionManager.getFirstName());
        lastName.setText(sessionManager.getLastName());
        mobile.setText(sessionManager.getMobileNumber());
        Glide.with(requireActivity())
                .load(sessionManager.getUserImage())
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .skipMemoryCache(false)
                .centerCrop()
//                        .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(roundedImageView);
    }


    private void initIds(View view, Bundle savedInstanceState) {
        ((DashboardActivity) requireActivity()).setTitleAndImage("bus No: " + sessionManager.getUserId(), R.drawable.ic_back_arrow, false);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        roundedImageView = view.findViewById(R.id.iv_avatar_bid);
        name = view.findViewById(R.id.name);
        rides = view.findViewById(R.id.rides);
        ratings = view.findViewById(R.id.rating);
        years = view.findViewById(R.id.years);
        firstName = view.findViewById(R.id.first_name);
        lastName = view.findViewById(R.id.last_name);
        mobile = view.findViewById(R.id.mobile_number);
        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(this);
    }

    private void run() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.logout:

                break;


        }
    }

}
