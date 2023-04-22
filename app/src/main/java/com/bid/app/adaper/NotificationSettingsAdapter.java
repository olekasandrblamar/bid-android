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
import com.bid.app.model.response.NotificationListInfo;

import java.util.List;

public class NotificationSettingsAdapter extends RecyclerView.Adapter<NotificationSettingsAdapter.ViewHolder> {

    private static final String TAG = NotificationSettingsAdapter.class.getSimpleName();

    private Context mContext;
    private List<NotificationListInfo> notificationSettings;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public NotificationSettingsAdapter(Context context, List<NotificationListInfo> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.notificationSettings = list;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public NotificationSettingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_notification_settings_list, parent, false);
        return new NotificationSettingsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationSettingsAdapter.ViewHolder holder, final int position) {

        final NotificationListInfo setting = notificationSettings.get(position);

        holder.tvName.setText(setting.getTitle());
        holder.tvDesc.setText(setting.getBody());

        if(setting.getIs_read().equals("1")){
            holder.ivSettings.setImageResource(R.drawable.ic_turn_on);
        } else {
            holder.ivSettings.setImageResource(R.drawable.ic_turn_off);
        }

        holder.ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setting.getIs_read().equals("1")) {
                    holder.ivSettings.setImageResource(R.drawable.ic_turn_off);
                    setting.setIs_read("0");
                } else {
                    holder.ivSettings.setImageResource(R.drawable.ic_turn_on);
                    setting.setIs_read("1");
                }
            }
        });

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickOnNotificationsSettings(position, setting);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationSettings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvDesc;
        private ImageView ivSettings;

        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name_notifications_adapter_list);
            tvDesc = itemView.findViewById(R.id.tv_desc_notifications_adapter_list);
            ivSettings = itemView.findViewById(R.id.iv_setting_adapter_list);

            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }
}
