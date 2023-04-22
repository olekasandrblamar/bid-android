package com.bid.app.ui.fragment.address;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.AddressListAdapter;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.AddressListResponse;
import com.bid.app.model.view.Address;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentAddressFragment extends BaseFragment {

    private static final String TAG = CurrentAddressFragment.class.getSimpleName();

    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private RecyclerView recyclerView;
    private AppCompatTextView tvNoAddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_address_list, container, false);

        ((DashboardActivity) Objects.requireNonNull(requireActivity())).setTitleAndImage(getResources().getString(R.string.title_address), R.drawable.ic_back_arrow, false);

        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        initController(view);

        return view;
    }

    private void initController(View view) {

        final Button btnAddAddress = view.findViewById(R.id.btn_add_address);

        recyclerView = view.findViewById(R.id.recycler_view);
        tvNoAddress = view.findViewById(R.id.tv_no_address);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.context);
        recyclerView.setLayoutManager(layoutManager);

        btnAddAddress.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        callCurrentAddressApi();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_add_address:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new AddAddressFragment(), R.id.frame_layout, true, false, false, null);
                break;
        }
    }

    private void initAdapter(final List<Address> list) {
        final AddressListAdapter addressListAdapter = new AddressListAdapter(requireActivity(), list, CurrentAddressFragment.this);
        recyclerView.setAdapter(addressListAdapter);

        if (addressListAdapter.getItemCount() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            tvNoAddress.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            tvNoAddress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void clickOnAddress(int position, Address address, boolean isDelete) {
        super.clickOnAddress(position, address, isDelete);
        if (isDelete){

        }
    }

    private void callCurrentAddressApi() {
        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);
        Logger.e(TAG, "getAccessToken " + sessionManager.getAccessToken());

        Call<AddressListResponse> listResponseCall = apiInterface.getUserAddress(sessionManager.getAccessToken());
        listResponseCall.enqueue(new Callback<AddressListResponse>() {
            @Override
            public void onResponse(Call<AddressListResponse> call, Response<AddressListResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();

                Logger.e(TAG, "response.body() " + response.body());
                final AddressListResponse addressListResponse = response.body();

                if (addressListResponse != null) {
                    final ErrorStatus status = addressListResponse.getErrorStatus();

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        AlertUtils.showAlerterSuccess(requireActivity(), status.getMessage());
                        initAdapter(addressListResponse.getAddress());
                    } else {
                        AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<AddressListResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }


}
