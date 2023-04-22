package com.bid.app.adaper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bid.app.R;
import com.bid.app.model.view.TestType;

import java.util.List;

public class TestTypesListAdapter extends BaseAdapter implements android.widget.SpinnerAdapter {

    private static final String TAG = IslandAdapter.class.getSimpleName();

    private Context mContext;
    private List<TestType> centerList;

    public TestTypesListAdapter(Context context, List<TestType> centers) {
        this.centerList = centers;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return centerList.size();
    }


    @Override
    public Object getItem(int position) {
        return centerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.adapter_spinner, null);
        TextView textView = view.findViewById(R.id.tv_spinner_adapter);

        textView.setText(centerList.get(position).getName());
        if (position == 0) {
            textView.setTextColor(mContext.getResources().getColor(R.color.gray));
        }
        else {
            textView.setTextColor(mContext.getResources().getColor(R.color.colorText));
        }

        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.adapter_spinner, null);
        final TextView textView = view.findViewById(R.id.tv_spinner_adapter);

        textView.setText(centerList.get(position).getName());
        if (position == 0) {
            textView.setTextColor(mContext.getResources().getColor(R.color.gray));
        }
        else {
            textView.setTextColor(mContext.getResources().getColor(R.color.colorText));
        }

        return view;
    }
}