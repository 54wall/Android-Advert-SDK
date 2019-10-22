package advert.sdk.com.advertlibrary.engine;

import android.app.Presentation;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.DisplayManager;
import android.util.Log;

import advert.sdk.com.advertlibrary.receiver.AdvertReceiver;
import advert.sdk.com.advertlibrary.receiver.HomeWatcherReceiver;
import advert.sdk.com.advertlibrary.service.AdvertService;
import advert.sdk.com.advertlibrary.utils.SPUtils;

/**
 *
 */

public class AdvertEngine {

    private static String TAG = AdvertEngine.class.getSimpleName();

    protected DisplayManager mDisplayManager;   //使用DisplayManagerAPI可以获得当前连接的所有显示屏的枚举
    protected Presentation mPresentationMain;

    /**
     * 初始化广告程序,在程序启动时候调用
     *
     * @param context
     */
    public static void init(final Context context) {
        Log.e(TAG, "init()");
        Intent service = new Intent(context, AdvertService.class);
        context.startService(service);
        //服务创建时,启动广播保活
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
        AdvertReceiver adverBroadCast = new AdvertReceiver();
        context.registerReceiver(adverBroadCast, intentFilter);
        //启动home键监听广播
        context.registerReceiver(new HomeWatcherReceiver(), new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        //模拟服务被干死
        /*Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                context.stopService( new Intent(context,AdvertService.class));
            }
        }, 10000);*/
        SPUtils.put(context, "HomeHitCount", 0);


    }

    /**
     * 销毁服务和广播,在程序停止时候调用
     *
     * @param context
     */
    public static void burning(final Context context) {
        Intent service = new Intent(context, AdvertService.class);
        context.stopService(service);
        AdvertReceiver adverBroadCast = new AdvertReceiver();
        context.unregisterReceiver(adverBroadCast);
        context.unregisterReceiver(new HomeWatcherReceiver());
    }


}
