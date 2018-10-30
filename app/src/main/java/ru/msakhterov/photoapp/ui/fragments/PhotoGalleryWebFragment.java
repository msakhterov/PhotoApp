package ru.msakhterov.photoapp.ui.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.msakhterov.photoapp.App;
import ru.msakhterov.photoapp.R;
import ru.msakhterov.photoapp.mvp.models.photo.ImageLoaderGlide;
import ru.msakhterov.photoapp.mvp.presenters.MainChildWebPresenter;
import ru.msakhterov.photoapp.mvp.views.PhotoGalleryView;
import ru.msakhterov.photoapp.ui.activities.MainActivity;
import ru.msakhterov.photoapp.ui.adapters.PhotoGalleryWebAdapter;
import ru.msakhterov.photoapp.utils.Constants;

public class PhotoGalleryWebFragment extends MvpAppCompatFragment implements PhotoGalleryView, PhotoListUpdatable {

    private static final String ARG_FRAGMENT_TYPE = "fragment_type";

    private int fragmentType;
    private int spanCount;
    private RecyclerView recyclerView;
    private PhotoGalleryWebAdapter photoGalleryAdapter;

    @Inject
    ImageLoaderGlide imageLoader;

    @InjectPresenter
    MainChildWebPresenter presenter;

    @ProvidePresenter
    MainChildWebPresenter provideMainChildWebPresenter() {
        MainChildWebPresenter presenter = new MainChildWebPresenter(AndroidSchedulers.mainThread());
        App.getInstance().getAppComponent().inject(presenter);
        return presenter;
    }

    public static PhotoGalleryWebFragment newInstance(int fragmentType) {
        Bundle args = new Bundle();
        args.putInt(ARG_FRAGMENT_TYPE, fragmentType);
        PhotoGalleryWebFragment fragment = new PhotoGalleryWebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App.getInstance().getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCount = Constants.SPAN_COUNT_VERTICAL;
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = Constants.SPAN_COUNT_HORIZONTAL;
        }
        presenter.setSpanCount(spanCount);
        fragmentType = getArguments().getInt(ARG_FRAGMENT_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
        photoGalleryAdapter = new PhotoGalleryWebAdapter(imageLoader, presenter.getPhotoListPresenter());
        recyclerView.setAdapter(photoGalleryAdapter);
        updateUI();
        return view;
    }

    @Override
    public void updateUI() {
        presenter.loadPhotosFromWeb();
    }

    @Override
    public void updatePhotoList() {
        photoGalleryAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshViewPager() {
        ((MainActivity) getActivity()).updatePicturesList();
    }

    public int getFragmentType() {
        return fragmentType;
    }

    public boolean onBackPressed() {
        getActivity().onBackPressed();
        return true;
    }
}
