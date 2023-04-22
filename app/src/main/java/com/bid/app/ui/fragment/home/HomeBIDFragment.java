package com.bid.app.ui.fragment.home;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.BIDHomeAdapter;
import com.bid.app.constants.Global;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.request.BIDRequestModel;
import com.bid.app.model.response.BIDResponse;
import com.bid.app.model.response.RefreshTokenResponse;
import com.bid.app.model.response.ScheduleTestLatestResponse;
import com.bid.app.model.view.BIDHome;
import com.bid.app.model.view.CovidResult;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.RoamFree;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.activity.ScheduleTestActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.COVIDTestResultsFragment;
import com.bid.app.ui.fragment.ScannerFragment;
import com.bid.app.ui.fragment.checkup.DailyCheckupFragment;
import com.bid.app.ui.fragment.tripHistory.TripHistoryFragment;
import com.bid.app.ui.fragment.profile.PersonalDetailsFragment;
import com.bid.app.ui.fragment.share.SendYourDetailsFragment;
import com.bid.app.ui.fragment.triage.TriageShowFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Constants;
import com.bid.app.util.Logger;
import com.bid.app.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.makeramen.roundedimageview.RoundedImageView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeBIDFragment extends BaseFragment {

    private static final String TAG = HomeBIDFragment.class.getSimpleName();

    private SessionManager sessionManager;
    private APIInterface apiInterface;
    private RoundedImageView ivProfile;
    private AppCompatTextView tvName;
    private AppCompatTextView tvId;
    private AppCompatTextView tvDOB;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewHealthCare;
    private ImageView ivScan;
    private BottomAppBar bottomAppBar;
    private RoamFree roamFree;
    private List<BIDHome> homeList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_b_id, container, false);
        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        refreshTokenApi();

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_my_bid), R.drawable.ic_menu, true);
        initController(view);

        roamFreeApi();
        return view;
    }

    private void initController(View view) {

        ivProfile = view.findViewById(R.id.iv_avatar_bid);

        tvName = view.findViewById(R.id.tv_name_bid);
        tvId = view.findViewById(R.id.tv_id_bid);
        tvDOB = view.findViewById(R.id.tv_dob_bid);

        final ImageView ivShare = view.findViewById(R.id.iv_share_bid);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerViewHealthCare = view.findViewById(R.id.recycler_view_health_care);
        ivScan = view.findViewById(R.id.iv_qr_dashboard);


        bottomAppBar = view.findViewById(R.id.bottom_bar);

        final LinearLayout ivBID = bottomAppBar.findViewById(R.id.lt_bid_dashboard);
        final LinearLayout ivWallet = bottomAppBar.findViewById(R.id.lt_discover_wallet);
        final LinearLayout ivDiscover = bottomAppBar.findViewById(R.id.lt_discover_dashboard);


        setLayoutManager();
        ivBID.setOnClickListener(this);
        ivWallet.setOnClickListener(this);
        ivDiscover.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivScan.setOnClickListener(this);

        roamFree = new RoamFree();
    }

    @Override
    public void onResume() {
        super.onResume();

        Logger.e(TAG, "Global.isHealthCheckUp " + Global.isHealthCheckUp);

        if (Global.isHealthCheckUp) {
            initAdapterHealthCare(getHealthCareList());
            recyclerView.setVisibility(View.GONE);
            recyclerViewHealthCare.setVisibility(View.VISIBLE);
        } else {
            homeList=getHomeList();
            initAdapter(homeList);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerViewHealthCare.setVisibility(View.GONE);
        }

        final String fName = !StringUtils.isEmpty(sessionManager.getFirstName()) ? sessionManager.getFirstName() : "";
        final String lName = !StringUtils.isEmpty(sessionManager.getLastName()) ? sessionManager.getLastName() : "";
        final String fullName = fName + " " + lName;
        tvName.setText(fullName);
        tvDOB.setText(sessionManager.getDob());

        if (sessionManager.getDob() != null && !sessionManager.getDob().isEmpty()) {
            tvDOB.setText(Utils.changeBEDateFormatToFE(sessionManager.getDob()));
        }

        tvId.setText(sessionManager.getBid());

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



    private void setLayoutManager() {
        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(DashboardActivity.context, 2);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    private void initAdapter(final List<BIDHome> list) {
        final BIDHomeAdapter bidHomeAdapter = new BIDHomeAdapter(requireActivity(), list, HomeBIDFragment.this, Constants.strStatusRoamFree);
        recyclerView.setAdapter(bidHomeAdapter);
        bidHomeAdapter.notifyDataSetChanged();
    }

    private List<BIDHome> getHomeList() {
        final List<BIDHome> homeList = new ArrayList<>();
        final BIDHome home1 = new BIDHome("Personal Details", R.drawable.ic_user_personal);
       // final BIDHome home4 = new BIDHome("Roam Free", R.drawable.ic_footprint);
        final BIDHome healthCare = new BIDHome("Healthcare", R.drawable.ic_heart);
//        final BIDHome home4 = new BIDHome("Roam Free", R.drawable.ic_footprint);
        final BIDHome home6 = new BIDHome("Id Documents", R.drawable.id_documents);
        final BIDHome chatWithUs = new BIDHome("Chat With Us", R.drawable.ic_chat);
        final BIDHome trips = new BIDHome("Trips", R.drawable.ticket);



        homeList.add(home1);

        homeList.add(healthCare);
        homeList.add(home6);
        homeList.add(chatWithUs);
        homeList.add(trips);
//        homeList.add(home4);

        Logger.e(TAG, "homeList : " + homeList.toString());

        return homeList;
    }

    private void initAdapterHealthCare(final List<BIDHome> list) {
        final BIDHomeAdapter bidHomeAdapter = new BIDHomeAdapter(requireActivity(), list, HomeBIDFragment.this, Constants.strStatusRoamFree);
        recyclerViewHealthCare.setAdapter(bidHomeAdapter);
        bidHomeAdapter.notifyDataSetChanged();
    }

    private List<BIDHome> getHealthCareList() {
        final List<BIDHome> homeList = new ArrayList<>();
        final BIDHome home1 = new BIDHome("Check Up", R.drawable.ic_personal_details);
        final BIDHome home2 = new BIDHome("Book A Test", R.drawable.ic_booa_a_test);
        final BIDHome home3 = new BIDHome("Results", R.drawable.ic_results);
        final BIDHome home4 = new BIDHome("Triage", R.drawable.ic_results);

        homeList.add(home1);
        homeList.add(home2);
        homeList.add(home3);
        homeList.add(home4);

        return homeList;
    }


    private void roamFreeApi() {
        final Call<ScheduleTestLatestResponse> call = apiInterface.schedule_test_latest(sessionManager.getAccessToken());
        call.enqueue(new Callback<ScheduleTestLatestResponse>() {
            @Override
            public void onResponse(Call<ScheduleTestLatestResponse> call, Response<ScheduleTestLatestResponse> response) {

                if (response.body() != null) {
                    final ScheduleTestLatestResponse body = response.body();
                    final ErrorStatus status = body.getErrorStatus();
                    CovidResult covidResult=body.getData();

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        if (body.getData() != null && body.getData().getStatus() != null) {
                            if (body.getData().getStatus().equals("2") || body.getData().getStatus().equals("4")) {
                                Constants.strStatusRoamFree = "true";


                                roamFree.setUsername(covidResult.getUser().getName());
                                roamFree.setCenter(covidResult.getCenter().getName());
                                roamFree.setRoamStart(covidResult.getRoam_free_start());
                                roamFree.setRoamEnd(covidResult.getRoam_free_end());
                                roamFree.setStatus(covidResult.getStatus());
                                //roamFree.setCountry(covidResult.getAddress().getCountry());
                                roamFree.setDob(covidResult.getUser().getDob());
                                roamFree.setPassportNumber("#");
                                roamFree.setTestType(covidResult.getTest_type().getName());
                                roamFree.setQr_code(covidResult.getQr_code());

                            } else {
                                Constants.strStatusRoamFree = "false";
                                AlertUtils.showAlerterWarning(requireActivity(),"Please receive a negative covid-19 test result or full vaccination.");
                            }
                        } else {
                            Constants.strStatusRoamFree = "false";
                        }

                    } else {
//                        AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                    }
                } else {
                    Logger.e(TAG, "response.body() == null");
                }
            }

            @Override
            public void onFailure(Call<ScheduleTestLatestResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_share_bid:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new SendYourDetailsFragment(), R.id.frame_layout, true, false, false, null);
                break;
            case R.id.iv_qr_dashboard:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new ScannerFragment(), R.id.frame_layout, true, false, false, null);
                break;
            case R.id.lt_bid_dashboard:
                Global.isHealthCheckUp = false;
//                tvTitle.setText(getResources().getString(R.string.title_my_bid));
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new HomeBIDFragment(), R.id.frame_layout, false, false, false, null);
                break;
            case R.id.lt_discover_wallet:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new HomeWalletFragment(), R.id.frame_layout, false, false, false, null);
                break;
            case R.id.lt_discover_dashboard:
//                tvTitle.setText(getResources().getString(R.string.title_discover));
                // AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new HomeDiscoverFragment(), R.id.frame_layout, true, false, false, null);
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new DiscoverPlaceFragment(), R.id.frame_layout, false, false, false, null);
                break;
        }
    }

    @Override
    public void clickOnBIDHome(int position, BIDHome home) {
        super.clickOnBIDHome(position, home);
        if ("Personal Details".equalsIgnoreCase(home.getName())) {
            Global.isHealthCheckUp = false;
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new PersonalDetailsFragment(), R.id.frame_layout, true, false, false, null);
        } else if ("Id Documents".equalsIgnoreCase(home.getName())) {
            Global.isHealthCheckUp = false;
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new IdDocumentsFragment(), R.id.frame_layout, true, false, false, null);
        } else if ("Trips".equalsIgnoreCase(home.getName())) {
            Global.isHealthCheckUp = false;
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TripHistoryFragment(), R.id.frame_layout, true, false, false, null);
        } else if ("Schedule Your Covid Test".equalsIgnoreCase(home.getName())) {
            Global.isHealthCheckUp = false;
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), ScheduleTestActivity.class, false, false, false, null);
        } else if ("Your COVID Results".equalsIgnoreCase(home.getName())) {
            Global.isHealthCheckUp = false;
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new COVIDTestResultsFragment(), R.id.frame_layout, true, false, false, null);
        } else if ("Roam Free".equalsIgnoreCase(home.getName())) {
            Global.isHealthCheckUp = false;
            if (Constants.strStatusRoamFree.equals("true")) {
                //reportDialog();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Roam Free", roamFree);
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new RoamFreeFragment(), R.id.frame_layout, true, false, true, bundle);
            }
        } else if ("Healthcare".equalsIgnoreCase(home.getName())) {
            Global.isHealthCheckUp = true;
            if (isProfileCompleted()) {
                if (userHasBID()) {
                    recyclerView.setVisibility(View.GONE);
                    recyclerViewHealthCare.setVisibility(View.VISIBLE);
                    initAdapterHealthCare(getHealthCareList());
                } else {
                    getUserBid();
                }
            } else {
                AlertUtils.showAlerterWarning(getActivity(), "You must complete your FirstName and your LastName first !");
            }
        } else if ("Check Up".equalsIgnoreCase(home.getName())) {
            Global.isHealthCheckUp = true;
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new DailyCheckupFragment(), R.id.frame_layout, true, false, false, null);

        } else if ("Book A Test".equalsIgnoreCase(home.getName())) {
            showBottomSheetDialog();

        } else if ("Results".equalsIgnoreCase(home.getName())) {
            Global.isHealthCheckUp = true;
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new COVIDTestResultsFragment(), R.id.frame_layout, true, false, false, null);

        } else if ("Chat With Us".equalsIgnoreCase(home.getName())) {
            AlertUtils.showAlerterWarning(requireActivity(), "Coming Soon!");

        }
        else if("Triage".equalsIgnoreCase(home.getName())) {
            Global.isHealthCheckUp = true;
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TriageShowFragment(), R.id.frame_layout, true, false, false, null);

        }
        else {
            Logger.e(TAG, "No fragment attached");
        }
    }

    private void getUserBid() {

        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);

        BIDRequestModel bidRequestModel = new BIDRequestModel();
        bidRequestModel.setFirst_name(sessionManager.getFirstName());
        bidRequestModel.setLast_name(sessionManager.getLastName());
        bidRequestModel.setUser_id(sessionManager.getUserId());

        final Call<BIDResponse> call = apiInterface.getUserBID(sessionManager.getAccessToken(), bidRequestModel);
        call.enqueue(new Callback<BIDResponse>() {
            @Override
            public void onResponse(Call<BIDResponse> call, Response<BIDResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                final BIDResponse bidResponse = response.body();

                if (bidResponse != null) {
                    final ErrorStatus status = bidResponse.getError();
                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        sessionManager.setBid(bidResponse.getB_id());
                        Log.d(TAG, "setBid: " + sessionManager.getBid());

                        recyclerView.setVisibility(View.GONE);
                        recyclerViewHealthCare.setVisibility(View.VISIBLE);
                        initAdapterHealthCare(getHealthCareList());

                    } else {
                        AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<BIDResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();

            }
        });
    }

    private boolean userHasBID() {
        return !sessionManager.getBid().isEmpty() && !sessionManager.getBid().equalsIgnoreCase("ID-default");
    }

    private boolean isProfileCompleted() {
        return !sessionManager.getFirstName().isEmpty() && !sessionManager.getLastName().isEmpty();
    }


    private void showBottomSheetDialog() {
//        final ReasonForTestBottomSheetDialog reasonForTestBottomSheetDialogFragment = ReasonForTestBottomSheetDialog.newInstance();
//        reasonForTestBottomSheetDialogFragment.show(requireActivity().getSupportFragmentManager(), ReasonForTestBottomSheetDialog.TAG);

          final DiseasesOfBottomSheetDialog diseasesOfBottomSheetDialog = DiseasesOfBottomSheetDialog.newInstance();
          diseasesOfBottomSheetDialog.show(requireActivity().getSupportFragmentManager(), DiseasesOfBottomSheetDialog.TAG);
    }

    // ----- Report Issue Dialog
    private void reportDialog() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_report_issue);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        TextView txtSubmit = (TextView) dialog.findViewById(R.id.txtSubmit);

        txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void refreshTokenApi() {

        final Call<RefreshTokenResponse> call = apiInterface.refresh_token(sessionManager.getRefreshToken());
        call.enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, Response<RefreshTokenResponse> response) {

                final RefreshTokenResponse body = response.body();
                final ErrorStatus status = body.getErrorStatus();

                if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                    sessionManager.setAccessToken(body.getAccess_token());
                    sessionManager.setRefreshToken(body.getRefresh_token());
                } else {
                    //    AlertUtils.showAlerterFailure(DashboardActivity.this, status.getMessage());
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }

}
