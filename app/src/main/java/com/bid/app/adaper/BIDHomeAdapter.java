package com.bid.app.adaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.view.BIDHome;
import com.bid.app.util.Constants;

import java.util.List;

public class BIDHomeAdapter extends RecyclerView.Adapter<BIDHomeAdapter.ViewHolder> {

    private static final String TAG = BIDHomeAdapter.class.getSimpleName();
    private final String strStatus;

    private Context mContext;
    private List<BIDHome> bidHomeList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public BIDHomeAdapter(Context context, List<BIDHome> list, IRecyclerViewClickListener listener, String strStatus) {
        this.mContext = context;
        this.bidHomeList = list;
        this.iRecyclerViewClickListener = listener;
        this.strStatus = strStatus;
    }

    @NonNull
    @Override
    public BIDHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_menu_bid_list, parent, false);
        return new BIDHomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BIDHomeAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final BIDHome bidHome = bidHomeList.get(position);

        holder.tvName.setText(bidHome.getName());
        holder.ivMenu.setImageResource(bidHome.getResource());
        if (bidHome.getName().equals("Roam Free")) {
            if (strStatus.equals("true")) {
                holder.ivMenu.setColorFilter(null);
            } else {
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                holder.ivMenu.setColorFilter(filter);
            }

            /*if (Constants.strStatusRoamFree.equals("true")){
                holder.cardView.setVisibility(View.VISIBLE);
            }else {
                holder.cardView.setVisibility(View.INVISIBLE);
            }*/
        }


        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickOnBIDHome(position, bidHome);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bidHomeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private ImageView ivMenu;
        private CardView cardView;

        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name_menu_bid_adapter);
            ivMenu = itemView.findViewById(R.id.iv_menu_bid_adapter);
            cardView = itemView.findViewById(R.id.cardView);
            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }

}
