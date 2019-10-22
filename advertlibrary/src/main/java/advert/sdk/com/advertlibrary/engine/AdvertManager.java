package advert.sdk.com.advertlibrary.engine;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.RemoteViews;

import java.util.List;

import advert.sdk.com.advertlibrary.R;
import advert.sdk.com.advertlibrary.bean.AdvertBean;
import advert.sdk.com.advertlibrary.constant.AdvertConstant;
import advert.sdk.com.advertlibrary.intf.OnGetBitmapByurlListener;
import advert.sdk.com.advertlibrary.ui.SecondaryPresentation;
import advert.sdk.com.advertlibrary.ui.loopview.SecondaryPresentationLoopView;
import advert.sdk.com.advertlibrary.utils.DownloadUtils;
import advert.sdk.com.advertlibrary.utils.ShowWindowAdvertUtils;

/**
 *
 */

public class AdvertManager {
    private final Context context;
    private final List<AdvertBean> advertBeanList;
    protected DisplayManager mDisplayManager;   //使用DisplayManagerAPI可以获得当前连接的所有显示屏的枚举
    protected SecondaryPresentationLoopView mPresentationMain;
    int index;
    int indexMax;
    Handler handler = new Handler();
    private String TAG = AdvertManager.class.getSimpleName();
    //每隔两分钟显示一个
    private int TIME = 10000;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // handler自带方法实现定时器
            showAdvertManager(advertBeanList.get(index));
            index++;
            if (index == indexMax) {
                index = 0;
            }
            handler.postDelayed(this, TIME);
        }
    };

    public AdvertManager(List<AdvertBean> advertBeanList, Context context) {
        this.context = context;
        this.advertBeanList = advertBeanList;
        handler.postDelayed(runnable, TIME);
        index = 0;
        indexMax = advertBeanList == null ? 0 : advertBeanList.size();
        mDisplayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        ShowPresentationByDisplaymanager();
    }

    protected void ShowPresentationByDisplaymanager() {
        Display[] presentationDisplays = mDisplayManager.getDisplays();
        if (presentationDisplays.length > 0) {
            Display displayMain = presentationDisplays[1];
            showPresentationMain(displayMain);
        }
    }

    private void showPresentationMain(Display presentationDisplay) {
        // Dismiss the current presentation if the display has changed.
        if (mPresentationMain != null && mPresentationMain.getDisplay() != presentationDisplay) {
            Log.i(TAG, "Dismissing presentation because the current route no longer "
                    + "has a presentation display.");
            mPresentationMain.dismiss();
            mPresentationMain = null;
        }
        // Show a new presentation if needed.
        if (mPresentationMain == null && presentationDisplay != null) {
            Log.i(TAG, "Showing presentation on display: " + presentationDisplay);
            mPresentationMain = new SecondaryPresentationLoopView(context, presentationDisplay);
            //  mPresentation.setOnDismissListener(mOnDismissListener);
            mPresentationMain.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

            try {
                mPresentationMain.show();
            } catch (WindowManager.InvalidDisplayException ex) {
                Log.w(TAG, "Couldn't show presentation!  Display was removed in "
                        + "the meantime.", ex);
                mPresentationMain = null;
            }
        }
    }

    private void showAdvertManager(final AdvertBean advertBean) {
        int advertType = advertBean.getAdvertType();
        Log.e(TAG, "广告类型 advertType:" + advertType);
        //根据广告类型来选择显示方式
        switch (advertType) {
            //插入广告
            case AdvertConstant.INSERT_ADVERT_TYPE:
                Log.e(TAG, "showAdvertManager 插入广告");
                //横幅广告
            case AdvertConstant.BANNER_ADVERT_TYPE:
                Log.e(TAG, "showAdvertManager 横幅广告");
                //这两种广告都采用dialog方式
                //类型是TYPE_TOAST，像一个普通的Android Toast一样。这样就不需要申请悬浮窗权限了。
                //初始化后不首先获得窗口焦点。不妨碍设备上其他部件的点击、触摸事件。
                ShowWindowAdvertUtils.initLoopView(mPresentationMain, advertType, advertBean.getBannerLocation(), advertBean.getAdvertApkDownloadUrl(), advertBean.getAdvertPicUrl(), advertBean.getAdvertTime(), context);
                ShowWindowAdvertUtils.showLoopView();
                //定时打开
            /*    new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, advertBean.getAdvertTime());*/
                break;
            case AdvertConstant.BAR_ADVERT_TYPE:
                Log.e(TAG, "标题栏广告");
                //标题栏广告,采用notification
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showNotificationAlert(advertBean);
                    }
                }, advertBean.getAdvertTime());
                break;

        }
    }

    //弹出通知栏显示广告
    private void showNotificationAlert(final AdvertBean advertBean) {
        Log.e(TAG, "弹出通知栏显示广告 showNotificationAlert()");
        //将图片链接转换为bitmap对象
        DownloadUtils.getBitmapByPIcUrl(advertBean.getAdvertPicUrl(), new OnGetBitmapByurlListener() {
            @Override
            public void onGetBitmapSuccess(Bitmap bitmap) {
                initNotification("我是广告", "我是广告", bitmap, advertBean);
            }

            @Override
            public void onGetBitmapFailed() {
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initNotification(String ticker, String title, Bitmap bitmap, AdvertBean advertBean) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.close);//设置图标
        builder.setWhen(System.currentTimeMillis());//设置通知时间
        Intent intent = new Intent("NotificationAdvertReceiver");
        intent.putExtra("apkUrl", advertBean.getAdvertApkDownloadUrl());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder.setDefaults(Notification.DEFAULT_LIGHTS);//设置指示灯
        builder.setDefaults(Notification.DEFAULT_SOUND);//设置提示声音
        builder.setDefaults(Notification.DEFAULT_VIBRATE);//设置震动
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.notification_advert_view);
        remoteView.setImageViewResource(R.id.image, R.drawable.close);
        remoteView.setImageViewBitmap(R.id.image, bitmap);
        remoteView.setTextViewText(R.id.title, ticker);
        remoteView.setTextViewText(R.id.text, title);
        notification.contentView = remoteView;
        manager.notify(2, notification);

    }
}
