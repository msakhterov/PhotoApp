package ru.msakhterov.photoapp.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.io.File;


@StateStrategyType(AddToEndSingleStrategy.class)
public interface MainView extends MvpView {

    @StateStrategyType(SkipStrategy.class)
    void addNewPhoto(File photoFile);

    void updatePicturesList();

    @StateStrategyType(SkipStrategy.class)
    void showMessage(String message);


}
