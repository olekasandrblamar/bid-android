package com.bid.app.ui.fragment;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bid.app.R;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.view.CovidResult;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.util.Utils;

public class COVIDTestDetailReportFragment extends BaseFragment {

    private static final String TAG = COVIDTestDetailReportFragment.class.getSimpleName();

    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private TextView tvDate;
    private TextView tvTime;
    private TextView tvLocation;
    private TextView tvTestType;
    private TextView tvStatus;
    private TextView tvStatusText;

    //private TextView tvCenterName;
    private TextView tvScan;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_test_result_details, container, false);

        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_covid_test_result), R.drawable.ic_back_arrow, false);

        initController(view);

        return view;
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            tvDate.setText(bundle.getString(IBundle.BUNDLE_TEST_DATE));
            tvTime.setText(Utils.getTime(bundle.getString(IBundle.BUNDLE_TEST_TIME)));
            tvLocation.setText(bundle.getString(IBundle.BUNDLE_TEST_LOCATION));
            tvTestType.setText(bundle.getString(IBundle.BUNDLE_TEST_TYPE));

            String status =bundle.getString(IBundle.BUNDLE_TEST_STATUS_NAME,"");
            tvStatus.setText(status);
            tvStatusText.setText(status);

            int fontColor= Color.parseColor(bundle.getString(IBundle.BUNDLE_TEST_STATUS_FONT_COLOR));
            int backgroundColor=Color.parseColor(bundle.getString(IBundle.BUNDLE_TEST_STATUS_COLOR));
            tvStatus.setTextColor(fontColor);
            changeStatusTestBackgroundColor(backgroundColor);


        }
    }

    private void changeStatusTestBackgroundColor(int backgroundColor) {

        GradientDrawable shapeDrawable = (GradientDrawable) AppCompatResources.getDrawable(requireContext(),R.drawable.shape_negative_result);
        shapeDrawable.setColor(backgroundColor);
        tvStatus.setBackground(shapeDrawable);
    }

    private void initController(View view) {

        tvDate = view.findViewById(R.id.tv_date_test_details);
        tvTime = view.findViewById(R.id.tv_time_test_details);
        tvLocation = view.findViewById(R.id.tv_location_test_details);
        tvTestType = view.findViewById(R.id.tv_type_test_details);
        tvStatus = view.findViewById(R.id.tv_status_test_result_details);
        tvStatusText = view.findViewById(R.id.tv_status_test_details);

        //tvCenterName = view.findViewById(R.id.tv_scan_center_test_details);
        tvScan = view.findViewById(R.id.tv_scan_test_details);

        tvScan.setOnClickListener(this);

        getBundle();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.tv_scan_test_details:
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new ScannerFragment(), R.id.frame_layout, true, false, false, null);
                break;

        }
    }

    @Override
    public void clickOnResult(int position, CovidResult covidResult) {
        super.clickOnResult(position, covidResult);
    }
}
