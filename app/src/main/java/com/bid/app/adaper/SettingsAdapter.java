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
import com.bid.app.model.view.Settings;

import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

    private static final String TAG = SettingsAdapter.class.getSimpleName();

    private Context mContext;
    private List<Settings> bidHomeList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public SettingsAdapter(Context context, List<Settings> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.bidHomeList = list;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public SettingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_settings_list, parent, false);
        return new SettingsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SettingsAdapter.ViewHolder holder, final int position) {

        final Settings setting = bidHomeList.get(position);

        holder.tvName.setText(setting.getName());
        holder.ivSettings.setImageResource(setting.getResource());

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickOnSettings(position, setting);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bidHomeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private ImageView ivSettings;

        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name_setting_adapter_list);
            ivSettings = itemView.findViewById(R.id.iv_setting_adapter_list);

            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }

}
