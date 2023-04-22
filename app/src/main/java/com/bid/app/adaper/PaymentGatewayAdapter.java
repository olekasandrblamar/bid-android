package com.bid.app.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.response.CardListInfo;
import com.makeramen.roundedimageview.RoundedImageView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;


public class PaymentGatewayAdapter extends RecyclerView.Adapter<PaymentGatewayAdapter.ViewHolder> {

    private static final String TAG = PaymentGatewayAdapter.class.getSimpleName();

    private Context mContext;
    private List<CardListInfo> paymentGatewayList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public PaymentGatewayAdapter(Context context, List<CardListInfo> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.paymentGatewayList = list;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_payment_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final CardListInfo paymentGateway = paymentGatewayList.get(position);
        //final Attachment attachment = paymentGateway.getAttachment();

        if (!StringUtils.isEmpty(paymentGateway.getCard_display_number())) {
            holder.tvName.setText(paymentGateway.getCard_display_number());
        } else {
            holder.tvName.setText(paymentGateway.getName());
        }

        if ("Add Card".equalsIgnoreCase(paymentGateway.getName())) {
            holder.ivCreditCard.setVisibility(View.VISIBLE);
        } else {
            holder.ivCreditCard.setVisibility(View.GONE);
        }

      /*  String imageUrl = "";
        if (attachment != null) {
            final String hashKey = Utils.getMD5Hash(attachment);
            imageUrl = Constans.API_IMAGE_URL(attachment, hashKey, "jpg");
        } else {
            imageUrl = Constans.NO_IMAGE_URL;
        }
        Log.e(TAG, "imageURL " + imageUrl);*/


       /* if ("Add Card".equalsIgnoreCase(paymentGateway.getName())) {
            Logger.e(TAG, paymentGateway.getName());
            holder.ivPayment.setImageResource(R.drawable.ic_add_card_grey);
        } else {
            Glide.with(mContext)
                    .load(imageUrl)
                    .into(holder.ivPayment);
        }*/

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickOnAddCard(paymentGateway);
            }
        });
    }

    @Override
    public int getItemCount() {
        return paymentGatewayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView ivPayment;
        private ImageView ivCreditCard;
        private TextView tvName;

        private LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPayment = itemView.findViewById(R.id.iv_payment_adapter_list);
            ivCreditCard = itemView.findViewById(R.id.iv_credit_card_payment_adapter_list);
            tvName = itemView.findViewById(R.id.tv_name_payment_adapter_list);

            linearLayout = itemView.findViewById(R.id.linear_layout);
        }
    }
}
