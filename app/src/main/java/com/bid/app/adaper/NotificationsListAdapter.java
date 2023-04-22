package com.bid.app.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.response.NotificationListInfo;
import com.bid.app.util.Constants;
import com.bumptech.glide.Glide;

import java.util.List;

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.ViewHolder> {

    private static final String TAG = NotificationsListAdapter.class.getSimpleName();

    private Context mContext;
    private List<NotificationListInfo> notificationsList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public NotificationsListAdapter(Context context, List<NotificationListInfo> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.notificationsList = list;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public NotificationsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_notifications_list, parent, false);
        return new NotificationsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationsListAdapter.ViewHolder holder, final int position) {

        final NotificationListInfo notifications = notificationsList.get(position);

        holder.tvName.setText(notifications.getTitle());
        holder.tvDesc.setText(notifications.getBody());
        if(notifications.getType().equals("Family")) {
            holder.controlPad.setVisibility(View.VISIBLE);
        } else{
            holder.controlPad.setVisibility(View.GONE);
        }
        Glide.with(mContext)
                .load(Constants.AWS_IMAGE_URL(notifications.getAttachment(), "jpg"))
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .skipMemoryCache(false)
                .centerCrop()
//                        .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_bid_logo)
                .into(holder.ivSettings);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // AppActivityManager.redirectTo((AppCompatActivity) mContext, new AccountSettingsFragment(), R.id.frame_layout, true, false, false, null);

               // iRecyclerViewClickListener.clickOnNotificationsSettings(position, notificationsList.get(position));
            }
        });
        holder.approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickApprove(position, true);
            }
        });
        holder.denyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickApprove(position, false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvDesc;
        private ImageView ivSettings;

        private LinearLayout linearLayout;
        private LinearLayout controlPad;
        private Button denyBtn, approveBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name_notifications_adapter_list);
            tvDesc = itemView.findViewById(R.id.tv_desc_notifications_adapter_list);
            ivSettings = itemView.findViewById(R.id.iv_setting_adapter_list);
            denyBtn = itemView.findViewById(R.id.deny);
            approveBtn = itemView.findViewById(R.id.approve);
            controlPad = itemView.findViewById(R.id.control_pad);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
