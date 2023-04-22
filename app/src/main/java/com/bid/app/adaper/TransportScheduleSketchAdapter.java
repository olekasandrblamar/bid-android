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
import com.bid.app.ui.fragment.discover.transit.ScheduleFragment;

import java.util.ArrayList;

public class TransportScheduleSketchAdapter extends RecyclerView.Adapter<TransportScheduleSketchAdapter.ViewHolder> {

    private static final String TAG = SettingsAdapter.class.getSimpleName();

    private static Context mContext;
    private ArrayList<ScheduleFragment.RouteForTicket> scheduleList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public TransportScheduleSketchAdapter(Context context, ArrayList<ScheduleFragment.RouteForTicket> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.scheduleList = new ArrayList<ScheduleFragment.RouteForTicket>();
        int i;
        for(i=0;i<list.size();i++){
            this.scheduleList.add(list.get(i));
        }
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public TransportScheduleSketchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transport_schedule_sketch, parent, false);
        return new TransportScheduleSketchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TransportScheduleSketchAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ScheduleFragment.RouteForTicket p = this.scheduleList.get(position);
        if(position>0) holder.arrowImg.setVisibility(View.VISIBLE);
        else holder.arrowImg.setVisibility(View.GONE);
        if(p.type == ScheduleFragment.RouteForTicket.WALKING) {
            holder.typeImg.setImageResource(R.drawable.walking);
            holder.scheduleTime.setText(Math.ceil(p.distance / 1000) + " min");
        }
        else{
            holder.typeImg.setImageResource(R.drawable.bus_blue);
            holder.scheduleTime.setText(Math.ceil(p.distance / 1000) + " min");
        }

    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView typeImg;
        private ImageView arrowImg;
        private TextView scheduleTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            scheduleTime =itemView.findViewById(R.id.schedule_time);
            typeImg = itemView.findViewById(R.id.type_img);
            arrowImg = itemView.findViewById(R.id.arrow_img);
        }
    }

}