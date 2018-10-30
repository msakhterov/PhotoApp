package ru.msakhterov.photoapp.ui.adapters;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.msakhterov.photoapp.R;
import ru.msakhterov.photoapp.mvp.models.entity.Photo;
import ru.msakhterov.photoapp.mvp.models.photo.ImageLoaderGlide;
import ru.msakhterov.photoapp.mvp.presenters.IPhotoListPresenter;
import ru.msakhterov.photoapp.mvp.views.PhotoRowView;
import ru.msakhterov.photoapp.ui.activities.MainActivity;
import ru.msakhterov.photoapp.utils.Constants;

public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.PhotoHolder> {

    private IPhotoListPresenter presenter;
    private ImageLoaderGlide imageLoader;

    public PhotoGalleryAdapter(ImageLoaderGlide imageLoader, IPhotoListPresenter presenter) {
        this.imageLoader = imageLoader;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PhotoHolder holder, int position) {
        presenter.bindPhotoListRow(position, holder);
    }

    @Override
    public int getItemCount() {
        return presenter.getPhotoCount();
    }

    public class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PhotoRowView {

        private Photo photo;

        @BindView(R.id.item_image_view)
        ImageView mItemImageView;

        @BindView(R.id.item_favourites_btn)
        ToggleButton mToggleButton;

        PhotoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            mItemImageView.setOnClickListener(this);
            mToggleButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mToggleButton.isChecked()) {
                photo.setFavorite(Constants.IS_FAVORITE);
            } else {
                photo.setFavorite(Constants.IS_NOT_FAVORITE);
            }
            presenter.setIsFavorite(photo);


//            switch (view.getId()) {
//                case R.id.item_image_view:
//                    mPictureGalleryListener.onPictureSelected(picture);
//                    break;
//                case R.id.item_favourites_btn:
//                    presenter.setIsFavorite(photo);
//                    break;
//                    if (mToggleButton.isChecked()) {
//
//                        picture.setFavorite(Constants.IS_FAVORITE);
//                    } else {
//                        picture.setFavorite(Constants.IS_NOT_FAVORITE);
//                    }
//                    break;
//            }
        }

        @Override
        public void setPhoto(Photo photo) {

            Point size = new Point();
            ((MainActivity) itemView.getContext()).getWindowManager().getDefaultDisplay().getSize(size);
            int photoSize = size.x / presenter.getSpanCount();
            imageLoader.loadInto(photo.getPath(), mItemImageView, photoSize);
            this.photo = photo;
        }

        @Override
        public void setFavorite(int isFavorite) {
            mToggleButton.setChecked(isFavorite == Constants.IS_FAVORITE);
        }
    }
}
