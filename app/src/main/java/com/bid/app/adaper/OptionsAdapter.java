package com.bid.app.adaper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;

import java.util.ArrayList;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {

    private static final String TAG = OptionsAdapter.class.getSimpleName();
    private IReasonClickListenerListener iRecyclerViewClickListener;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private ArrayList<String> reasons;

    public interface IReasonClickListenerListener {
        void clickOnReasonForTest(int position);
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public OptionsAdapter(ArrayList<String> reasons, IReasonClickListenerListener listener) {
        this.iRecyclerViewClickListener = listener;
        this.reasons = reasons;
    }

    @NonNull
    @Override
    public OptionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.options_item, parent, false);
        return new OptionsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OptionsAdapter.ViewHolder holder, final int position) {

        holder.reason_name.setText(reasons.get(position));

        if (selectedPosition == position) {
            holder.reason_radio_btn.setChecked(true);
        } else {
            holder.reason_radio_btn.setChecked(false);
        }

        holder.constraint_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                iRecyclerViewClickListener.clickOnReasonForTest(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reasons.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView reason_name;
        private AppCompatRadioButton reason_radio_btn;
        private ConstraintLayout constraint_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            reason_name = itemView.findViewById(R.id.reason_name);
            reason_radio_btn = itemView.findViewById(R.id.reason_radio_btn);
            constraint_layout = itemView.findViewById(R.id.constraint_layout);
        }
    }

}