package ru.msakhterov.photoapp.mvp.models.entity.flickr;

import com.google.gson.annotations.SerializedName;

public class PhotoCollection {

    @SerializedName("photos")
    public FlickrPhotos photos;

}
