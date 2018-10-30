package ru.msakhterov.photoapp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;
import ru.msakhterov.photoapp.di.DaggerTestComponent;
import ru.msakhterov.photoapp.di.TestComponent;
import ru.msakhterov.photoapp.di.modules.TestRealmCacheModule;
import ru.msakhterov.photoapp.mvp.models.cache.PhotoRealmCache;
import ru.msakhterov.photoapp.mvp.models.entity.Photo;
import ru.msakhterov.photoapp.mvp.presenters.MainChildPresenter;
import ru.msakhterov.photoapp.mvp.views.PhotoGalleryView;

public class MainChildPresenterUnitTest {

    private MainChildPresenter presenter;
    private TestScheduler testScheduler;

    @Mock
    PhotoGalleryView photoGalleryView;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testScheduler = new TestScheduler();
        presenter = Mockito.spy(new MainChildPresenter(testScheduler));
    }

    @Test
    public void loadPhotosTest() {
        List<Photo> photos = new ArrayList<>();
        photos.add(new Photo());
        TestComponent component = DaggerTestComponent.builder()
                .testRealmCacheModule(new TestRealmCacheModule() {
            @Override
            public PhotoRealmCache photoRealmCache () {
                PhotoRealmCache cache = super.photoRealmCache();
                Mockito.when(cache.getAllPhotos(0)).thenReturn(Observable.just(photos));
                return cache;
            }
        }).build();

        component.inject(presenter);
        presenter.attachView(photoGalleryView);
        presenter.loadPhotos(0);
        Mockito.verify(presenter).loadPhotos(0);

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        Mockito.verify(photoGalleryView).updatePhotoList();
    }
}
