package com.bid.app.adaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.view.GetReviewData;
import com.bid.app.ui.fragment.discover.transit.ScheduleFragment;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class ReviewListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private static final String TAG = SettingsAdapter.class.getSimpleName();

    private static Context mContext;
    private ArrayList<GetReviewData> scheduleList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public ReviewListAdapter(Context context, ArrayList<GetReviewData> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.scheduleList = new ArrayList<GetReviewData>();
        int i;
        for(i=0;i<list.size();i++){
            this.scheduleList.add(list.get(i));
        }
        this.iRecyclerViewClickListener = listener;
    }
    public void setList(ArrayList<GetReviewData> list) {
        this.scheduleList = new ArrayList<GetReviewData>();
        int i;
        for(i=0;i<list.size();i++){
            this.scheduleList.add(list.get(i));
        }}
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_review_list, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        GetReviewData getReviewData = this.scheduleList.get(position);
        if (holder instanceof LoadingViewHolder) {
//            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
        else if (holder instanceof ItemViewHolder ){
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Glide.with(mContext)
                    .load(getReviewData.getUser().getAttachment().getFilename())
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .skipMemoryCache(false)
                    .centerCrop()
//                        .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(itemViewHolder.roundedImageView);
            itemViewHolder.ratingBar.setRating(new Float(getReviewData.getRating()));
            itemViewHolder.reviewTxt.setText(getReviewData.getComment());
            itemViewHolder.name.setText(getReviewData.getUser().getFirstName() + getReviewData.getUser().getLastName());
        }
    }

    @Override
    public int getItemCount() {
        return scheduleList == null ? 0 : scheduleList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return scheduleList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView roundedImageView;
        private RatingBar ratingBar;
        private TextView reviewTxt;
        private TextView name;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            roundedImageView = itemView.findViewById(R.id.iv_avatar_bid);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            reviewTxt = itemView.findViewById(R.id.review_txt);
            name = itemView.findViewById(R.id.name);
        }
    }
    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }


}