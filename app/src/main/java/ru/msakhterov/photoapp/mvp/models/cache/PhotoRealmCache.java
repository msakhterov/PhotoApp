package ru.msakhterov.photoapp.mvp.models.cache;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.realm.Realm;
import ru.msakhterov.photoapp.App;
import ru.msakhterov.photoapp.mvp.models.entity.Photo;
import ru.msakhterov.photoapp.mvp.models.entity.realm.CachedPhoto;
import ru.msakhterov.photoapp.utils.Constants;
import timber.log.Timber;

public class PhotoRealmCache implements PhotoCache {
    private static final String PHOTO_FOLDER_NAME = "photo";

    @Override
    public File getPhotoFile(Photo photo) {
        File filesDir = App.getInstance().getApplicationContext().getFilesDir();
        return new File(filesDir, photo.getPhotoFilename());
    }

    @Override
    public Observable<String> putPhoto(Photo photo) {
        return Observable.create(e -> {
            Realm realm = Realm.getDefaultInstance();
            final CachedPhoto realmPhoto = realm.where(CachedPhoto.class).equalTo("path", photo.getStringPath()).findFirst();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm innerRealm) {
                    if (realmPhoto == null) {
                        CachedPhoto newCachedPhoto = innerRealm.createObject(CachedPhoto.class, photo.getStringPath());
                        newCachedPhoto.setIsFavorite(photo.isFavorite());
                        newCachedPhoto.setSource(photo.getSource());
                        e.onNext("Photo added");
                    } else {
                        realmPhoto.setIsFavorite(photo.isFavorite());
                        e.onNext("Photo favorite status changed");
                    }
                }
            });
            realm.close();
            e.onComplete();
        });
    }



@Override
    public Observable<List<Photo>> getAllPhotos(int requestType) {
        return Observable.create(e -> {
            Realm realm = Realm.getDefaultInstance();
            List<Photo> photos = new ArrayList<>();
            List<CachedPhoto> realmPhotos = null;
            switch (requestType) {
                case Constants.GET_ALL_PHOTOS:
                    realmPhotos = realm.where(CachedPhoto.class).findAll();
                    Timber.d("GET_ALL_PHOTOS");
                    break;
                case Constants.GET_FAV_PHOTOS:
                    realmPhotos = realm.where(CachedPhoto.class).equalTo("isFavorite", Constants.IS_FAVORITE).findAll();
                    Timber.d("GET_FAV_PHOTOS");
                    break;
                case Constants.GET_ALL_DB_PHOTOS:
                    realmPhotos = realm.where(CachedPhoto.class).equalTo("source", Constants.DB_SOURCE).findAll();
                    Timber.d("GET_ALL_DB_PHOTOS");
                    break;
                case Constants.GET_FAV_DB_PHOTOS:
                    realmPhotos = realm.where(CachedPhoto.class).equalTo("source", Constants.DB_SOURCE).equalTo("isFavorite", Constants.IS_FAVORITE).findAll();
                    Timber.d("GET_FAV_DB_PHOTOS");
                    break;
                case Constants.GET_FAV_WEB_PHOTOS:
                    realmPhotos = realm.where(CachedPhoto.class).equalTo("source", Constants.WEB_SOURCE).equalTo("isFavorite", Constants.IS_FAVORITE).findAll();
                    Timber.d("GET_FAV_WEB_PHOTOS");
                    break;
            }
            if (realmPhotos == null) {
                e.onError(new RuntimeException("No photos in cache"));
            } else {
                for (CachedPhoto cachedPhoto : realmPhotos) {
                    photos.add(new Photo(new File(cachedPhoto.getPath()), cachedPhoto.isFavorite(), cachedPhoto.getSource()));
                    Timber.d(cachedPhoto.getPath());
                }
                e.onNext(photos);
            }
            realm.close();
            e.onComplete();
        });
    }

    public Photo getPhoto(String path) {
        Realm realm = Realm.getDefaultInstance();
        CachedPhoto cachedPhoto = realm.where(CachedPhoto.class).equalTo("path", path).findFirst();
        if (cachedPhoto != null) {
            Photo photo = new Photo(new File(cachedPhoto.getPath()), cachedPhoto.isFavorite(), cachedPhoto.getSource());
            realm.close();
            return photo;
        }
        realm.close();
        return null;
    }

    public File saveImage(final String url, Bitmap bitmap) {
        if (!getImageDir().exists() && !getImageDir().mkdirs()) {
            throw new RuntimeException("Failed to create directory: " + getImageDir().toString());
        }

        final String fileFormat = url.contains(".jpg") ? ".jpg" : ".png";
        final File imageFile = new File(getImageDir(), SHA1(url) + fileFormat);
        FileOutputStream fos;

        try {
            fos = new FileOutputStream(imageFile);
            bitmap.compress(fileFormat.equals("jpg") ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            Timber.d("Failed to save image");
            return null;
        }

        Realm.getDefaultInstance().executeTransactionAsync(realm -> {
            CachedPhoto cachedPhoto = new CachedPhoto();
            cachedPhoto.setPath(imageFile.toString());
            realm.copyToRealm(cachedPhoto);
        });

        return imageFile;
    }

    public File getFile(String url) {
        CachedPhoto cachedPhoto = Realm.getDefaultInstance().where(CachedPhoto.class).equalTo("url", url).findFirst();
        if (cachedPhoto != null) {
            return new File(cachedPhoto.getPath());
        }
        return null;
    }

    public boolean contains(String path) {
        return Realm.getDefaultInstance().where(CachedPhoto.class).equalTo("path", path).count() > 0;
    }

    public void clear() {
        Realm.getDefaultInstance().executeTransaction(realm -> realm.delete(CachedPhoto.class));
        deleteFileOrDirRecursive(getImageDir());
    }


    public File getImageDir() {
        return new File(App.getInstance().getExternalFilesDir(null) + "/" + PHOTO_FOLDER_NAME);
    }

    public String SHA1(String s) {
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(s.getBytes(), 0, s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }

    public float getSizeKb() {
        return getFileOrDirSize(getImageDir()) / 1024f;
    }

    public void deleteFileOrDirRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteFileOrDirRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }

    public long getFileOrDirSize(File f) {
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFileOrDirSize(file);
            }
        } else {
            size = f.length();
        }
        return size;
    }
}
