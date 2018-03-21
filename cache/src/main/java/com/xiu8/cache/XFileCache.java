package com.xiu8.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache;

import org.afinal.simplecache.ACache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chunyang on 2018/3/10.
 */

public class XFileCache implements IFileCache {


    static final Map<File, XFileCache> cacheMap = Collections.synchronizedMap(new HashMap<File, XFileCache>());

    private ACache mCache;
    private DiskLruCache mLruCache;
    private static final int APP_VERSION = 1;
    private static final int VALUE_COUNT = 1;
    private int appVersion;
    private File fileDir;
    private long maxSize;

    public static synchronized XFileCache get(File fileDir) {
        if (cacheMap.containsKey(fileDir)) {
            return cacheMap.get(fileDir);
        } else {
            XFileCache cache = new XFileCache(fileDir);
            cacheMap.put(fileDir, cache);
            return cache;
        }
    }

    public static synchronized XFileCache get(File fileDir, int appVersion) {
        if (cacheMap.containsKey(fileDir)) {
            return cacheMap.get(fileDir);
        } else {
            XFileCache cache = new XFileCache(fileDir, appVersion, ACache.MAX_SIZE);
            cacheMap.put(fileDir, cache);
            return cache;
        }
    }

    public static synchronized XFileCache get(File fileDir, int appVersion, long maxSize) {
        if (cacheMap.containsKey(fileDir)) {
            return cacheMap.get(fileDir);
        } else {
            XFileCache cache = new XFileCache(fileDir, appVersion, maxSize);
            cacheMap.put(fileDir, cache);
            return cache;
        }
    }

    private XFileCache(File fileDir) {
        this(fileDir, ACache.MAX_SIZE);
    }

    private XFileCache(File fileDir, int appVersion) {
        this(fileDir, appVersion, ACache.MAX_SIZE);
    }

    private XFileCache(File fileDir, long maxSize) {
        this(fileDir, APP_VERSION, maxSize);
    }

    private XFileCache(File fileDir, int appVersion, long maxSize) {
        this.fileDir = fileDir;
        this.appVersion = appVersion;
        this.maxSize = maxSize;
    }


    private DiskLruCache newLruCache(File fileDir, int appVersion, long maxSize) {
        try {
            return DiskLruCache.open(fileDir, appVersion, VALUE_COUNT, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private ACache newACache(File fileDir, int appVersion, int valueCount, long maxSize) {
        return ACache.get(fileDir, maxSize);
    }

    private synchronized ACache getACache() {
        if (mCache == null) {
            mCache = newACache(fileDir, appVersion, VALUE_COUNT, maxSize);
        }
        return mCache;
    }

    private synchronized DiskLruCache getLruCache() {
        if (mLruCache == null) {
            mLruCache = newLruCache(fileDir, appVersion, maxSize);
        }
        return mLruCache;
    }


    public static File getDiskCache(Context context, String cacheName) {
        String cachePath;
        //如果sd卡存在并且没有被移除
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath, cacheName);
    }

    @Override
    public void put(String key, String value) {
        key = Utils.md5(key);
        getACache().put(key, value);
    }

    @Override
    public void put(String key, String value, int saveTime) {
        key = Utils.md5(key);
        getACache().put(key, value, saveTime);
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        key = Utils.md5(key);
        getACache().put(key, bitmap);
    }

    @Override
    public void put(String key, Bitmap bitmap, int saveTime) {
        key = Utils.md5(key);
        getACache().put(key, bitmap, saveTime);
    }

    @Override
    public String getString(String key) {
        key = Utils.md5(key);
        return getACache().getAsString(key);
    }

    @Override
    public Bitmap getBitmap(String key) {
        key = Utils.md5(key);
        return getACache().getAsBitmap(key);
    }

    @Override
    public boolean remove(String key) {
        key = Utils.md5(key);
        return getACache().remove(key);
    }

    @Override
    public boolean clear() {
        getACache().clear();
        return false;
    }

    @Override
    public void putLruBitmap(String key, Bitmap bitmap) {
        key = Utils.md5(key);
        byte[] bytes = Utils.Bitmap2Bytes(bitmap);
        put(key, bytes);
    }

    @Override
    public Bitmap getLruBitmap(String key) {
        key = Utils.md5(key);
        try {
            return Utils.Bytes2Bimap(getBytes(key));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean removeLru(String key) {
        try {
            key = Utils.md5(key);
            return getLruCache().remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void clearLru() {
        try {
            getLruCache().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(String key, byte[] bytes) {
        OutputStream out = null;
        DiskLruCache.Editor editor = null;
        try {
            editor = getLruCache().edit(key);
            if (editor == null) {
                return;
            }
            out = editor.newOutputStream(0);
            out.write(bytes);
            out.flush();
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (editor != null) {
                    editor.abort();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public byte[] getBytes(String key) throws IOException {
        byte[] bytes = null;

        DiskLruCache.Snapshot snapshot = null;
        snapshot = getLruCache().get(key);
        InputStream is = snapshot.getInputStream(0);
        if (is == null) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[256];
        int len = 0;
        try {
            while ((len = is.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }


//    private void putLruString(String key, String value) {
//        key = Utils.md5(key);
//        DiskLruCache.Editor editor = null;
//        BufferedWriter bfw = null;
//        try {
//            editor = getLruCache().edit(key);
//            OutputStream os = editor.newOutputStream(0);
//            bfw = new BufferedWriter(new OutputStreamWriter(os));
//            bfw.write(value);
//            editor.commit();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (editor != null) {
//                    editor.abort();
//                }
//                if (bfw != null) {
//                    bfw.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private String getLruStrinng(String key) {
//        key = Utils.md5(key);
//        DiskLruCache.Snapshot snapshot = null;
//        BufferedReader bufferedReader = null;
//        try {
//            snapshot = getLruCache().get(key);
//            InputStream is = snapshot.getInputStream(0);
//            bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//            StringBuilder buffer = new StringBuilder();
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                buffer.append(line);
//            }
//            return buffer.toString();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (snapshot != null) {
//                snapshot.close();
//            }
//            try {
//                if (bufferedReader != null) {
//                    bufferedReader.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

}
