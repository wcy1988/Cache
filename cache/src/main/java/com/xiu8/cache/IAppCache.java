package com.xiu8.cache;

import android.content.SharedPreferences;

/**
 * Created by chunyang on 2018/3/10.
 */

interface IAppCache {


    IAppCache putString(String key, String value);

    IAppCache putInt(String key, int value);

    IAppCache putLong(String key, long value);

    IAppCache putFloat(String key, float value);

    IAppCache putBoolean(String key, boolean value);

    IAppCache remove(String key);

    IAppCache clear();

    String getStringValue(String key, String defaultValue);

    int getIntValue(String key, int defaultValue);

    long getLongValue(String key, long defaultValue);

    float getFloatValue(String key, float defaultValue);

    boolean getBooleanValue(String key, boolean defaultValue);

    void apply(SharedPreferences.Editor editor);

    void apply();
}
