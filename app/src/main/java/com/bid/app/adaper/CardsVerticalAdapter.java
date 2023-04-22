package com.bid.app.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.response.CardListInfo;
import com.bid.app.model.view.PersonalDetail;
import com.bid.app.util.Logger;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class CardsVerticalAdapter extends RecyclerView.Adapter<CardsVerticalAdapter.ViewHolder> {

    private static final String TAG = CardsVerticalAdapter.class.getSimpleName();

    private Context mContext;
    private List<CardListInfo> cardListInfos;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public CardsVerticalAdapter(Context context, List<CardListInfo> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.cardListInfos = list;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public CardsVerticalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_card_vertical_list, parent, false);
        return new CardsVerticalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardsVerticalAdapter.ViewHolder holder, final int position) {

        final CardListInfo cardListInfo = cardListInfos.get(position);

        holder.cardName.setText(cardListInfo.getName());

        holder.cardNumber.setText(cardListInfo.getCard_display_number());

        holder.cardAvatar.setImageResource(R.drawable.debit_credit);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickOnCardListInfo(position, cardListInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardListInfos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView cardName;
        private TextView cardNumber;
        private ImageView cardAvatar;

        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardAvatar = itemView.findViewById(R.id.card_avatar);
            cardName = itemView.findViewById(R.id.card_name);
            cardNumber = itemView.findViewById(R.id.card_number);

            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }
}
