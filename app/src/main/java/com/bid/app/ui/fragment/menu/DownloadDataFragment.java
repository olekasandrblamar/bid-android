package com.bid.app.ui.fragment.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bid.app.R;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.retrofit.APIClient;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;

public class DownloadDataFragment extends BaseFragment {

    private static final String TAG = DownloadDataFragment.class.getSimpleName();

    private APIInterface apiInterface;

    private AppCompatTextView tvDownloadData;
    private AppCompatImageView ivDownloadData;


    private AppCompatButton btnDownload;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_download_my_data, container, false);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_download_data), R.drawable.ic_menu, true);

        initController(view);

        return view;
    }

    private void initController(View view) {
        tvDownloadData = view.findViewById(R.id.tv_download_data);
        ivDownloadData = view.findViewById(R.id.iv_download_data);
        btnDownload = view.findViewById(R.id.btn_download);
        btnDownload.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_download:
                break;
        }
    }
}

