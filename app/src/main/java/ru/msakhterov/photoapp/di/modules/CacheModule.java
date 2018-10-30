package ru.msakhterov.photoapp.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.msakhterov.photoapp.mvp.models.cache.PhotoCache;
import ru.msakhterov.photoapp.mvp.models.cache.PhotoRealmCache;

@Module
public class CacheModule {

    @Provides
    public PhotoCache photoCache(){
        return new PhotoRealmCache();
    }

}
