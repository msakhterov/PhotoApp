package ru.msakhterov.photoapp.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.msakhterov.photoapp.mvp.models.cache.PhotoCache;
import ru.msakhterov.photoapp.mvp.models.photo.ImageLoaderGlide;

@Module(includes = {AppModule.class, CacheModule.class})
public class ImageModule {

    @Singleton
    @Provides
    public ImageLoaderGlide imageLoaderGlide (PhotoCache cache){
        return new ImageLoaderGlide(cache);
    }

}
