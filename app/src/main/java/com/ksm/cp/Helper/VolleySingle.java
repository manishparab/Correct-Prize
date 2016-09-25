package com.ksm.cp.Helper;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by mparab on 1/9/2016.
 */
public class VolleySingle {

    private static VolleySingle volleySingle;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private VolleySingle(Context context) {
        requestQueue = Volley.newRequestQueue(context);

       /* imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);


            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });*/
    }

    public static VolleySingle getInstance(Context context) {
        if (volleySingle == null) {
            volleySingle = new VolleySingle(context);
        }
        return volleySingle;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
