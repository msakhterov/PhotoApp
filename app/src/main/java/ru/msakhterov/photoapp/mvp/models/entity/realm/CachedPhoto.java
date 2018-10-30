package ru.msakhterov.photoapp.mvp.models.entity.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CachedPhoto extends RealmObject
{
    @PrimaryKey
    private String path;
    private int isFavorite;
    private int source;


    public void setPath(String path)
    {
        this.path = path;
    }

    public String getPath()
    {
        return path;
    }

    public int isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
