package ru.msakhterov.photoapp.utils;

public class Constants {
    public static final String MAIN_TAG = "Main";
    public static final String DB_TAG = "DB";
    public static final String WEB_TAG = "Web";
    public static final String All_TITLE = "ALL";
    public static final String FAV_TITLE = "FAVORITES";
    public static final int SPAN_COUNT_VERTICAL = 3;
    public static final int SPAN_COUNT_HORIZONTAL = 6;
    public static final int IS_FAVORITE = 1;
    public static final int IS_NOT_FAVORITE = 0;

//    FRAGMENT TYPES
    public static final int ALL_FRAGMENT_TYPE = 0;
    public static final int FAV_FRAGMENT_TYPE = 1;
    public static final int ALL_DB_FRAGMENT_TYPE = 2;
    public static final int FAV_DB_FRAGMENT_TYPE = 3;
    public static final int ALL_WEB_FRAGMENT_TYPE = 4;
    public static final int FAV_WEB_FRAGMENT_TYPE = 5;

//    PHOTO SOURCE
    public static int DB_SOURCE = 0;
    public static int WEB_SOURCE = 1;

//    GET FROM CACHE
    public static final int GET_ALL_PHOTOS = 0;
    public static final int GET_FAV_PHOTOS = 1;
    public static final int GET_ALL_DB_PHOTOS = 2;
    public static final int GET_FAV_DB_PHOTOS = 3;
    public static final int GET_FAV_WEB_PHOTOS = 4;
}
