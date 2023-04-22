package com.bid.app.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.view.About;

import java.util.List;

public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.ViewHolder> {

    private static final String TAG = SettingsAdapter.class.getSimpleName();

    private Context mContext;
    private List<About> aboutList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public AboutAdapter(Context context, List<About> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.aboutList = list;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public AboutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_about_bid_list, parent, false);
        return new AboutAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AboutAdapter.ViewHolder holder, final int position) {

        final About about = aboutList.get(position);

        holder.tvName.setText(about.getName());

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickOnAbout(position, about);
            }
        });
    }

    @Override
    public int getItemCount() {
        return aboutList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;

        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name_about_bid_adapter_list);

            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }

}