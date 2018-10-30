package ru.msakhterov.photoapp.mvp.models.entity;

import android.text.format.DateFormat;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import ru.msakhterov.photoapp.utils.Constants;

public class Photo {
    private String dateFormat = "yyyyMMdd_HHmmss";
    private File path;
    private int isFavorite;
    private int source;

    public Photo() {
    }

    public Photo(File path) {
        this.path = path;
        isFavorite = Constants.IS_NOT_FAVORITE;
    }

    public Photo(File path, int isFavorite, int source) {
        this.path = path;
        this.isFavorite = isFavorite;
        this.source = source;
    }

    public String getPhotoFilename() {
        return "IMG_" + DateFormat.format(dateFormat, new Date()) + ".jpg";
    }

    public File getPath() {
        return path;
    }

    public String getStringPath(){
        try {
           return path.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setPath(File path) {
        this.path = path;
    }

    public int isFavorite() {
        return isFavorite;
    }

    public void setFavorite(int favorite) {
        isFavorite = favorite;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
