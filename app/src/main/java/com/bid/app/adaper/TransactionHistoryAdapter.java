package com.bid.app.adaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.response.TransactionsHistory;
import com.bid.app.model.view.About;

import java.text.DecimalFormat;
import java.util.List;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder> {

    private static final String TAG = TransactionHistoryAdapter.class.getSimpleName();
    private final List<TransactionsHistory> transactionHistoryList;

    private Context mContext;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public TransactionHistoryAdapter(Context context, List<TransactionsHistory> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.transactionHistoryList = list;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public TransactionHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transaction_history_list, parent, false);
        return new TransactionHistoryAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final TransactionHistoryAdapter.ViewHolder holder, final int position) {

        TransactionsHistory transactionHistory = transactionHistoryList.get(position);
        String sign = "-";
        if(transactionHistory.getTransaction_type() == null) {

        }
        else if(transactionHistory.getTransaction_type().equals("0")) { // top up
            holder.cardAvatar.setImageResource(R.drawable.top_up_transaction);
            holder.tvHospital.setText("Top up");
            holder.tvTest.setText("visa/Master Card");
            sign = "+";
        }
        else if(transactionHistory.getTransaction_type().equals("1")) { //health
            holder.cardAvatar.setImageResource(R.drawable.health_booking_transaction);
            holder.tvHospital.setText("Health booking");
            holder.tvTest.setText("visa/Master Card");
        }
        else if(transactionHistory.getTransaction_type().equals("2")) { // transit;
            holder.cardAvatar.setImageResource(R.drawable.bus_blue);
            holder.tvHospital.setText("Bus booking");
            holder.tvTest.setText("visa/Master Card");
        } else if(transactionHistory.getTransaction_type().equals("3")) { // penalty;
            holder.cardAvatar.setImageResource(R.drawable.bus_blue);
            holder.tvHospital.setText("Booking Penalty");
            holder.tvTest.setText("visa/Master Card");
        }
        holder.tvDate.setText(transactionHistory.getCreated_at());
        DecimalFormat moneyFormat = new DecimalFormat("$#,##0.00");
        holder.tvAmount.setText(sign + moneyFormat.format(new Float(transactionHistory.getAmount())));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iRecyclerViewClickListener.clickTransactionHistory(position, transactionHistory);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDate;
        private TextView tvHospital;
        private TextView tvTest;
        private TextView tvAmount;
        private ImageView cardAvatar;

        private LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tv_date_transaction_history_adapter_list);
            tvHospital = itemView.findViewById(R.id.tv_hospital_transaction_history_adapter_list);
            tvTest = itemView.findViewById(R.id.tv_test_transaction_history_adapter_list);
            tvAmount = itemView.findViewById(R.id.tv_amount_transaction_history_adapter_list);
            cardAvatar = itemView.findViewById(R.id.card_avatar);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}