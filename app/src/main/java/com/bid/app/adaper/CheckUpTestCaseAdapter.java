package com.bid.app.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.view.CheckUpTestCase;

import java.util.List;

public class CheckUpTestCaseAdapter extends RecyclerView.Adapter<CheckUpTestCaseAdapter.ViewHolder> {

    private static final String TAG = CheckUpTestCaseAdapter.class.getSimpleName();

    private Context mContext;
    private List<CheckUpTestCase> checkUpTestCases;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public CheckUpTestCaseAdapter(Context context, List<CheckUpTestCase> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.checkUpTestCases = list;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public CheckUpTestCaseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_test_next_steps, parent, false);
        return new CheckUpTestCaseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CheckUpTestCaseAdapter.ViewHolder holder, final int position) {

        final CheckUpTestCase testCase = checkUpTestCases.get(position);

        holder.tvSerialNumber.setText(String.valueOf(testCase.getNumber()));
        holder.tvName.setText(testCase.getName());
        holder.tvDesc.setText(testCase.getDesc());

    }

    @Override
    public int getItemCount() {
        return checkUpTestCases.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView tvSerialNumber;
        private AppCompatTextView tvName;
        private AppCompatTextView tvDesc;

        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSerialNumber = itemView.findViewById(R.id.tv_serial_number_next_step_adapter);
            tvName = itemView.findViewById(R.id.tv_follow_doctor_orders);
            tvDesc = itemView.findViewById(R.id.tv_follow_doctor_orders_desc);

            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }


}
