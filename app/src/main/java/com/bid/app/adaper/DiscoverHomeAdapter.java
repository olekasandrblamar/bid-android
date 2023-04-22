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
import com.bid.app.constants.Global;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.view.Restaurant;
import com.bumptech.glide.Glide;

import java.util.List;

public class DiscoverHomeAdapter extends RecyclerView.Adapter<DiscoverHomeAdapter.ViewHolder> {

    private static final String TAG = DiscoverHomeAdapter.class.getSimpleName();

    private Context mContext;
    private List<Restaurant> bidHomeList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public DiscoverHomeAdapter(Context context, List<Restaurant> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.bidHomeList = list;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public DiscoverHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_discover_list, parent, false);
        return new DiscoverHomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DiscoverHomeAdapter.ViewHolder holder, final int position) {

        final Restaurant bidHome = bidHomeList.get(position);

        holder.tvName.setText(bidHome.getName());
        //holder.ivMenu.setImageResource(bidHome.getResource());

        final String photoReference = bidHome.getPhotos() != null && !bidHome.getPhotos().isEmpty() ? bidHome.getPhotos().get(0).getPhoto_reference() : "http://";

        Glide.with(mContext)
                .load(Global.getRestaurantImage(photoReference))
                .placeholder(R.drawable.discover)
                .error(R.drawable.discover)
                .into(holder.ivMenu);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickRestaurant(position, bidHome);
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

        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_desc_discover_adapter_list);
            ivMenu = itemView.findViewById(R.id.iv_discover_adapter_list);

            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }

}
