package com.ksm.cp;

import android.app.Application;
import android.graphics.Bitmap;


import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by Manish.Parab on 10/21/2015.
 */
public class ApplicationConfiguration extends Application {


    public ApplicationConfiguration() {
        super();
    }



    @Override
    public void onCreate() {
        super.onCreate();
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);


    }
}
