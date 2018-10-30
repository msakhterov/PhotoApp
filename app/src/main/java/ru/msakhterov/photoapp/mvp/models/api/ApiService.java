package ru.msakhterov.photoapp.mvp.models.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import ru.msakhterov.photoapp.mvp.models.entity.flickr.PhotoCollection;

public interface ApiService
{
    @GET("services/rest/?method=flickr.photos.getRecent&api_key=929d7ea97303e5e52ec82b59547bd257&format=json&nojsoncallback=1&extras=url_s")
    Observable<PhotoCollection> getPhotoUrls();

}
