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
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CardsHorizontalAdapter extends RecyclerView.Adapter<CardsHorizontalAdapter.ViewHolder> {

    private static final String TAG = CardsHorizontalAdapter.class.getSimpleName();

    private Context mContext;
    private List<CardListInfo> cardListInfos;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public CardsHorizontalAdapter(Context context, List<CardListInfo> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.cardListInfos = list;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public CardsHorizontalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_card_horizontal_list, parent, false);
        return new CardsHorizontalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardsHorizontalAdapter.ViewHolder holder, final int position) {

        final CardListInfo cardListInfo = cardListInfos.get(position);

        holder.cardName.setText(cardListInfo.getName());

        holder.cardNumber.setText(cardListInfo.getCard_display_number());

        if(cardListInfo.getCard_type().equals("Visa")) {
            holder.cardAvatar.setImageResource(R.drawable.visa);

        }
        else if(cardListInfo.getCard_type().equals("MasterCard")) {
//            cardTypeImg.setImageResource(R.drawable.visa);
        }
        else if(cardListInfo.getCard_type().equals("Discover")) {
//            cardTypeImg.setImageResource(R.drawable.visa);
        }
        holder.cardBoard.setOnClickListener(new View.OnClickListener() {
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

        private TextView amount;
        private TextView cardNumber;
        private TextView cardName;
        private ImageView cardAvatar;
        private MaterialCardView cardBoard;
        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardAvatar = itemView.findViewById(R.id.card_avatar);
            cardName = itemView.findViewById(R.id.card_name);
            amount = itemView.findViewById(R.id.amount);
            cardNumber = itemView.findViewById(R.id.card_number);
            cardBoard = itemView.findViewById(R.id.card_board);
            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }
}
