package com.bid.app.ui.fragment.home;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bid.app.R;
import com.bid.app.constants.Global;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.GetWalletBalanceResponse;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.ScannerFragment;
import com.bid.app.ui.fragment.discover.transit.driver.DriverFragment;
import com.bid.app.ui.fragment.discover.transit.ChooseTransportFragment;
import com.bid.app.ui.fragment.wallet.TopUpFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscoverPlaceFragment extends BaseFragment { // ST
    private ImageView ivScan;
    private BottomAppBar bottomAppBar;
    private LinearLayout discover_transit;
    private LinearLayout discover_flights;
    private LinearLayout discover_boat;
    private LinearLayout discover_activities;
    private LinearLayout discover_mobile_top_up;
    private LinearLayout discover_bill_payment;
    private LinearLayout discover_pay;
    private LinearLayout discover_top_up;
    private LinearLayout discover_more;
    private LinearLayout discover_bluestone_wallet;
    private TextView amount;
    private SessionManager sessionManager;
    private APIInterface apiInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover_place, container, false);
        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_discover), R.drawable.ic_menu, true);
        initController(view);
        run();
        return view;
    }
    private void run() {
        loadWalletInfo();
    }
    private void initController(View view) {
        initializeWebView(view);
        ivScan = view.findViewById(R.id.iv_qr_dashboard);
        bottomAppBar = view.findViewById(R.id.bottom_bar);
        discover_transit = view.findViewById(R.id.discover_transit);
        discover_flights = view.findViewById(R.id.discover_flights);
        discover_boat = view.findViewById(R.id.discover_boat);
        discover_activities = view.findViewById(R.id.discover_activities);
        discover_mobile_top_up = view.findViewById(R.id.discover_mobile_top_up);
        discover_bill_payment = view.findViewById(R.id.discover_bill_payment);
        discover_pay = view.findViewById(R.id.discover_pay);
        discover_top_up = view.findViewById(R.id.discover_top_up);
        discover_more = view.findViewById(R.id.discover_more);
        discover_bluestone_wallet = view.findViewById(R.id.discover_blueston_wallet);
        amount = view.findViewById(R.id.amount);
        sessionManager = new SessionManager(getContext());

        final LinearLayout ivBID = bottomAppBar.findViewById(R.id.lt_bid_dashboard);
        final LinearLayout ivWallet = bottomAppBar.findViewById(R.id.lt_discover_wallet);
        final LinearLayout ivDiscover = bottomAppBar.findViewById(R.id.lt_discover_dashboard);
        ivBID.setOnClickListener(this);
        ivWallet.setOnClickListener(this);
        ivDiscover.setOnClickListener(this);
        ivScan.setOnClickListener(this);
        discover_transit.setOnClickListener(this);
        discover_flights.setOnClickListener(this);
        discover_boat.setOnClickListener(this);
        discover_activities.setOnClickListener(this);
        discover_mobile_top_up.setOnClickListener(this);
        discover_bill_payment.setOnClickListener(this);
        discover_pay.setOnClickListener(this);
        discover_top_up.setOnClickListener(this);
        discover_more.setOnClickListener(this);
        discover_bluestone_wallet.setOnClickListener(this);

//        discover_driver.setVisibility();
        apiInterface = APIClient.getClient().create(APIInterface.class);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_qr_dashboard:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new ScannerFragment(), R.id.frame_layout, true, false, false, null);
                break;
            case R.id.lt_bid_dashboard:
                Global.isHealthCheckUp = false;
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new HomeBIDFragment(), R.id.frame_layout, false, false, false, null);
                break;
            case R.id.lt_discover_wallet:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new HomeWalletFragment(), R.id.frame_layout, false, false, false, null);
                break;
            case R.id.lt_discover_dashboard:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new DiscoverPlaceFragment(), R.id.frame_layout, false, false, false, null);
                break;
            case R.id.discover_transit:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new ChooseTransportFragment(), R.id.frame_layout, true, false, false, null);
                break;
            case R.id.discover_flights:
                AlertUtils.showAlerterWarning(requireActivity(), "Coming Soon!");
                break;
            case R.id.discover_boat:
                AlertUtils.showAlerterWarning(requireActivity(), "Coming Soon!");
                break;
            case R.id.discover_activities:
                AlertUtils.showAlerterWarning(requireActivity(), "Coming Soon!");
                break;
            case R.id.discover_mobile_top_up:
                AlertUtils.showAlerterWarning(requireActivity(), "Coming Soon!");
                break;
            case R.id.discover_bill_payment:
                AlertUtils.showAlerterWarning(requireActivity(), "Coming Soon!");
                break;
            case R.id.discover_blueston_wallet:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new HomeWalletFragment(), R.id.frame_layout, false, false, false, null);
                break;
            case R.id.discover_pay:
                AlertUtils.showAlerterWarning(requireActivity(), "Coming Soon!");
                break;
            case R.id.discover_top_up:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TopUpFragment(), R.id.frame_layout, true, false, false, null);
                break;
            case R.id.discover_more:
                AlertUtils.showAlerterWarning(requireActivity(), "Coming Soon!");
                break;
        }
    }


    public void initializeWebView(View view) {
        WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.setBackgroundColor(0);
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
//                dialog.dismiss();
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl("https://www.triage.com/create-account");
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 100) {
//                    dialog.dismiss();
//                }
            }
        });
    }

    public void loadWalletInfo() {
        CustomProgressDialog.getInstance().showDialog(getContext(), true);
        Call<GetWalletBalanceResponse> listResponseCall = apiInterface.getWalletBalance(sessionManager.getAccessToken());
        listResponseCall.enqueue(new Callback<GetWalletBalanceResponse>() {
            @Override
            public void onResponse(Call<GetWalletBalanceResponse> call, Response<GetWalletBalanceResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                GetWalletBalanceResponse walletBalanceResponse = response.body();
                if(walletBalanceResponse == null) return;
                DecimalFormat moneyFormat = new DecimalFormat("$#,##0.00");
                amount.setText(moneyFormat.format(new Float(walletBalanceResponse.getBalance())));

            }

            @Override
            public void onFailure(Call<GetWalletBalanceResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });

    }
}
