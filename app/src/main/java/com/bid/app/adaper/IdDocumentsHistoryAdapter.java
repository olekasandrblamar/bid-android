package com.bid.app.adaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.response.IdDocumentsData;
import com.bid.app.util.Constants;

import java.util.ArrayList;
import java.util.List;


public class IdDocumentsHistoryAdapter extends RecyclerView.Adapter<IdDocumentsHistoryAdapter.ViewHolder> {


    private static final String TAG = IdDocumentsHistoryAdapter.class.getSimpleName();

    private Context mContext;
    private List<IdDocumentsData> idDocumentsDataList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;


    public IdDocumentsHistoryAdapter(Context context, ArrayList<IdDocumentsData> idDocuments, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.idDocumentsDataList = idDocuments;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public IdDocumentsHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_id_documents, parent, false);
        return new IdDocumentsHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final IdDocumentsData idDocumentsData = idDocumentsDataList.get(position);

        if (idDocumentsData.getName() != null && !idDocumentsData.getName().isEmpty()) {
            holder.tvName.setText(idDocumentsData.getName());

            if (idDocumentsData.getName().equalsIgnoreCase(Constants.NIB_CARD)) {
                holder.ivImage.setImageResource(R.drawable.booking_idcard_png);
            } else if (idDocumentsData.getName().equalsIgnoreCase(Constants.LICENSE)) {
                holder.ivImage.setImageResource(R.drawable.booking_license);
            } else if (idDocumentsData.getName().equalsIgnoreCase(Constants.PASSPORT)) {
                holder.ivImage.setImageResource(R.drawable.booking_passport);
            } else {
                holder.ivImage.setImageResource(R.drawable.booking_idcard_png);
            }
        }

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickIdDocuments(position, idDocumentsData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return idDocumentsDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImage;
        private AppCompatTextView tvName;
        private AppCompatTextView tvDesc;

        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.iv_id_documents_adapter);
            tvName = itemView.findViewById(R.id.tv_name_id_documents_adapter);
            tvDesc = itemView.findViewById(R.id.tv_desc_id_documents_adapter);
            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }
}



