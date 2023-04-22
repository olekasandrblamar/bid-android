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
import com.bid.app.ui.fragment.discover.transit.ScheduleFragment;

import java.util.ArrayList;
import java.util.Stack;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.ViewHolder> {

    public static int BUY_TICKET = 0;
    public static int QRCODE = 1;
    public static int SOS = 2;
    public static int TICKET = 3;

    private static final String TAG = SettingsAdapter.class.getSimpleName();

    private static Context mContext;
    private ArrayList<Ticket> scheduleList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public TripListAdapter(Context context, ArrayList<Ticket> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.scheduleList = new ArrayList<Ticket>();
        int i;
        for(i=0;i<list.size();i++){
            this.scheduleList.add(list.get(i));
        }
        this.iRecyclerViewClickListener = listener;
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
    public TripListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transport_schedule, parent, false);
        return new TripListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TripListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Ticket p = this.scheduleList.get(position);
        Double prevDistance = 0.0;
        for(int i = 0; i < position; i++) prevDistance += Math.ceil(new Double(this.scheduleList.get(i).getDistance()) / 1000);
        if(this.scheduleList.size() - 1 == position) {
            holder.stationMark.setImageResource(R.drawable.destination_position);
        }
        if(this.scheduleList.size()> position + 1 && this.scheduleList.get(position + 1).getMode().equals("driving")) {
            holder.eta.setText("will arrive in "  + this.scheduleList.get(position + 1).getEta() + "s");
            holder.eta.setVisibility(View.VISIBLE);
        }
        else {
            holder.eta.setVisibility(View.GONE);
        }
        if(p.getMode().equals("walking")) {
            holder.typeImg.setImageResource(R.drawable.walking);
            holder.toStation.setText( p.getEndStop());
            holder.toStation.setSelected(true);
            holder.typeLyt.setVisibility(View.INVISIBLE);
            holder.busInfo.setText("");
            holder.selectedSeat.setText("");;
            holder.scheduleTime.setText(Math.ceil(new Double(p.getDistance()) / 1000) + " mins");
            holder.arrivalTime.setText("will takes " +(Math.ceil(new Double(p.getDistance()) / 1000)) + " mins" );
        }
        else if(p.getMode().equals("driving")){
            holder.typeImg.setImageResource(R.drawable.bus_blue);
            holder.toStation.setText( p.getEndStop());
            holder.toStation.setSelected(true);
            holder.typeLyt.setVisibility(View.VISIBLE);
            holder.busInfo.setText("No: " + p.getBusNo());
            holder.scheduleTime.setText(Math.ceil(new Double(p.getDistance()) / 1000) + " mins");
            holder.arrivalTime.setText("will takes " +(Math.ceil(new Double(p.getDistance()) / 1000)) + " mins" );
            holder.eta.setText("in "  + p.getEta() + "s");
            if(p.getSeatNumber() == null) {
                holder.selectedSeat.setText("Seat: ");
            }else {
                holder.selectedSeat.setText("Seat:" + ((Integer)(new Integer(p.getSeatNumber()) + 1)));
            }
            if(p.getStatus() ==null  || p.getStatus().equals(InstructionFragment.TICKET_CANCELED) || p.getStatus().equals(InstructionFragment.TICKET_UNAVILABLE)) {
                holder.sel_btn.setVisibility(View.VISIBLE);
                holder.ticketDetail.setVisibility(View.GONE);
                holder.qrCode.setVisibility(View.GONE);
            }
            else  if(p.getStatus().equals(InstructionFragment.TICKET_UNUSED)){
                holder.sel_btn.setVisibility(View.GONE);
                holder.ticketDetail.setVisibility(View.VISIBLE);
                holder.qrCode.setVisibility(View.VISIBLE);
            }
            else if(p.getStatus().equals(InstructionFragment.TICKET_USED)){
                holder.sel_btn.setVisibility(View.GONE);
                holder.ticketDetail.setVisibility(View.VISIBLE);
                holder.qrCode.setVisibility(View.GONE);
            } else {
                holder.sel_btn.setVisibility(View.VISIBLE);
                holder.ticketDetail.setVisibility(View.GONE);
                holder.qrCode.setVisibility(View.GONE);
            }


            holder.sel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iRecyclerViewClickListener.clickTickets(position, scheduleList.get(position), BUY_TICKET);
                }
            });

            holder.ticketDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iRecyclerViewClickListener.clickTickets(position, scheduleList.get(position), TICKET);
                }
            });
            holder.qrCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iRecyclerViewClickListener.clickTickets(position, scheduleList.get(position), QRCODE);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView typeImg;
        private TextView toStation;
        private TextView busInfo;
        private TextView scheduleTime;
        private TextView eta;
        private TextView selectedSeat;
        private TextView arrivalTime;
        private ImageView stationMark;
        private Button sel_btn;
        private LinearLayout ticketDetail, qrCode;
        private LinearLayout typeLyt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            scheduleTime =itemView.findViewById(R.id.schedule_time);
            typeLyt = itemView.findViewById(R.id.type_lyt);
            typeImg = itemView.findViewById(R.id.type_img);
            toStation = itemView.findViewById(R.id.to_station);
            eta = itemView.findViewById(R.id.eta);
            busInfo = itemView.findViewById(R.id.bus_info);
            sel_btn = itemView.findViewById(R.id.buy_ticket);
            ticketDetail = itemView.findViewById(R.id.ticket_detail);
            qrCode = itemView.findViewById(R.id.qr_code);
            selectedSeat = itemView.findViewById(R.id.selected_seat);
            arrivalTime = itemView.findViewById(R.id.arrival_time);
            stationMark = itemView.findViewById(R.id.station_mark);
        }
    }

}