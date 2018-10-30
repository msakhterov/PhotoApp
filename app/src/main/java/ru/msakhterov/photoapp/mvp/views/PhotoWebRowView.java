package ru.msakhterov.photoapp.mvp.views;

import ru.msakhterov.photoapp.mvp.models.entity.flickr.PhotoUrl;

public interface PhotoWebRowView
{
    void setPhoto(PhotoUrl photoUrl);

    void setFavorite(int isFavorite);
}
