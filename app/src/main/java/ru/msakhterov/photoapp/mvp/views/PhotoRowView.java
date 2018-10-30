package ru.msakhterov.photoapp.mvp.views;

import ru.msakhterov.photoapp.mvp.models.entity.Photo;

public interface PhotoRowView
{
    void setPhoto(Photo photo);

    void setFavorite(int isFavorite);
}
