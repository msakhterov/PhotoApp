package ru.msakhterov.photoapp.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.msakhterov.photoapp.App;

@Module
public class AppModule {
    private App app;

    public AppModule(App app){
        this.app = app;
    }

    @Provides
    public App app(){
        return app;
    }

}
