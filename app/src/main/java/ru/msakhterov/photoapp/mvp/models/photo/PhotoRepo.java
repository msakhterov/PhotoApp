package ru.msakhterov.photoapp.mvp.models.photo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.msakhterov.photoapp.mvp.models.api.ApiService;
import ru.msakhterov.photoapp.mvp.models.entity.flickr.PhotoUrl;
import ru.msakhterov.photoapp.utils.NetworkStatus;
import timber.log.Timber;

public class PhotoRepo {

    private ApiService api;

    public PhotoRepo(ApiService api) {
        this.api = api;
    }

    public Observable<List<PhotoUrl>> getPhotoUrls() {
        if (NetworkStatus.isOnline()) {
            return api.getPhotoUrls()
                    .map(s ->{
                        List<PhotoUrl> photoUrls = s.photos.photo;
                        return photoUrls;
                    }).subscribeOn(Schedulers.computation());
        }
        return null;
    }
}
