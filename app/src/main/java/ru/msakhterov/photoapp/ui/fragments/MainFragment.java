package ru.msakhterov.photoapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.msakhterov.photoapp.R;
import ru.msakhterov.photoapp.ui.adapters.MainFragmentPagerAdapter;
import timber.log.Timber;

public class MainFragment extends Fragment implements PhotoListUpdatable {

    private MainFragmentPagerAdapter adapter;
    private ViewPager viewPager;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Timber.e("MainFragment onViewCreated");
        viewPager = view.findViewById(R.id.view_pager);
        adapter = new MainFragmentPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void updateUI() {
//        Fragment fragment = findVisibleFragment();
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            if (fragment instanceof PhotoListUpdatable) {
                PhotoListUpdatable updatableFragment = (PhotoListUpdatable) fragment;
                updatableFragment.updateUI();
            }
        }
    }

    private Fragment findVisibleFragment() {
        Fragment fragment = null;
        fragment = adapter.getCurrentFragment();
        return fragment;
    }
}
