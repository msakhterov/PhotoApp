package ru.msakhterov.photoapp.mvp.models.photo;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import java.io.File;

public interface ImageLoader<T>{

    void loadInto(@Nullable File path, T container, int spanCount);
    void webLoadInto(@Nullable String url, ImageView container, int size);
}
