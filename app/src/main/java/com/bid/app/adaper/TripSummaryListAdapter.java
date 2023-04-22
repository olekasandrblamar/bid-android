package com.bid.app.adaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.view.Ticket;
import com.bid.app.ui.fragment.discover.transit.InstructionFragment;

import java.util.ArrayList;

public class TripSummaryListAdapter extends RecyclerView.Adapter<TripSummaryListAdapter.ViewHolder> {

    private static final String TAG = SettingsAdapter.class.getSimpleName();

    private static Context mContext;
    private ArrayList<Ticket> scheduleList;

    public TripSummaryListAdapter(Context context, ArrayList<Ticket> list) {
        this.mContext = context;
        this.scheduleList = new ArrayList<Ticket>();
        int i;
        for(i=0;i<list.size();i++){
            this.scheduleList.add(list.get(i));
        }
    }
    public void setList(ArrayList<Ticket> list) {
        this.scheduleList = new ArrayList<Ticket>();
        int i;
        for(i=0;i<list.size();i++){
            this.scheduleList.add(list.get(i));
        }
    }

    @NonNull
    @Override
    public TripSummaryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_trip_summary_item, parent, false);
        return new TripSummaryListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TripSummaryListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Ticket p = this.scheduleList.get(position);
        if(p.getMode().equals("walking")) {

        }
        else if(p.getMode().equals("driving")){
            holder.busNumber.setText("Bus" + (new Integer(position + 1)).toString() + ":" + p.getBusNo());
            holder.fromTo.setText("Route" + (new Integer(position + 1)).toString() + ":" + p.getStartStop() + "-------->" + p.getEndStop());
        }

    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView busNumber;
        private TextView fromTo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            busNumber = itemView.findViewById(R.id.bus_number);
            fromTo = itemView.findViewById(R.id.from_to);
        }
    }

}