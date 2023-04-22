package com.bid.app.ui.fragment.home;


import android.graphics.Bitmap;
import android.graphics.Color;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bid.app.R;
import com.bid.app.model.view.RoamFree;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.base.BaseFragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class RoamFreeFragment extends BaseFragment {

    private SessionManager sessionManager;
    private RoamFree roamFree;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roam_free, container, false);

        sessionManager = new SessionManager(requireActivity());
        roamFree= (RoamFree) getArguments().getSerializable("Roam Free");

        initViews(view);
        return view;
    }

    private void initViews(View view) {
        TextView tvName=view.findViewById(R.id.tv_name_value);
        TextView tvDob=view.findViewById(R.id.tv_dob_value);
        TextView tvPassport=view.findViewById(R.id.tv_passport_value);
        TextView tvRoamFreeStart=view.findViewById(R.id.tv_roam_free_start_value);
        TextView tvRoamFreeEnd=view.findViewById(R.id.tv_roam_free_end_value);
        TextView tvStatus=view.findViewById(R.id.tv_status_view_value);
        TextView tvTestType=view.findViewById(R.id.tv_test_type_value);
        TextView tvIssuer=view.findViewById(R.id.tv_issuer_value);
        TextView tvCountry=view.findViewById(R.id.tv_county_value);

        tvName.setText(roamFree.getUsername());
        tvDob.setText(roamFree.getDob());
        tvPassport.setText(roamFree.getPassportNumber());
        tvRoamFreeStart.setText(roamFree.getRoamStart());
        tvRoamFreeEnd.setText(roamFree.getRoamEnd());
        tvStatus.setText(roamFree.getStatus());
        tvTestType.setText(roamFree.getTestType());
        tvIssuer.setText(roamFree.getCenter());
      //  tvCountry.setText(roamFree.getCountry());

        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(roamFree.getQr_code(), BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            ((ImageView) view.findViewById(R.id.imageView)).setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

}
