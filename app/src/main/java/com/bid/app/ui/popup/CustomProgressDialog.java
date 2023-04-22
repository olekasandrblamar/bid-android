package com.bid.app.ui.popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import com.bid.app.R;
import com.bid.app.util.Logger;

public class CustomProgressDialog {

    public static CustomProgressDialog customProgress = null;
    private Dialog mDialog;

    public static CustomProgressDialog getInstance() {
        if (customProgress == null) {
            customProgress = new CustomProgressDialog();
        }
        return customProgress;
    }

    public void showDialog(Context context, boolean cancelable) {
        try {
            mDialog = new Dialog(context);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setContentView(R.layout.custom_progress_dialog);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            ProgressBar mProgressBar = mDialog.findViewById(R.id.progress_bar);
            //  mProgressBar.getIndeterminateDrawable().setColorFilter(context.getResources()
            // .getColor(R.color.material_blue_gray_500), PorterDuff.Mode.SRC_IN);

            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setIndeterminate(true);
            mDialog.setCancelable(cancelable);
            mDialog.setCanceledOnTouchOutside(cancelable);
            if (mDialog != null && !mDialog.isShowing()) {
                mDialog.show();
            }
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
    }

    public void dismissDialog() {
        try {
            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
    }
}
