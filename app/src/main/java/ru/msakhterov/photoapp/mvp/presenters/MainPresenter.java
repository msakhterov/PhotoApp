package ru.msakhterov.photoapp.mvp.presenters;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import ru.msakhterov.photoapp.mvp.models.cache.PhotoCache;
import ru.msakhterov.photoapp.mvp.models.cache.PhotoRealmCache;
import ru.msakhterov.photoapp.mvp.models.entity.Photo;
import ru.msakhterov.photoapp.mvp.views.MainView;
import ru.msakhterov.photoapp.utils.Constants;
import timber.log.Timber;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private Scheduler scheduler;
    private Photo currentPhoto;

    @Inject
    PhotoCache photoCache;

    public MainPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void addNewPhoto() {
        currentPhoto = new Photo();
        currentPhoto.setPath(photoCache.getPhotoFile(currentPhoto));
        currentPhoto.setSource(Constants.DB_SOURCE);
        getViewState().addNewPhoto(currentPhoto.getPath());
    }

    @SuppressLint("CheckResult")
    public void saveNewPhoto() {
        photoCache.putPhoto(currentPhoto)
                .subscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .subscribe(s -> {
                    MainPresenter.this.getViewState().updatePicturesList();
                    MainPresenter.this.getViewState().showMessage(s);
                }, throwable -> {
                    Timber.e(throwable);
                });
    }

    public File getPhotoFile() {
        return currentPhoto.getPath();
    }
}
