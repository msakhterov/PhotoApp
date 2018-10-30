package ru.msakhterov.photoapp.mvp.presenters;

import ru.msakhterov.photoapp.mvp.models.entity.Photo;
import ru.msakhterov.photoapp.mvp.views.PhotoRowView;


public interface IPhotoListPresenter {
    void bindPhotoListRow(int pos, PhotoRowView rowView);
    int getPhotoCount();
    int getSpanCount();
    void setIsFavorite(Photo photo);

}
