package com.xiu8.cache;

import android.graphics.Bitmap;

/**
 * Created by chunyang on 2018/3/10.
 */

public interface IFileCache {

    void put(String key, String value);

    void put(String key, String value, int saveTime);

    void put(String key, Bitmap bitmap);

    void put(String key, Bitmap bitmap, int saveTime);

    String getString(String key);

    Bitmap getBitmap(String key);

    boolean remove(String key);

    boolean clear();

    void putLruBitmap(String key, Bitmap bitmap);

    Bitmap getLruBitmap(String key);

    boolean removeLru(String key);

    void clearLru();
}
