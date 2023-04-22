package com.bid.app.adaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.response.SlotDetails;
import com.bid.app.util.Utils;

import java.util.ArrayList;
import java.util.Objects;

public class ScheduleSlotsAdapter extends RecyclerView.Adapter<ScheduleSlotsAdapter.ViewHolder> {

    private static final String TAG = SettingsAdapter.class.getSimpleName();
    private final Context mContext;
    private final IRecyclerViewClickListener iRecyclerViewClickListener;
    private final ArrayList<SlotDetails> timeslotsList;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public ScheduleSlotsAdapter(Context context, ArrayList<SlotDetails> timeslotsList, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.timeslotsList = timeslotsList;
        this.iRecyclerViewClickListener = listener;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    @NonNull
    @Override
    public ScheduleSlotsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_schedule_time_list, parent, false);
        return new ScheduleSlotsAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ScheduleSlotsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        SlotDetails slotDetails = timeslotsList.get(position);

        if (!slotDetails.getTime().isEmpty()) {
            holder.tv_time_schedule_list.setText(Utils.convertTimeTo12HourFormat(slotDetails.getTime()).toUpperCase());
        }

//        if (Objects.requireNonNull(slotDetails).getFree() != null && !slotDetails.getFree().isEmpty()) {
//            holder.tv_time_free.setText(slotDetails.getFree() + " Free");
//        }
        if (Objects.requireNonNull(slotDetails).getPaid() != null && !slotDetails.getPaid().isEmpty()) {
            holder.tv_time_paid.setText(slotDetails.getFree() + " Paid");
        }

        if (selectedPosition == position) {
            holder.constraint_layout_time_slot_list.setBackgroundResource(R.drawable.shape_selected_slot);
            holder.tv_time_schedule_list.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
            holder.tv_time_schedule_text.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
        } else {
            holder.tv_time_schedule_list.setTextColor(mContext.getResources().getColor(R.color.colorTextGray));
            holder.tv_time_schedule_text.setTextColor(mContext.getResources().getColor(R.color.colorTextGray));
            holder.constraint_layout_time_slot_list.setBackgroundResource(R.drawable.shape_unselected_slot);
        }

        if (slotDetails.getIs_expired()) {
            holder.constraint_layout.setAlpha(0.3f);
        } else {
            holder.constraint_layout.setAlpha(1f);
        }

        holder.constraint_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!slotDetails.getIs_expired()) {
                    iRecyclerViewClickListener.clickOnSlot(position);
                } else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.can_not_book_from_past), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeslotsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_time_schedule_list;
        private final TextView tv_time_paid;
        private final TextView tv_time_schedule_text;
        private final ConstraintLayout constraint_layout_time_slot_list;
        private final ConstraintLayout constraint_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_time_schedule_list = itemView.findViewById(R.id.tv_time_schedule_list);
//            tv_time_free = itemView.findViewById(R.id.tv_time_free);
            tv_time_paid = itemView.findViewById(R.id.tv_time_paid);
            constraint_layout_time_slot_list = itemView.findViewById(R.id.constraint_layout_time_slot_list);
            constraint_layout = itemView.findViewById(R.id.constraint_layout);
            tv_time_schedule_text = itemView.findViewById(R.id.tv_time_schedule_text);
        }
    }

}