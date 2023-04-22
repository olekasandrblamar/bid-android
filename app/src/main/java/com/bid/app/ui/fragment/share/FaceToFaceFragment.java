package com.bid.app.ui.fragment.share;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bid.app.R;
import com.bid.app.interfaces.IBundle;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class FaceToFaceFragment extends BaseFragment {

    private ImageView ivQRfaceToFace;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_face_to_face, container, false);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_face_to_face_share), R.drawable.ic_back_arrow, false);

        initController(view);

        return view;
    }

    private String getPersonalDetails() {
        final Bundle bundle = getArguments();
        return bundle != null ? bundle.getString(IBundle.BUNDLE_SELECTED_PERSONAL_DETAILS) : "";
    }

    private void initController(View view) {

        ivQRfaceToFace = view.findViewById(R.id.iv_qr_face_to_face);

        ivQRfaceToFace.setOnClickListener(this);



        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            final BitMatrix bitMatrix = qrCodeWriter.encode(getPersonalDetails(), BarcodeFormat.QR_CODE, 200, 200);
            int height = bitMatrix.getHeight();
            int width = bitMatrix.getWidth();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++){
                for (int y = 0; y < height; y++){
                    bmp.setPixel(x, y, bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
                }
            }
            ivQRfaceToFace.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }
}







