package com.bid.app.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.view.Country;

import java.util.List;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> {

    private static final String TAG = SettingsAdapter.class.getSimpleName();

    private Context mContext;
    private List<Country> countryList;
    private IRecyclerViewClickListener iRecyclerViewClickListener;
    int selectedPosition = -1;

    public CountryListAdapter(Context context, List<Country> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.countryList = list;
        this.iRecyclerViewClickListener = listener;
    }

    @NonNull
    @Override
    public CountryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_country_list, parent, false);
        return new CountryListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CountryListAdapter.ViewHolder holder, final int position) {

        final Country country = countryList.get(position);

        holder.tvName.setText(country.getName());
        holder.tvHeader.setText(country.getCode().substring(0, 1));


        if (selectedPosition == position) {
            holder.radioButton.setChecked(true);
        } else {
            holder.radioButton.setChecked(false);
        }

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                holder.radioButton.setChecked(true);
                iRecyclerViewClickListener.clickOnCountry(position, country);
                notifyDataSetChanged();
            }
        });

        if (position > 0) {
            int i = position - 1;
            if (i < countryList.size() && country.getName().substring(0, 1).equals(countryList.get(i).getName().substring(0, 1))) {
                holder.tvHeader.setVisibility(View.GONE);
            } else {
                holder.tvHeader.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView tvHeader;
        private AppCompatTextView tvName;
        private AppCompatRadioButton radioButton;

        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHeader = itemView.findViewById(R.id.tv_header_name_country_adapter_list);
            tvName = itemView.findViewById(R.id.tv_name_country_adapter_list);
            radioButton = itemView.findViewById(R.id.radio_button_country_adapter_list);

            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }

    public void filterList(List<Country> filteredList) {
        countryList = filteredList;
        notifyDataSetChanged();
    }
}