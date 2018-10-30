package ru.msakhterov.photoapp.di;


import javax.inject.Singleton;

import dagger.Component;
import ru.msakhterov.photoapp.di.modules.TestRealmCacheModule;
import ru.msakhterov.photoapp.mvp.presenters.MainChildPresenter;

@Singleton
@Component(modules = {TestRealmCacheModule.class})
public interface TestComponent {
    void inject(MainChildPresenter presenter);
}
