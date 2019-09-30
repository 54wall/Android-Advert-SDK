package advert.sdk.com.advertsdk;

import android.app.Application;
import android.util.Log;

import advert.sdk.com.advertlibrary.engine.AdvertEngine;


/**
 * Created by 18271 on 19/06/2017.
 */

public class MyApplication  extends Application{
    private String TAG = MyApplication.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        AdvertEngine.init(this);

    }
}
