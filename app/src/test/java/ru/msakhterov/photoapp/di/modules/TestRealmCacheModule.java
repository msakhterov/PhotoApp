package ru.msakhterov.photoapp.di.modules;

import org.mockito.Mockito;

import dagger.Module;
import dagger.Provides;
import ru.msakhterov.photoapp.mvp.models.cache.PhotoRealmCache;

@Module
public class TestRealmCacheModule {

    @Provides
    public PhotoRealmCache photoRealmCache (){
        return Mockito.mock(PhotoRealmCache.class);
    }
}
