package com.bid.app.ui.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;

import com.bid.app.R;
import com.bid.app.adaper.ViewPagerIdDocumentsAdapter;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.onboard.CountryListFragment;
import com.bid.app.util.Constants;
import com.bid.app.util.Logger;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class IdDocumentsFragment extends BaseFragment {

    private static final String TAG = IdDocumentsFragment.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private AppCompatButton btnAddIdDocuments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_id_documents, container, false);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_id_documents), R.drawable.ic_back_arrow, true);

        initController(view);

        return view;

    }

    @Override
    public void onStop() {
        Logger.e(TAG, "onStop");
        super.onStop();
        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_my_bid), R.drawable.ic_menu, true);
    }

    private void initController(View views) {

        tabLayout = views.findViewById(R.id.tab_layout);
        viewPager = views.findViewById(R.id.view_pager);
        btnAddIdDocuments = views.findViewById(R.id.btn_add_id_documents);

        btnAddIdDocuments.setOnClickListener(this);

        tabLayout.setupWithViewPager(viewPager);

        initViewPagerAdapter();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }


    private void initViewPagerAdapter() {
        final ViewPagerIdDocumentsAdapter viewPagerIdDocumentsAdapter = new ViewPagerIdDocumentsAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerIdDocumentsAdapter);
        viewPagerIdDocumentsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {

            case R.id.btn_add_id_documents:
                //FragmentSelectIdDocument
                final Bundle bundle = new Bundle();
                bundle.putString(IBundle.BUNDLE_FRAGMENT_FROM, Constants.ADD_DOCUMENT_FRAGMENT);
                AppActivityManager.redirectTo((AppCompatActivity) Objects.requireNonNull(requireActivity()), new CountryListFragment(IdDocumentsFragment.this), R.id.frame_layout, true, false, true, bundle);
                break;

        }
    }
}
