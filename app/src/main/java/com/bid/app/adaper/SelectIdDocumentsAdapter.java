package com.bid.app.adaper;

import android.annotation.SuppressLint;
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
import com.bid.app.model.view.SelectIdDocument;

import java.util.List;


public class SelectIdDocumentsAdapter extends RecyclerView.Adapter<SelectIdDocumentsAdapter.ViewHolder> {

    private static final String TAG = SelectIdDocumentsAdapter.class.getSimpleName();
    private Context mContext;
    private List<SelectIdDocument> selectIdDocumentList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public SelectIdDocumentsAdapter(Context context, List<SelectIdDocument> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.selectIdDocumentList = list;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public SelectIdDocumentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_id_document_list, parent, false);
        return new SelectIdDocumentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectIdDocumentsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final SelectIdDocument selectIdDocument = selectIdDocumentList.get(position);

        holder.tv_name_menu_bid_adapter.setText(selectIdDocument.getName());
        holder.iv_menu_bid_adapter.setImageResource(selectIdDocument.getResource());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickAddIDCardType(position, selectIdDocument);
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectIdDocumentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name_menu_bid_adapter;
        private ImageView iv_menu_bid_adapter;

        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name_menu_bid_adapter = itemView.findViewById(R.id.tv_name_menu_bid_adapter);
            iv_menu_bid_adapter = itemView.findViewById(R.id.iv_menu_bid_adapter);
            constraintLayout = itemView.findViewById(R.id.constraint_layout);

        }
    }
}