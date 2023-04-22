package com.bid.app.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.view.PersonalDetail;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class SendPreviewAdapter extends RecyclerView.Adapter<SendPreviewAdapter.ViewHolder> {

    private static final String TAG = SendPreviewAdapter.class.getSimpleName();

    private Context mContext;
    private List<PersonalDetail> sendPreviewList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public SendPreviewAdapter(Context context, List<PersonalDetail> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.sendPreviewList = list;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public SendPreviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_preview_details_list, parent, false);
        return new SendPreviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SendPreviewAdapter.ViewHolder holder, final int position) {

        final PersonalDetail personalDetail = sendPreviewList.get(position);

        holder.tvMobile.setText(personalDetail.getName());

        if (!StringUtils.isEmpty(personalDetail.getDesc())) {
            holder.tvMobileNumber.setText(personalDetail.getDesc());
            holder.tvMobileNumber.setVisibility(View.VISIBLE);
        } else {
            holder.tvMobileNumber.setVisibility(View.GONE);
        }

        holder.ivLeft.setImageResource(personalDetail.getResourceOne());

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickOnPersonalDetail(position, personalDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sendPreviewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMobile;
        private TextView tvMobileNumber;
        private ImageView ivLeft;
        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMobile = itemView.findViewById(R.id.tv_mobile_preview_details_list_adapter);
            tvMobileNumber = itemView.findViewById(R.id.tv_mobile_number_preview_details_list_adapter);

            ivLeft = itemView.findViewById(R.id.iv_preview_details_list_adapter);


            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }
}
