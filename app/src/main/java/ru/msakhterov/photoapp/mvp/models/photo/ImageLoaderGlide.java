package ru.msakhterov.photoapp.mvp.models.photo;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import java.io.File;

import ru.msakhterov.photoapp.mvp.models.cache.PhotoCache;

public class ImageLoaderGlide implements ImageLoader<ImageView> {

    PhotoCache imageCache;

    public ImageLoaderGlide(PhotoCache imageCache) {
        this.imageCache = imageCache;
    }

    @Override
    public void loadInto(@Nullable File path, ImageView container, int size) {
        GlideApp.with(container.getContext()).asBitmap().load(path).override(size, size).into(container);
    }

    @Override
    public void webLoadInto(@Nullable String url, ImageView container, int size) {
        GlideApp.with(container.getContext()).asBitmap().load(url).override(size, size).into(container);
    }

}


