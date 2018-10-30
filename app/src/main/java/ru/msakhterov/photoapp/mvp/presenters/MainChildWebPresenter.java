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
import ru.msakhterov.photoapp.mvp.models.entity.flickr.PhotoUrl;
import ru.msakhterov.photoapp.mvp.models.photo.PhotoRepo;
import ru.msakhterov.photoapp.mvp.views.PhotoGalleryView;
import ru.msakhterov.photoapp.mvp.views.PhotoWebRowView;
import timber.log.Timber;

@InjectViewState
public class MainChildWebPresenter extends MvpPresenter<PhotoGalleryView> {

    @Inject
    PhotoCache photoCache;

    @Inject
    PhotoRepo photoRepo;

    private Scheduler scheduler;

    private IPhotoWebListPresenter photoListPresenter = new PhotoListPresenter();
    private List<Photo> photos = new ArrayList<>();
    private List<PhotoUrl> photoUrls = new ArrayList<>();
    private int spanCount;

    public MainChildWebPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public IPhotoWebListPresenter getPhotoListPresenter() {
        return photoListPresenter;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    @SuppressLint("CheckResult")
    public void loadPhotosFromWeb() {
        photoRepo.getPhotoUrls()
                .subscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .subscribe(photoUrls -> {
                    MainChildWebPresenter.this.photoUrls = photoUrls;
                    Timber.e("PhotoUrls.size: " + photoUrls.size());
                    MainChildWebPresenter.this.getViewState().updatePhotoList();
                }, throwable -> Timber.e(throwable));
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }


    class PhotoListPresenter implements IPhotoWebListPresenter {

        @Override
        public void bindPhotoListRow(int pos, PhotoWebRowView rowView) {
            if (photos != null) {
                rowView.setPhoto(photoUrls.get(pos));
//                rowView.setFavorite(photos.get(pos).isFavorite());
            }
        }

        @Override
        public int getPhotoCount() {
            return photoUrls.size();
        }

        public int getSpanCount() {
            return spanCount;
        }

        @SuppressLint("CheckResult")
        @Override
        public void setIsFavorite(int position) {
            Photo photo = new Photo();
            photoCache.putPhoto(photo)
                    .subscribeOn(Schedulers.io())
                    .observeOn(scheduler)
                    .subscribe(s -> {
                        MainChildWebPresenter.this.getViewState().refreshViewPager();
                    });
        }
    }
}
