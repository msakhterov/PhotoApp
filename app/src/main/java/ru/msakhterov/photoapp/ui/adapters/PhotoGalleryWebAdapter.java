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
import ru.msakhterov.photoapp.mvp.models.entity.flickr.PhotoUrl;
import ru.msakhterov.photoapp.mvp.models.photo.ImageLoaderGlide;
import ru.msakhterov.photoapp.mvp.presenters.IPhotoWebListPresenter;
import ru.msakhterov.photoapp.mvp.views.PhotoWebRowView;
import ru.msakhterov.photoapp.ui.activities.MainActivity;
import ru.msakhterov.photoapp.utils.Constants;

public class PhotoGalleryWebAdapter extends RecyclerView.Adapter<PhotoGalleryWebAdapter.PhotoHolder> {

    private IPhotoWebListPresenter presenter;
    private ImageLoaderGlide imageLoader;

    public PhotoGalleryWebAdapter(ImageLoaderGlide imageLoader, IPhotoWebListPresenter presenter) {
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

    public class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PhotoWebRowView {

        @BindView(R.id.item_image_view)
        ImageView mItemImageView;

        @BindView(R.id.item_favourites_btn)
        ToggleButton mToggleButton;

        PhotoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mToggleButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }

        @Override
        public void setPhoto(PhotoUrl photoUrl) {
            Point size = new Point();
            ((MainActivity) itemView.getContext()).getWindowManager().getDefaultDisplay().getSize(size);
            int photoSize = size.x / presenter.getSpanCount();
            imageLoader.webLoadInto(photoUrl.getUrl_s(), mItemImageView, photoSize);
        }

        @Override
        public void setFavorite(int isFavorite) {
            mToggleButton.setChecked(isFavorite == Constants.IS_FAVORITE);
        }
    }
}
