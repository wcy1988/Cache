package com.xiu8.xiu8cache.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.xiu8.cache.XFileCache;
import com.xiu8.xiu8cache.R;
import com.xiu8.xiu8cache.databinding.ActivityFileCacheBinding;

import java.io.File;

public class FileCacheActivity extends AppCompatActivity {


    final String TAG = "FileCacheActivity";
    final String KEY_STRING = "key_string";
    final String KEY_STRING_TIME = "key_string_time";
    final String KEY_BITMAP = "key_bitmap";
    final String KEY_BITMAP_TIME = "key_bitmap_time";
    File mFile;
    ActivityFileCacheBinding mBinding;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, FileCacheActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_file_cache);

        mFile = getCacheDir();

    }

    private String getCacheContent() {
        String content = mBinding.etCacheFileContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        return content;
    }

    private Bitmap getCacheBitmap() {
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);
        return bmp;
    }

    public void onCacheType(View view) {

        if (mBinding.cbCacheFileLru.isChecked()) {
            XFileCache.get(mFile).putLruBitmap(KEY_BITMAP, getCacheBitmap());
            return;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String[] items = new String[]{"String", "String 4s", "Bitmap", "Bitmap 4s"};
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            XFileCache.get(mFile).put(KEY_STRING, getCacheContent());
                            break;
                        case 1:
                            XFileCache.get(mFile).put(KEY_STRING_TIME, getCacheContent(), 4);
                            break;
                        case 2:
                            XFileCache.get(mFile).put(KEY_BITMAP, getCacheBitmap());
                            break;
                        case 3:
                            XFileCache.get(mFile).put(KEY_BITMAP_TIME, getCacheBitmap(), 4);
                            break;
                    }
                    dialog.dismiss();
                }
            });
            builder.setCancelable(true);
            builder.create().show();
        }
    }

    public void onCacheRemove(View view) {
        if (mBinding.cbCacheFileLru.isChecked()) {
            XFileCache.get(mFile).removeLru(KEY_BITMAP);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String[] items = new String[]{"String", "StringTime", "Bitmap", "BitmapTime"};
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            XFileCache.get(mFile).remove(KEY_STRING);
                            break;
                        case 1:
                            XFileCache.get(mFile).remove(KEY_STRING_TIME);
                            break;
                        case 2:
                            XFileCache.get(mFile).remove(KEY_BITMAP);
                            break;
                        case 3:
                            XFileCache.get(mFile).remove(KEY_STRING_TIME);
                            break;
                    }
                    dialog.dismiss();
                }
            });
            builder.setCancelable(true);
            builder.create().show();
        }
    }

    public void onCacheClear(View view) {
        if (mBinding.cbCacheFileLru.isChecked()) {
            XFileCache.get(mFile).clearLru();
        } else {
            XFileCache.get(mFile).clear();
        }
    }

    public void getFileCache(View view) {
        if (mBinding.cbCacheFileLru.isChecked()) {
            Bitmap bitmap = XFileCache.get(mFile).getLruBitmap(KEY_BITMAP);
            if (bitmap != null) {
                mBinding.imvCacheFileResult.setImageBitmap(bitmap);
            } else {
                mBinding.tvCacheFileResult.setText("未获取到 Bitmap");
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String[] items = new String[]{"String", "StringTime", "Bitmap", "BitmapTime"};
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            String content = XFileCache.get(mFile).getString(KEY_STRING);
                            if (TextUtils.isEmpty(content)) {
                                mBinding.tvCacheFileResult.setText("未获取到 String");
                            } else {
                                mBinding.tvCacheFileResult.setText(content);
                            }
                            break;
                        case 1:
                            String contentTime = XFileCache.get(mFile).getString(KEY_STRING_TIME);
                            if (TextUtils.isEmpty(contentTime)) {
                                mBinding.tvCacheFileResult.setText("未获取到 StringTime");
                            } else {
                                mBinding.tvCacheFileResult.setText(contentTime);
                            }
                            break;
                        case 2: {
                            Bitmap bitmap = XFileCache.get(mFile).getBitmap(KEY_BITMAP);
                            if (bitmap != null) {
                                mBinding.imvCacheFileResult.setImageBitmap(bitmap);
                            } else {
                                mBinding.tvCacheFileResult.setText("未获取到 Bitmap");
                            }
                        }
                        break;
                        case 3:
                            Bitmap bitmap = XFileCache.get(mFile).getBitmap(KEY_BITMAP_TIME);
                            if (bitmap != null) {
                                mBinding.imvCacheFileResult.setImageBitmap(bitmap);
                            } else {
                                mBinding.tvCacheFileResult.setText("未获取到 Bitmap Time");
                            }
                            break;
                    }
                    dialog.dismiss();
                }
            });
            builder.setCancelable(true);
            builder.create().show();


        }
    }
}
