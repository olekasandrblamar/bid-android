package com.bid.app.ui.fragment.about;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.bid.app.R;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.model.response.StaticPageResponse;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.retrofit.APIClient;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsAndConditionsFragment extends BaseFragment {

    private static final String TAG = TermsAndConditionsFragment.class.getSimpleName();

    private APIInterface apiInterface;

    private AppCompatTextView tvTitle;
    private AppCompatTextView tvMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_terms_conditions, container, false);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.hint_terms_conditions), R.drawable.ic_back_arrow, false);

        initController(view);

        return view;
    }

    private void initController(View view) {
        tvTitle = view.findViewById(R.id.tv_text_static_page);
        tvMessage = view.findViewById(R.id.tv_content_static_page);

        callTermsAndConditionsApi();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
        }
    }

    private void callTermsAndConditionsApi() {

        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);

        final Call<StaticPageResponse> call = apiInterface.termsAndConditions();
        call.enqueue(new Callback<StaticPageResponse>() {
            @Override
            public void onResponse(Call<StaticPageResponse> call, Response<StaticPageResponse> response) {

                CustomProgressDialog.getInstance().dismissDialog();

                final StaticPageResponse staticPageResponse = response.body();
                final ErrorStatus status = staticPageResponse.getError();

                if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                    tvMessage.setText(Html.fromHtml(staticPageResponse.getStaticPage().getContent()));
                } else {
                    AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                }
            }

            @Override
            public void onFailure(Call<StaticPageResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }
}