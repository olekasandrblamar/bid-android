package com.bid.app.adaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.view.PersonalDetail;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class SelectSendDetailsAdapter extends RecyclerView.Adapter<SelectSendDetailsAdapter.ViewHolder> {

    private static final String TAG = SelectSendDetailsAdapter.class.getSimpleName();

    private Context mContext;
    private List<PersonalDetail> shareDetailsList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public SelectSendDetailsAdapter(Context context, List<PersonalDetail> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.shareDetailsList = list;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public SelectSendDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_share_details_list, parent, false);
        return new SelectSendDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectSendDetailsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final PersonalDetail personalDetail = shareDetailsList.get(position);

        holder.tvName.setText(personalDetail.getName());

        if (!StringUtils.isEmpty(personalDetail.getDesc())) {
            holder.tvDesc.setText(personalDetail.getDesc());
            holder.tvDesc.setVisibility(View.VISIBLE);
        } else {
            holder.tvDesc.setVisibility(View.GONE);
        }

        holder.ivLeft.setImageResource(personalDetail.getResourceOne());

        if (personalDetail.isSelected()) {
            holder.appCompatCheckBox.setChecked(true);
        } else {
            holder.appCompatCheckBox.setChecked(false);
        }

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personalDetail.setSelected(true);
                iRecyclerViewClickListener.clickOnPersonalDetail(position, personalDetail);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return shareDetailsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvDesc;
        private ImageView ivLeft;
        private AppCompatCheckBox appCompatCheckBox;

        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name_share_details_list_adapter);
            tvDesc = itemView.findViewById(R.id.tv_desc_share_details_list_adapter);

            ivLeft = itemView.findViewById(R.id.iv_share_details_list_adapter);

            appCompatCheckBox = itemView.findViewById(R.id.rbtn_share_list_adapter);

            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }
}
