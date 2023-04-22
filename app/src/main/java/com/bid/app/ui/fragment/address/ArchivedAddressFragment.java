package com.bid.app.ui.fragment.address;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.IdDocumentsHistoryAdapter;
import com.bid.app.model.response.IdDocumentsData;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class ArchivedAddressFragment extends BaseFragment {

    private static final String TAG = ArchivedAddressFragment.class.getSimpleName();

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_current_id_documents, container, false);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_id_documents), R.drawable.ic_back_arrow, true);

        initController(view);

        Logger.e(TAG, "ArchivedAddressFragment");

        return view;
    }

    private void initController(View view) {

        recyclerView = view.findViewById(R.id.recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.context);
        recyclerView.setLayoutManager(layoutManager);

        getDocuments();
    }


    private void initAdapter(final List<IdDocumentsData> list) {
        IdDocumentsHistoryAdapter idDocumentsHistoryAdapter = new IdDocumentsHistoryAdapter(requireActivity(), new ArrayList<IdDocumentsData>(), ArchivedAddressFragment.this);
        recyclerView.setAdapter(idDocumentsHistoryAdapter);
        idDocumentsHistoryAdapter.notifyDataSetChanged();
    }

    private void getDocuments() {

//        final List<IdDocumentsData> list = new ArrayList<>();
//
//        final IdDocumentsData idDocumentsData = new IdDocumentsData();
//        idDocumentsData.setName("Passport");
//        idDocumentsData.setDesc("Bahamas Passport");
//
//        final IdDocumentsData idDocumentsData2 = new IdDocumentsData();
//        idDocumentsData2.setName("Passport");
//        idDocumentsData2.setDesc("Bahamas Passport");
//
//        list.add(idDocumentsData);
//        list.add(idDocumentsData2);
//
//        Logger.e(TAG, "list " + list.size());
//
//        initAdapter(list);
    }
}
