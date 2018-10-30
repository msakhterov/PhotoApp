package ru.msakhterov.photoapp.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.msakhterov.photoapp.mvp.models.api.ApiService;
import ru.msakhterov.photoapp.mvp.models.photo.PhotoRepo;

@Module(includes = {ApiModule.class, CacheModule.class})
public class RepoModule {

    @Provides
    public PhotoRepo photoRepo(ApiService api){
        return new PhotoRepo(api);
    }
}
