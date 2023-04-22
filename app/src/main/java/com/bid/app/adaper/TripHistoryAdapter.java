package com.bid.app.adaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.view.Trip;

import java.util.ArrayList;
import java.util.List;


public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryAdapter.ViewHolder> {

    public static int TRIP = 0;
    public static int REPEAT_TRIP = 2;


    private static final String TAG = TripHistoryAdapter.class.getSimpleName();

    private Context mContext;
    private List<Trip> ticketsDataList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;


    public TripHistoryAdapter(Context context, ArrayList<Trip> trips, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.ticketsDataList = trips;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public TripHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_trips, parent, false);
        return new TripHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final Trip tripData = ticketsDataList.get(position);


        holder.fromStation.setText(tripData.getFrom());
        holder.fromStation.setSelected(true);
        holder.toStation.setText(tripData.getTo());
        holder.toStation.setSelected(true);

        if(tripData.getType().equals("upcoming"))
        {
            holder.repeatTrip.setVisibility(View.GONE);
        }
        else if(tripData.getType().equals("history"))
        {
            holder.repeatTrip.setVisibility(View.VISIBLE);
        }


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickTrip(position, tripData, TRIP);
            }
        });

        holder.repeatTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iRecyclerViewClickListener.clickTrip(position,tripData, REPEAT_TRIP);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ticketsDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        private LinearLayout layout, repeatTrip;
        TextView fromStation, toStation;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout);
            repeatTrip = itemView.findViewById(R.id.repeat_trip);
            fromStation = itemView.findViewById(R.id.from_station);
            toStation = itemView.findViewById(R.id.to_station);

        }
    }
}



