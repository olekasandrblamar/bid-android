package com.bid.app.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.bid.app.R;
import com.bid.app.adaper.MenuAdapter;
import com.bid.app.constants.Global;
import com.bid.app.duonav.views.DuoDrawerLayout;
import com.bid.app.duonav.views.DuoMenuView;
import com.bid.app.duonav.widgets.DuoDrawerToggle;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.CheckNotificationResponse;
import com.bid.app.model.response.RefreshTokenResponse;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.base.BaseActivity;
import com.bid.app.ui.fragment.TransactionHistoryFragment;
import com.bid.app.ui.fragment.about.AboutListFragment;
import com.bid.app.ui.fragment.home.HomeBIDFragment;
import com.bid.app.ui.fragment.menu.DownloadDataFragment;
import com.bid.app.ui.fragment.menu.HelpFragment;
import com.bid.app.ui.fragment.menu.SendFeedbackFragment;
import com.bid.app.ui.fragment.menu.SettingsFragment;
import com.bid.app.ui.fragment.menu.TellAFriendFragment;
import com.bid.app.ui.fragment.notification.NotificationsListFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.Logger;
import com.bid.app.util.Utils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends BaseActivity implements DuoMenuView.OnMenuClickListener {

    private static final String TAG = DashboardActivity.class.getSimpleName();
    private SessionManager sessionManager;
    private APIInterface apiInterface;
    private Toolbar toolbar;
    private MenuAdapter mMenuAdapter;
    private ViewHolder mViewHolder;
    private ArrayList<com.bid.app.model.view.Menu> mTitles = new ArrayList<>();
    private AppCompatTextView tvTitle;
    private ImageView ivBack;
    private ImageView ivRight;
    private ImageView ivBag;
    private DuoDrawerLayout drawerLayout;
    private DuoDrawerToggle drawerToggle;
    public static Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        context = this;
        sessionManager = new SessionManager(DashboardActivity.this);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        handleMenu();

        initToolBar();

        initBottomAppBar();

        initController();

        getPermission();
    }

    private void initToolBar() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(null);

        ivBack = toolbar.findViewById(R.id.iv_back_tool_bar);
        ivRight = toolbar.findViewById(R.id.iv_right_tool_bar);
        tvTitle = toolbar.findViewById(R.id.tv_title_tool_bar);
        ivBag = toolbar.findViewById(R.id.iv_bag);

        ivBack.setImageResource(R.drawable.ic_menu);

        ivBack.setOnClickListener(this);
        ivRight.setOnClickListener(this);
    }

    private void initBottomAppBar() {

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new DuoDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

    }

    private void initController() {
        AppActivityManager.redirectTo(DashboardActivity.this, new HomeBIDFragment(), R.id.frame_layout, false, false, false, null);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.iv_back_tool_bar:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    if (drawerLayout.isDrawerOpen()) {
                        drawerLayout.closeDrawer();
                    } else {
                        drawerLayout.openDrawer();
                    }
                    Utils.hideKeyboard(DashboardActivity.this);
                }
                break;

            case R.id.iv_right_tool_bar:
                AppActivityManager.redirectTo(DashboardActivity.this, new NotificationsListFragment(), R.id.frame_layout, true, false, false, null);
                break;
        }
    }

    public void setTitleAndImage(final String title, final int resource, final boolean isVisible) {
        tvTitle.setText(title);
        ivBack.setImageResource(resource);
        if (isVisible) {
            ivRight.setVisibility(View.VISIBLE);
            checkNotification();
        } else {
            ivRight.setVisibility(View.GONE);
            ivBag.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFooterClicked() {
        Toast.makeText(this, "onFooterClicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHeaderClicked() {
        Toast.makeText(this, "onHeaderClicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        // Set the toolbar title
        setTitle(mTitles.get(position).getName());

        // Set the right options selected
        mMenuAdapter.setViewSelected(position, true);

        // Navigate to the right fragment
        switch (position) {

            case 0:
                Global.isHealthCheckUp = false;
                AppActivityManager.redirectTo(DashboardActivity.this, new HomeBIDFragment(), R.id.frame_layout, false, false, false, null);
                break;

            case 1:
                AppActivityManager.redirectTo(DashboardActivity.this, new TellAFriendFragment(), R.id.frame_layout, false, false, false, null);
                break;

            case 2:
                AppActivityManager.redirectTo(DashboardActivity.this, new HelpFragment(), R.id.frame_layout, false, false, false, null);
                break;

            case 3:
                AppActivityManager.redirectTo(DashboardActivity.this, new SendFeedbackFragment(), R.id.frame_layout, false, false, false, null);
                break;

            case 4:
                AppActivityManager.redirectTo(DashboardActivity.this, new DownloadDataFragment(), R.id.frame_layout, false, false, false, null);
                break;

            case 5:
                AppActivityManager.redirectTo(DashboardActivity.this, new SettingsFragment(), R.id.frame_layout, false, false, false, null);
                break;

            case 6:
                AppActivityManager.redirectTo(DashboardActivity.this, new TransactionHistoryFragment(), R.id.frame_layout, true, false, false, null);
                break;

            case 7:
                AppActivityManager.redirectTo(DashboardActivity.this, new AboutListFragment(), R.id.frame_layout, false, false, false, null);
                break;

            case 8:
                sessionManager.clearSession();
                AppActivityManager.redirectTo(DashboardActivity.this, OnBoardingActivity.class, true, false, false, null);
                break;
        }

        // Close the drawer
        mViewHolder.mDuoDrawerLayout.closeDrawer();
    }

    private class ViewHolder {
        private DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;
        private Toolbar mToolbar;

        ViewHolder() {
            mDuoDrawerLayout = findViewById(R.id.drawer_layout);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
            mToolbar = findViewById(R.id.toolbar);
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }


    private void handleMenu() {
        mViewHolder = new ViewHolder();

        com.bid.app.model.view.Menu home = new com.bid.app.model.view.Menu("Home", R.drawable.ic_home);
        com.bid.app.model.view.Menu tellAFriend = new com.bid.app.model.view.Menu("Tell a Friend", R.drawable.ic_tell_a_friend);
        com.bid.app.model.view.Menu help = new com.bid.app.model.view.Menu("Help", R.drawable.ic_help);
        com.bid.app.model.view.Menu feedback = new com.bid.app.model.view.Menu("Send us Feedback", R.drawable.ic_mail);
        com.bid.app.model.view.Menu myData = new com.bid.app.model.view.Menu("My Data", R.drawable.ic_download);
        com.bid.app.model.view.Menu settings = new com.bid.app.model.view.Menu("Settings", R.drawable.ic_setting);
        com.bid.app.model.view.Menu history = new com.bid.app.model.view.Menu("Transaction History", R.drawable.ic_credit_card);
        com.bid.app.model.view.Menu about = new com.bid.app.model.view.Menu("About", R.drawable.ic_about);
        com.bid.app.model.view.Menu logout = new com.bid.app.model.view.Menu("Logout", R.drawable.ic_logout);

        mTitles.add(home);
        mTitles.add(tellAFriend);
        mTitles.add(help);
        mTitles.add(feedback);
        mTitles.add(myData);
        mTitles.add(settings);
        mTitles.add(history);
        mTitles.add(about);
        mTitles.add(logout);

        mMenuAdapter = new MenuAdapter(mTitles);
        mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
        mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
        mMenuAdapter.setViewSelected(0, true);
    }

    private void getPermission() {
        Dexter.withActivity(DashboardActivity.this)
                .withPermissions(Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Logger.e(TAG, "areAllPermissionsGranted");
                            AppActivityManager.redirectTo(DashboardActivity.this, new HomeBIDFragment(), R.id.frame_layout, false, false, false, null);
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Logger.e(TAG, "onPermissionRationaleShouldBeShown");
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Logger.e(TAG, "error" + error.toString());
            }
        })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshTokenApi();

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


    private void checkNotification() {

        final Call<CheckNotificationResponse> call = apiInterface.checkNotifications(sessionManager.getAccessToken());
        call.enqueue(new Callback<CheckNotificationResponse>() {
            @Override
            public void onResponse(Call<CheckNotificationResponse> call, Response<CheckNotificationResponse> response) {

                final CheckNotificationResponse body = response.body();
                final ErrorStatus status = body.getError();

                if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                    if(body.getData().equals("1")) {
//                        ivRight.setBackgroundColor(Color.MAGENTA);
                        ivBag.setVisibility(View.VISIBLE);
                    }
                    else {
//                        ivRight.setBackgroundColor(Color.WHITE);
                        ivBag.setVisibility(View.GONE);
                    }
                } else {
//                    ivRight.setBackgroundColor(Color.WHITE);
                    ivBag.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CheckNotificationResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }
}
