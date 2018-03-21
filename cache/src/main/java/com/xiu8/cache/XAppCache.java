package com.xiu8.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;

/**
 * Created by chunyang on 2018/3/10.
 */

public class XAppCache implements IAppCache {

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;


    public static XAppCache create(Context context, String name) {
        return new XAppCache(context, name);
    }

    private XAppCache(Context context, String name) {
        mPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    @Override
    public XAppCache putString(String key, String value) {
        mEditor.putString(key, value);
        return this;
    }

    @Override
    public XAppCache putInt(String key, int value) {
        mEditor.putInt(key, value);
        return this;
    }

    @Override
    public XAppCache putLong(String key, long value) {
        mEditor.putLong(key, value);
        return this;
    }

    @Override
    public XAppCache putFloat(String key, float value) {
        mEditor.putFloat(key, value);
        return this;
    }

    @Override
    public XAppCache putBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        return this;
    }

    @Override
    public XAppCache remove(String key) {
        mEditor.remove(key);
        return this;
    }

    @Override
    public XAppCache clear() {
        mEditor.clear();
        return this;
    }

    @Override
    public String getStringValue(String key, String defaultValue) {
        return mPreferences.getString(key, defaultValue);
    }

    @Override
    public int getIntValue(String key, int defaultValue) {
        return mPreferences.getInt(key, defaultValue);
    }

    @Override
    public long getLongValue(String key, long defaultValue) {
        return mPreferences.getLong(key, defaultValue);
    }

    @Override
    public float getFloatValue(String key, float defaultValue) {
        return mPreferences.getFloat(key, defaultValue);
    }

    @Override
    public boolean getBooleanValue(String key, boolean defaultValue) {
        return mPreferences.getBoolean(key, defaultValue);
    }


    @Override
    public void apply(SharedPreferences.Editor editor) {
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    @Override
    public void apply() {
        if (mEditor == null) return;
        apply(mEditor);
    }
}
