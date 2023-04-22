package com.bid.app.ui.fragment.IdDocuments;

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

import java.util.ArrayList;
import java.util.List;


public class ArchivedIdDocumentsFragment extends BaseFragment {

    private static final String TAG = ArchivedIdDocumentsFragment.class.getSimpleName();

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_current_id_documents, container, false);


        initController(view);

        return view;

    }
    private void initController(View views) {


        recyclerView = views.findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DashboardActivity.context);
        recyclerView.setLayoutManager(linearLayoutManager);


        getDocuments();
    }


    private void initAdapter(final List<IdDocumentsData> list) {
        IdDocumentsHistoryAdapter idDocumentsHistoryAdapter = new IdDocumentsHistoryAdapter(requireActivity(), new ArrayList<IdDocumentsData>(), ArchivedIdDocumentsFragment.this);
        recyclerView.setAdapter(idDocumentsHistoryAdapter);
        idDocumentsHistoryAdapter.notifyDataSetChanged();
    }

    private void getDocuments() {

//        final List<IdDocumentsData> list = new ArrayList<>();

//        final IdDocumentsData idDocumentsData = new IdDocumentsData();
//        idDocumentsData.setName("Passport");
//        idDocumentsData.setDesc("Bahamas Passport");

//        list.add(idDocumentsData);
//        initAdapter(list);
    }

    @Override
    public void clickIdDocuments(int position, IdDocumentsData idDocument) {
        super.clickIdDocuments(position, idDocument);
    }
}
