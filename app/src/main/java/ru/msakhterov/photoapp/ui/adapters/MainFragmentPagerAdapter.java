package ru.msakhterov.photoapp.ui.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import ru.msakhterov.photoapp.ui.fragments.PhotoGalleryFragment;
import ru.msakhterov.photoapp.utils.Constants;
import timber.log.Timber;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    private Fragment mCurrentFragment;

    public MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PhotoGalleryFragment.newInstance(Constants.ALL_FRAGMENT_TYPE);
            case 1:
                return PhotoGalleryFragment.newInstance(Constants.FAV_FRAGMENT_TYPE);
            default:
                Timber.e("Illegal tab position");
                return PhotoGalleryFragment.newInstance(Constants.ALL_FRAGMENT_TYPE);

        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return Constants.All_TITLE;
            case 1:
                return Constants.FAV_TITLE;
            default:
                return "Illegal title";
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }
}
