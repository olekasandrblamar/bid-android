package com.bid.app.adaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.response.IdDocumentsData;
import com.bid.app.model.view.FamilyData;
import com.bid.app.util.Constants;

import java.util.ArrayList;
import java.util.List;


public class FamilyListAdapter extends RecyclerView.Adapter<FamilyListAdapter.ViewHolder> {


    private static final String TAG = FamilyListAdapter.class.getSimpleName();

    private Context mContext;
    private List<FamilyData> idDocumentsDataList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;


    public FamilyListAdapter(Context context, ArrayList<FamilyData> idDocuments, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.idDocumentsDataList = idDocuments;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public FamilyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_family_list, parent, false);
        return new FamilyListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final FamilyData idDocumentsData = idDocumentsDataList.get(position);

        holder.relationship.setText(idDocumentsData.getRelationship());

        holder.name.setText(idDocumentsData.getFullName());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickFamilyData(position, idDocumentsData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return idDocumentsDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        private LinearLayout linearLayout;
        private TextView relationship, name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.constraint_layout);
            relationship = itemView.findViewById(R.id.relationship);
            name = itemView.findViewById(R.id.name);
        }
    }
}



