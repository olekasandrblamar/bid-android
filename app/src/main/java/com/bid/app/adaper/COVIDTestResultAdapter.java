package com.bid.app.adaper;

import android.content.Context;
import android.graphics.Color;
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
import com.bid.app.model.view.CovidResult;
import com.bid.app.util.Utils;

import java.util.List;

public class COVIDTestResultAdapter extends RecyclerView.Adapter<COVIDTestResultAdapter.ViewHolder> {

    private static final String TAG = SettingsAdapter.class.getSimpleName();

    private Context mContext;
    private List<CovidResult> covidResultList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public COVIDTestResultAdapter(Context context, List<CovidResult> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.covidResultList = list;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public COVIDTestResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_test_result_list, parent, false);
        return new COVIDTestResultAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final COVIDTestResultAdapter.ViewHolder holder, final int position) {

        final CovidResult result = covidResultList.get(position);

        holder.tvDate.setText("Date : " + result.getReg_date());
        holder.tvTime.setText("Time : " + Utils.getTime(result.getUpdated_at()));
        holder.tvLocation.setText("Testing Location : " + result.getCenter().getName());

        holder.tvStatus.setText(result.getTest_status().getName());
        int color = Color.parseColor(result.getTest_status().getColor());
        holder.constraintLayoutResult.setBackgroundColor(color);

        if ("2".equalsIgnoreCase(result.getStatus())) {
            holder.ivStatus.setImageResource(R.drawable.ic_negative);
        } else {
            holder.ivStatus.setImageResource(R.drawable.ic_positive);
        }


        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickOnResult(position, result);
            }
        });
    }

    @Override
    public int getItemCount() {
        return covidResultList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDate;
        private TextView tvTime;
        private TextView tvLocation;
        private TextView tvStatus;

        private ImageView ivStatus;

        private ConstraintLayout constraintLayoutResult;

        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tv_date_test_result_adapter_list);
            tvTime = itemView.findViewById(R.id.tv_time_test_result_adapter_list);
            tvLocation = itemView.findViewById(R.id.tv_location_test_result_adapter_list);
            tvStatus = itemView.findViewById(R.id.tv_status_test_result_adapter_list);

            ivStatus = itemView.findViewById(R.id.iv_status_test_result_adapter_list);

            constraintLayoutResult = itemView.findViewById(R.id.constraint_layout_result_adapter);
            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }

}