package ru.msakhterov.photoapp.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.msakhterov.photoapp.di.modules.AppModule;
import ru.msakhterov.photoapp.di.modules.CacheModule;
import ru.msakhterov.photoapp.di.modules.ImageModule;
import ru.msakhterov.photoapp.di.modules.RepoModule;
import ru.msakhterov.photoapp.mvp.presenters.MainChildPresenter;
import ru.msakhterov.photoapp.mvp.presenters.MainChildWebPresenter;
import ru.msakhterov.photoapp.mvp.presenters.MainPresenter;
import ru.msakhterov.photoapp.ui.activities.MainActivity;
import ru.msakhterov.photoapp.ui.fragments.PhotoGalleryFragment;
import ru.msakhterov.photoapp.ui.fragments.PhotoGalleryWebFragment;

@Singleton
@Component(modules = {
        AppModule.class,
        RepoModule.class,
        CacheModule.class,
        ImageModule.class
})

public interface AppComponent {
    void inject(MainActivity activity);
    void inject(MainPresenter presenter);
    void inject(MainChildPresenter presenter);
    void inject(MainChildWebPresenter presenter);
    void inject(PhotoGalleryFragment fragment);
    void inject(PhotoGalleryWebFragment fragment);

}
