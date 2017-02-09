package com.libray.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import com.android.volley.toolbox.ImageLoader;

/**
 * @author t77yq @2014-07-22.
 */
public enum BitmapLruCache  implements ImageLoader.ImageCache {

	
    INSTANCE;

    private LruCache<String, Bitmap> cache;
    public static final int DEFAULT_BITMAP_CACHE_SIZE = 4 * 1024 * 1024;
    public static BitmapLruCache getInstance() {
        return INSTANCE;
    }

    private BitmapLruCache() {
        cache = new LruCache<String, Bitmap>(DEFAULT_BITMAP_CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return cache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        cache.put(url, bitmap);
    }
}
