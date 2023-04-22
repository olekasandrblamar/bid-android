package com.bid.app.ui.fragment.share;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bid.app.R;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.util.Logger;

public class SendMethodFragment extends BaseFragment {

    private static final String TAG = SendMethodFragment.class.getSimpleName();

    private CardView cardViewFaceToFace;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_send_method, container, false);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_send_method), R.drawable.ic_back_arrow, false);

        Logger.e(TAG, "getPersonalDetails " + getPersonalDetails());

        initController(view);

        return view;
    }

    private String getPersonalDetails() {
        final Bundle bundle = getArguments();
        return bundle != null ? bundle.getString(IBundle.BUNDLE_SELECTED_PERSONAL_DETAILS) : "";
    }

    private void initController(View view) {

        cardViewFaceToFace = view.findViewById(R.id.card_view_send_method);


        cardViewFaceToFace.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.card_view_send_method:
                final Bundle bundle = new Bundle();
                bundle.putString(IBundle.BUNDLE_SELECTED_PERSONAL_DETAILS, getPersonalDetails());
                AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new FaceToFaceFragment(), R.id.frame_layout, true, false, true, bundle);
                break;

        }
    }

}
