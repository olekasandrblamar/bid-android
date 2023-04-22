package com.bid.app.ui.fragment.onboard;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.adaper.CountryListAdapter;
import com.bid.app.interfaces.IBundle;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.response.CountryListResponse;
import com.bid.app.model.view.Country;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.IdDocuments.FragmentSelectIdDocument;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Constants;
import com.bid.app.util.Logger;
import com.bid.app.util.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CountryListFragment extends BaseFragment {

    IRecyclerViewClickListener listener;

    public CountryListFragment(IRecyclerViewClickListener listener){
        this.listener=listener;
    }

    public CountryListFragment() {
    }

    private static final String TAG = CountryListFragment.class.getSimpleName();

    private AppCompatEditText etSearch;
    private AppCompatButton btnContinue;
    private RecyclerView recyclerView;

    private CountryListAdapter countryListAdapter;

    private List<Country> countryList = new ArrayList<>();

    private Country country = null;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_country_list, container, false);
        sessionManager =new SessionManager(requireContext());

        initToolBar(view);
        initController(view);

        return view;
    }

    private String getFragmentFrom() {
        final Bundle bundle = getArguments();
        return bundle != null ? bundle.getString(IBundle.BUNDLE_FRAGMENT_FROM) : "";
    }

    private void initToolBar(View view) {
        final View viewToolbar = view.findViewById(R.id.tool_bar);

        final ImageView ivBack = view.findViewById(R.id.iv_back_tool_bar);
        final ImageView iv_right_tool_bar = view.findViewById(R.id.iv_right_tool_bar);
        final AppCompatTextView tvTitle = view.findViewById(R.id.tv_title_tool_bar);

        tvTitle.setText(getResources().getString(R.string.title_select_country));

        iv_right_tool_bar.setVisibility(View.GONE);

        ivBack.setOnClickListener(this);

        if (Constants.ADD_DOCUMENT_FRAGMENT.equalsIgnoreCase(getFragmentFrom()) || Constants.PROFILE_UPDATE_FRAGMENT.equalsIgnoreCase(getFragmentFrom())) {
            viewToolbar.setVisibility(View.GONE);
            ((DashboardActivity) Objects.requireNonNull(requireActivity())).setTitleAndImage(getResources().getString(R.string.title_select_country), R.drawable.ic_back_arrow, true);
        } else {
            viewToolbar.setVisibility(View.VISIBLE);
        }

    }

    private void initController(View view) {

        etSearch = view.findViewById(R.id.et_search_country_list);
        btnContinue = view.findViewById(R.id.btn_continue_country_list);
        recyclerView = view.findViewById(R.id.recycler_view);

        setListeners();

        /**
         * Country list
         */
        getCountryListJson();
    }

    private void setListeners() {

        btnContinue.setOnClickListener(this);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void initAdapter() {

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DashboardActivity.context);
        recyclerView.setLayoutManager(linearLayoutManager);

        countryListAdapter = new CountryListAdapter(requireActivity(), countryList, this);
        recyclerView.setAdapter(countryListAdapter);
        countryListAdapter.notifyDataSetChanged();
    }

    private void getCountryListJson() {

        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final CountryListResponse countryListResponse = objectMapper.readValue(Constants.COUNTRY_JSON, CountryListResponse.class);
            countryList = countryListResponse.getCountryList();
            initAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.iv_back_tool_bar:
                Objects.requireNonNull(getFragmentManager()).popBackStack();
                break;

            case R.id.btn_continue_country_list:
                Utils.hideKeyboard((AppCompatActivity) Objects.requireNonNull(requireActivity()));
                final Bundle bundle = new Bundle();
                bundle.putParcelable(IBundle.BUNDLE_COUNTRY_OBJECT, country);
                if (country != null) {
                    if (Constants.ADD_DOCUMENT_FRAGMENT.equalsIgnoreCase(getFragmentFrom())) {
                        AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new FragmentSelectIdDocument(), R.id.frame_layout, true, false, true, bundle);
                    } else if (Constants.PROFILE_UPDATE_FRAGMENT.equalsIgnoreCase(getFragmentFrom())) {
                        Objects.requireNonNull(getFragmentManager()).popBackStack();
                    } else if(Constants.LOG_IN_FRAGMENT.equalsIgnoreCase(getFragmentFrom())) {
                        AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new LoginFragment(), R.id.frame_layout, false, false, true, bundle);
                    } else
                    {
                        AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new VerifyMobileNumberFragment(), R.id.frame_layout, true, false, true, bundle);
                    }
                } else {
                    AlertUtils.showAlerterWarning(requireActivity(), "Please select a country to continue?");
                }
                break;
        }
    }

    private void filter(String text) {
        List<Country> filterNames = new ArrayList<>();
        for (Country s : countryList) {
            if (s.getName().toLowerCase().contains(text.toLowerCase())) {
                filterNames.add(s);
            }
        }
        countryListAdapter.filterList(filterNames);
    }

    @Override
    public void clickOnCountry(int position, Country c) {
        super.clickOnCountry(position, c);
        if(listener!=null){
            listener.clickOnCountry(position,c);
        }
        country = c;

    }
}
