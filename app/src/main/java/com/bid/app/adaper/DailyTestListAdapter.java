package com.bid.app.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.view.Symptoms;

import java.util.List;

public class DailyTestListAdapter extends RecyclerView.Adapter<DailyTestListAdapter.ViewHolder> {

    private static final String TAG = DailyTestListAdapter.class.getSimpleName();

    private Context mContext;
    private List<Symptoms> symptomsList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public DailyTestListAdapter(Context context, List<Symptoms> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.symptomsList = list;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public DailyTestListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_test_history_list, parent, false);
        return new DailyTestListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DailyTestListAdapter.ViewHolder holder, final int position) {

        final Symptoms symptoms = symptomsList.get(position);

        holder.tvName.setText(symptoms.getCreated_at());
        holder.tvSymptoms.setText(symptoms.getName());

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickOnDailyTest(position,symptoms);
            }
        });
    }

    @Override
    public int getItemCount() {
        return symptomsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvSymptoms;

        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_desc_history_list);
            tvSymptoms = itemView.findViewById(R.id.tv_status_history_list);

            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }

}