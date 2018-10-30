package ru.msakhterov.photoapp.mvp.models.cache;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import ru.msakhterov.photoapp.mvp.models.entity.Photo;

public interface PhotoCache {

    File getPhotoFile(Photo photo);

    Observable<String> putPhoto(Photo photo);

    Observable<List<Photo>> getAllPhotos(int requestType);

}
