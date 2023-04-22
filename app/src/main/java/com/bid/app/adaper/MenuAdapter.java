package com.bid.app.adaper;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bid.app.duonav.views.DuoOptionView;
import com.bid.app.model.view.Menu;

import java.util.ArrayList;

public class MenuAdapter extends BaseAdapter {

    private ArrayList<Menu> mOptions = new ArrayList<>();
    private ArrayList<DuoOptionView> mOptionViews = new ArrayList<>();

    public MenuAdapter(ArrayList<Menu> options) {
        mOptions = options;
    }

    @Override
    public int getCount() {
        return mOptions.size();
    }

    @Override
    public Object getItem(int position) {
        return mOptions.get(position);
    }

    public void setViewSelected(int position, boolean selected) {

        for (int i = 0; i < mOptionViews.size(); i++) {
            if (i == position) {
                mOptionViews.get(i).setSelected(selected);
            } else {
                mOptionViews.get(i).setSelected(!selected);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Menu menuItem = mOptions.get(position);

        // Using the DuoOptionView to easily recreate the demo
        final DuoOptionView optionView;
        if (convertView == null) {
            optionView = new DuoOptionView(parent.getContext());
        } else {
            optionView = (DuoOptionView) convertView;
        }

        optionView.bind(menuItem.getName(), menuItem.getResource(), null);

        mOptionViews.add(optionView);

        return optionView;
    }
}
