package com.bid.app.ui.fragment;

import static com.bid.app.ui.activity.DashboardActivity.context;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bid.app.R;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.ScheduleScanResult;
import com.bid.app.model.response.ScheduleTestLatestResponse;
import com.bid.app.model.response.StatusResponse;
import com.bid.app.model.view.CovidResult;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.discover.transit.driver.DriverFragment;
import com.bid.app.ui.fragment.home.HomeBIDFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Constants;
import com.bid.app.util.Logger;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScannerFragment extends BaseFragment {

    private static final String TAG = ScannerFragment.class.getSimpleName();

    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private CodeScanner mCodeScanner;

    private Boolean isPermissionGranted = false;
    private String currentDate = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scanner, container, false);

        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_scanner), R.drawable.ic_back_arrow, false);

        initController(view);


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDate = df.format(c);

        return view;
    }

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
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new HomeBIDFragment(), R.id.frame_layout, false, false, false, null);
            }
        });

        dialog.show();
    }

    private void initController(View view) {

        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(requireActivity(), scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Logger.e(TAG, "Result : " + result.toString());

                        // if (!Constans.strStatusRoamFree.equals("false")) {
                        scheduleTestScanResult(result.toString());
                        // }

                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    private void scheduleTestScanResult(String toString) {
        Logger.e(TAG, "getAccessToken " + sessionManager.getAccessToken());
        try {
            JSONObject recData = new JSONObject(toString);
            if(recData.has("type") && recData.getString("type").equals("ticket")) {
                String  ticketId = recData.getString("data");
                CustomProgressDialog.getInstance().showDialog(getContext(), true);
                Call<StatusResponse> listResponseCall = apiInterface.checkTicketWithQR(sessionManager.getAccessToken(), ticketId);
                listResponseCall.enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        CustomProgressDialog.getInstance().dismissDialog();
                        final StatusResponse statusResponse = response.body();
//                        AlertUtils.showAlerterSuccess(requireActivity(), status.getMessage());

                        if(statusResponse != null && statusResponse.getError().getCode().equals("0")){
                            new AlertDialog.Builder(context)
                                    .setTitle("Correct ticket!")
                                    .setMessage(statusResponse.getError().getMessage())
                                    .setCancelable(false)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new DriverFragment(), R.id.frame_layout, false, false, false, null);
//                                            dialog.dismiss();
                                            // Whatever...
//                                            final Bundle bundle = new Bundle();
//                                            bundle.putSerializable(IBundle.BUNDLE_SELECTED_PERSONAL_DETAILS, statusResponse.getTicket());
//
////                                    bundle.putString(IBundle.BUNDLE_SELECTED_PERSONAL_DETAILS, purchaseTicketResponse.toString());
//                                            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TicketFragment(), R.id.frame_layout, false, false, true, bundle);
                                        }
                                    }).show();
                        }
                        else if(statusResponse !=null){
                            AlertUtils.showAlerterWarning(requireActivity(), statusResponse.getError().getMessage());
                            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new DriverFragment(), R.id.frame_layout, false, false, false, null);
                        }
                        else{
                            AlertUtils.showAlerterWarning(requireActivity(), "Server response is wrong");
                            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new DriverFragment(), R.id.frame_layout, false, false, false, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        t.printStackTrace();
                        AlertUtils.showAlerterWarning(requireActivity(), "Net work Connection Problem!");
                        CustomProgressDialog.getInstance().dismissDialog();
                    }
                });



            }

        } catch (JSONException e) {
            final ScheduleScanResult request = new ScheduleScanResult();
            request.setReg_date(currentDate);
            request.setQr_code(toString);
            Logger.e(TAG, "request : " + request);

            Call<ScheduleTestLatestResponse> listResponseCall = apiInterface.schedule_test_scan(sessionManager.getAccessToken(), request);
            listResponseCall.enqueue(new Callback<ScheduleTestLatestResponse>() {
                @Override
                public void onResponse(Call<ScheduleTestLatestResponse> call, Response<ScheduleTestLatestResponse> response) {
                    CustomProgressDialog.getInstance().dismissDialog();
                    final ScheduleTestLatestResponse body = response.body();
                    final ErrorStatus status = body.getErrorStatus();
                    final CovidResult covidResult = body.getData();

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        if (covidResult.getStatus() != null) {
                            AlertUtils.showAlerterSuccess(requireActivity(), status.getMessage());

                            Bundle bundle = new Bundle();

                            bundle.putString(IBundle.BUNDLE_TEST_DATE, covidResult.getReg_date());
                            bundle.putString(IBundle.BUNDLE_TEST_TIME, covidResult.getUpdated_at());
                            bundle.putString(IBundle.BUNDLE_TEST_LOCATION, covidResult.getCenter().getName());
                            bundle.putString(IBundle.BUNDLE_TEST_TYPE, covidResult.getTest_type().getName());
                            bundle.putString(IBundle.BUNDLE_TEST_STATUS, covidResult.getStatus());
                            bundle.putString(IBundle.BUNDLE_TEST_STATUS_NAME, covidResult.getTest_status().getName());
                            bundle.putString(IBundle.BUNDLE_TEST_STATUS_COLOR, covidResult.getTest_status().getColor());
                            bundle.putString(IBundle.BUNDLE_TEST_STATUS_FONT_COLOR, covidResult.getTest_status().getFont_color());

                            Constants.strStatusRoamFree = "";
                            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new COVIDTestDetailReportFragment(), R.id.frame_layout, true, false, true, bundle);
                        }
                    } else {
                        AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ScheduleTestLatestResponse> call, Throwable t) {
                    t.printStackTrace();
                    CustomProgressDialog.getInstance().dismissDialog();
                }
            });

            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCodeScanner.releaseResources();
    }
}
