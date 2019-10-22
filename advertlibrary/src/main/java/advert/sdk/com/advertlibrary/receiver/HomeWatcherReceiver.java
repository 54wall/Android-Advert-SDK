package advert.sdk.com.advertlibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import advert.sdk.com.advertlibrary.bean.AdvertBean;
import advert.sdk.com.advertlibrary.utils.SPUtils;

import static advert.sdk.com.advertlibrary.service.AdvertService.ADURLTEST1;


/**
 *
 */

public class HomeWatcherReceiver extends BroadcastReceiver {
    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private String TAG = HomeWatcherReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive");
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                // 按下Home键
                Log.e(TAG, "按下Home键");
                SPUtils.put(context, "HomeHitCount", SPUtils.getInt(context, "HomeHitCount") + 1);
                //第一次弹
                if (SPUtils.getInt(context, "HomeHitCount") == 1) {
                    Log.e(TAG, "第一次弹");
                    AdvertBean insertadvertBean = new AdvertBean(5000, 0, 0, "https://pic4.zhimg.com//v2-67d349d4c879effe311f5401e0bdc347.jpg", ADURLTEST1);//插屏
//                    ShowWindowAdvertUtils.init(insertadvertBean.getAdvertType(), insertadvertBean.getBannerLocation(), insertadvertBean.getAdvertApkDownloadUrl(), insertadvertBean.getAdvertPicUrl(), insertadvertBean.getAdvertTime(), context);
//                    ShowWindowAdvertUtils.show();
                }
            }
        }
    }
}
