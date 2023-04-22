package com.bid.app.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.view.Symptoms;

import java.util.List;

public class SymptomsAdapter extends RecyclerView.Adapter<SymptomsAdapter.ViewHolder> {

    private static final String TAG = SymptomsAdapter.class.getSimpleName();

    private Context mContext;
    private List<Symptoms> symptomsList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public SymptomsAdapter(Context context, List<Symptoms> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.symptomsList = list;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public SymptomsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_symptoms_list, parent, false);
        return new SymptomsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SymptomsAdapter.ViewHolder holder, final int position) {

        final Symptoms symptoms = symptomsList.get(position);

        holder.tvName.setText(symptoms.getName());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickOnSymptoms(position, symptoms);
            }
        });

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBox.setChecked(!holder.checkBox.isChecked());
                iRecyclerViewClickListener.clickOnSymptoms(position, symptoms);
            }
        });
    }

    @Override
    public int getItemCount() {
        return symptomsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;

        private CheckBox checkBox;

        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_test_name_adapter_list);

            checkBox = itemView.findViewById(R.id.cb_test_adapter_list);

            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }

}