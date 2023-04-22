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
import com.bid.app.model.view.PersonalDetail;
import com.bid.app.util.Logger;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class PersonalDetailsAdapter extends RecyclerView.Adapter<PersonalDetailsAdapter.ViewHolder> {

    private static final String TAG = PersonalDetailsAdapter.class.getSimpleName();

    private Context mContext;
    private List<PersonalDetail> personalDetailsList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public PersonalDetailsAdapter(Context context, List<PersonalDetail> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.personalDetailsList = list;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public PersonalDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_personal_details_list, parent, false);
        return new PersonalDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PersonalDetailsAdapter.ViewHolder holder, final int position) {

        final PersonalDetail personalDetail = personalDetailsList.get(position);

        holder.tvName.setText(personalDetail.getName());


        Logger.e(TAG, "getDesc --> " + personalDetail.getDesc());
        Logger.e(TAG, "getHint --> " + personalDetail.getHint());

        if (!StringUtils.isEmpty(personalDetail.getName())) {
            holder.tvName.setText(personalDetail.getName());
        } else {
            holder.tvName.setHint(personalDetail.getHint());
        }
        if (!StringUtils.isEmpty(personalDetail.getDesc())) {
            holder.tvDesc.setText(personalDetail.getDesc());
        } else {
            holder.tvDesc.setText(personalDetail.getHint());
        }

        holder.ivLeft.setImageResource(personalDetail.getResourceOne());

        if (personalDetail.getResourceTwo() != 0) {
            holder.ivRight.setImageResource(personalDetail.getResourceTwo());
            holder.ivRight.setVisibility(View.VISIBLE);
        } else {
            holder.ivRight.setVisibility(View.GONE);
        }

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickOnPersonalDetail(position, personalDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return personalDetailsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvDesc;
        private ImageView ivLeft;
        private ImageView ivRight;

        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_personal_details_adapter_list);
            tvDesc = itemView.findViewById(R.id.tv_desc_personal_details_adapter_list);
            ivLeft = itemView.findViewById(R.id.iv_personal_details_adapter_list);
            ivRight = itemView.findViewById(R.id.iv_add_personal_details_adapter_list);

            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }
}
