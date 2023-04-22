package com.bid.app.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.view.Address;

import java.util.List;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> {

    private static final String TAG = AddressListAdapter.class.getSimpleName();

    private Context mContext;
    private List<Address> addressList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public AddressListAdapter(Context context, List<Address> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.addressList = list;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public AddressListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_address_list_old, parent, false);
        return new AddressListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressListAdapter.ViewHolder holder, final int position) {

        final Address address = addressList.get(position);

        if ("1".equalsIgnoreCase(address.getIsDefault())) {
            holder.rbtnDefault.setChecked(true);
        } else {
            holder.rbtnDefault.setChecked(false);
        }

        final String fullAddress = address.getAddressLine1()
                + ", " + address.getCity()
                + ", " + address.getCountry()
                + ", " + address.getZipCode();

        holder.tvAddress.setText(fullAddress);

        holder.rbtnDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickOnAddress(position, address, false);
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickOnAddress(position, address, true);
            }
        });

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // iRecyclerViewClickListener.clickOnAddCard(paymentGateway);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private RadioButton rbtnDefault;
        private ImageView ivDelete;

        private TextView tvDesc;
        private TextView tvAddress;

        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rbtnDefault = itemView.findViewById(R.id.rbtn_address_list_adapter);
            ivDelete = itemView.findViewById(R.id.iv_delete_list_adapter);

            tvDesc = itemView.findViewById(R.id.tv_desc_address_adapter_list);
            tvAddress = itemView.findViewById(R.id.tv_name_address_adapter_list);

            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }
}
