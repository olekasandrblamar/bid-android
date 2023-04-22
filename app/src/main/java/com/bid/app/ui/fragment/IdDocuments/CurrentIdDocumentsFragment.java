package com.bid.app.ui.fragment.IdDocuments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.IdDocumentsHistoryAdapter;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.model.response.IdDocuments;
import com.bid.app.model.response.IdDocumentsData;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CurrentIdDocumentsFragment extends BaseFragment {

    private static final String TAG = CurrentIdDocumentsFragment.class.getSimpleName();

    private SessionManager sessionManager;
    private APIInterface apiInterface;
    private RecyclerView recyclerView;
    private TextView no_records_tv;
    private ArrayList<IdDocumentsData> idDocumentsData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_current_id_documents, container, false);

        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        initController(view);

        return view;

    }

    private void initController(View views) {

        recyclerView = views.findViewById(R.id.recycler_view);
        no_records_tv = views.findViewById(R.id.no_records_tv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DashboardActivity.context);
        recyclerView.setLayoutManager(linearLayoutManager);

        getDocuments();
    }

    private void initAdapter(final ArrayList<IdDocumentsData> idDocumentsData) {
        Activity activity = getActivity();
        if (activity != null) {
            if (idDocumentsData.size() > 0) {
                IdDocumentsHistoryAdapter idDocumentsHistoryAdapter = new IdDocumentsHistoryAdapter(requireActivity(), idDocumentsData, CurrentIdDocumentsFragment.this);
                recyclerView.setAdapter(idDocumentsHistoryAdapter);

                recyclerView.setVisibility(View.VISIBLE);
                no_records_tv.setVisibility(View.GONE);

            } else {
                recyclerView.setVisibility(View.GONE);
                no_records_tv.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getDocuments() {

        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);

        final Call<IdDocuments> call = apiInterface.getSavedDocuments(sessionManager.getAccessToken());
        call.enqueue(new Callback<IdDocuments>() {
            @Override
            public void onResponse(Call<IdDocuments> call, Response<IdDocuments> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                final IdDocuments idDocuments = response.body();

                if (idDocuments != null) {
                    final ErrorStatus status = idDocuments.getError();
                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        idDocumentsData = new ArrayList<>();
                        for(IdDocumentsData doc: idDocuments.getData()){
                            if(doc.getDocument() != null) {
                                idDocumentsData.add(doc);
                            }
                        }
                        initAdapter(idDocumentsData);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        no_records_tv.setVisibility(View.GONE);
                        AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<IdDocuments> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();

                recyclerView.setVisibility(View.GONE);
                no_records_tv.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void clickIdDocuments(int position, IdDocumentsData idDocument) {
        super.clickIdDocuments(position, idDocument);
    }
}
