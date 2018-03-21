package com.xiu8.xiu8cache.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.xiu8.cache.XAppCache;
import com.xiu8.xiu8cache.R;
import com.xiu8.xiu8cache.databinding.ActivityAppCacheBinding;

public class AppCacheActivity extends AppCompatActivity {


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, AppCacheActivity.class);
        return intent;
    }


    final String TAG = "AppCacheActivity";
    final String KEY_STRING = "key_string";
    final String KEY_INT = "key_int";
    final String KEY_LONG = "key_long";
    final String KEY_FLOAT = "key_float";
    final String KEY_BOOLEAN = "key_boolean";
    boolean booleanValue = false;
    ActivityAppCacheBinding mBinding;
    XAppCache mAppCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_app_cache);
        mAppCache = XAppCache.create(this, TAG);


//        mBinding.etCacheStringType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (isSingleType) {
//
//                }
//            }
//        });
    }

    public void onSave(View view) {

        String strValue = mBinding.etCacheStringType.getText().toString().trim();
        String intType = mBinding.etCacheIntegerType.getText().toString().trim();
        String longType = mBinding.etCacheLongType.getText().toString().trim();
        String floatType = mBinding.etCacheFloatType.getText().toString().trim();
        String booleanType = mBinding.tvCacheBooleanType.getText().toString().trim();

//        if (isSingleType) {
//            if (!TextUtils.isEmpty(strValue)) {
//                mAppCache.putString(KEY_STRING, strValue);
//            }
//        } else {
        if (!TextUtils.isEmpty(strValue)) {
            mAppCache.putString(KEY_STRING, strValue);
        }
        if (!TextUtils.isEmpty(intType)) {
            mAppCache.putInt(KEY_INT, Integer.valueOf(intType));
        }
        if (!TextUtils.isEmpty(longType)) {
            mAppCache.putLong(KEY_LONG, Long.valueOf(longType));
        }

        if (!TextUtils.isEmpty(floatType)) {
            mAppCache.putFloat(KEY_FLOAT, Float.valueOf(floatType));
        }

        if (!TextUtils.isEmpty(booleanType)) {
            mAppCache.putBoolean(KEY_BOOLEAN, Boolean.valueOf(booleanType));
        }
        mAppCache.apply();
//        }

    }

    public void onCacheType(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] items = {"单类型", "多类型"};
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mBinding.btnCacheType.setText(items[which]);
            }
        });
        builder.setCancelable(true);
        builder.create().show();
    }

    public void onCacheKey(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] items = {"String", "Integer", "Long", "Float", "Boolean"};
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String result = "null";
                switch (which) {
                    case 0:
                        result = mAppCache.getStringValue(KEY_STRING, result);
                        break;
                    case 1:
                        result = String.valueOf(mAppCache.getIntValue(KEY_INT, 0));
                        break;
                    case 2:
                        result = String.valueOf(mAppCache.getLongValue(KEY_LONG, 0l));
                        break;
                    case 3:
                        result = String.valueOf(mAppCache.getFloatValue(KEY_FLOAT, 0f));
                        break;
                    case 4:
                        result = String.valueOf(mAppCache.getBooleanValue(KEY_BOOLEAN, false));
                        break;
                }
                mBinding.btnCacheKey.setText(items[which]);
                mBinding.tvCacheResult.setText(result);
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        builder.create().show();
    }

    public void onRemove(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] items = {"String", "Integer", "Long", "Float", "Boolean"};
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                String result = "没有清楚";
                switch (which) {
                    case 0:
                        mAppCache.remove(KEY_STRING);
//                        result = mAppCache.getStringValue(KEY_STRING, result);
                        break;
                    case 1:
                        mAppCache.remove(KEY_INT);
//                        result = String.valueOf(mAppCache.getIntValue(KEY_INT, -1));
                        break;
                    case 2:
                        mAppCache.remove(KEY_LONG);
//                        result = String.valueOf(mAppCache.getLongValue(KEY_LONG, -1));
                        break;
                    case 3:
                        mAppCache.remove(KEY_FLOAT);
//                        result = String.valueOf(mAppCache.getFloatValue(KEY_FLOAT, -1));
                        break;
                    case 4:
                        mAppCache.remove(KEY_BOOLEAN);
//                        result = String.valueOf(mAppCache.getBooleanValue(KEY_BOOLEAN, false));
                        break;
                }
                mAppCache.apply();
//                mBinding.tvCacheResult.setText(result);
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        builder.create().show();
    }

    public void onClear(View view) {
        mAppCache.clear().apply();
    }

    public void onChoseBoolean(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] items = {"true", "false"};
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                booleanValue = which == 0;
                mBinding.tvCacheBooleanType.setText(items[which]);
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        builder.create().show();
    }
}
