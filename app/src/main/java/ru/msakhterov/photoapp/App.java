package ru.msakhterov.photoapp;

import android.app.Application;

import io.realm.Realm;
import ru.msakhterov.photoapp.di.AppComponent;
import ru.msakhterov.photoapp.di.DaggerAppComponent;
import ru.msakhterov.photoapp.di.modules.AppModule;
import timber.log.Timber;

public class App extends Application {
    private static App instance;

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Realm.init(this);
        Timber.plant(new Timber.DebugTree());
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}

