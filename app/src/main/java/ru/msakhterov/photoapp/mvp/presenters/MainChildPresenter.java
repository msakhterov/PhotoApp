package ru.msakhterov.photoapp.mvp.presenters;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import ru.msakhterov.photoapp.mvp.models.cache.PhotoCache;
import ru.msakhterov.photoapp.mvp.models.entity.Photo;
import ru.msakhterov.photoapp.mvp.views.PhotoGalleryView;
import ru.msakhterov.photoapp.mvp.views.PhotoRowView;
import timber.log.Timber;

@InjectViewState
public class MainChildPresenter extends MvpPresenter<PhotoGalleryView> {

    @Inject
    PhotoCache photoCache;

    private Scheduler scheduler;

    private IPhotoListPresenter photoListPresenter = new PhotoListPresenter();
    private List<Photo> photos = new ArrayList<>();
    private int spanCount;

    public MainChildPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public IPhotoListPresenter getPhotoListPresenter() {
        return photoListPresenter;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    @SuppressLint("CheckResult")
    public void loadPhotos(int requestType) {
        photoCache.getAllPhotos(requestType)
                .subscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .subscribe(photos -> {
                    MainChildPresenter.this.photos = photos;
                    MainChildPresenter.this.getViewState().updatePhotoList();
                }, throwable -> Timber.e(throwable));
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    class PhotoListPresenter implements IPhotoListPresenter {

        @Override
        public void bindPhotoListRow(int pos, PhotoRowView rowView) {
            if (photos != null) {
                rowView.setPhoto(photos.get(pos));
                rowView.setFavorite(photos.get(pos).isFavorite());
            }
        }

        @Override
        public int getPhotoCount() {
            return photos.size();
        }

        public int getSpanCount() {
            return spanCount;
        }

        @SuppressLint("CheckResult")
        @Override
        public void setIsFavorite(Photo photo) {
            photoCache.putPhoto(photo)
                    .subscribeOn(Schedulers.io())
                    .observeOn(scheduler)
                    .subscribe(s -> {
                        Timber.d(s);

                        MainChildPresenter.this.getViewState().refreshViewPager();
                    });
        }
    }
}
