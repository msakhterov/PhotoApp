package ru.msakhterov.photoapp.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(SkipStrategy.class)
public interface PhotoGalleryView extends MvpView {

    void updatePhotoList();

    void updateUI();

    void refreshViewPager();

}
