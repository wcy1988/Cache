package com.xiu8.xiu8cache;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.xiu8.xiu8cache.ui.AppCacheActivity;
import com.xiu8.xiu8cache.ui.FileCacheActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onAppCache(View view) {
        startActivity(AppCacheActivity.getStartIntent(this));
    }

    public void onFileCache(View view) {
        startActivity(FileCacheActivity.getStartIntent(this));
    }
}
