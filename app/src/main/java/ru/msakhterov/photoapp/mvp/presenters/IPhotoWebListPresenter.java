package ru.msakhterov.photoapp.mvp.presenters;

import ru.msakhterov.photoapp.mvp.views.PhotoWebRowView;


public interface IPhotoWebListPresenter
{
    void bindPhotoListRow(int pos, PhotoWebRowView rowView);
    int getPhotoCount();
    int getSpanCount();
    void setIsFavorite(int position);

}
