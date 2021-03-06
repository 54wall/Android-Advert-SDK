package advert.sdk.com.advertlibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import advert.sdk.com.advertlibrary.R;
import advert.sdk.com.advertlibrary.constant.AdvertConstant;
import advert.sdk.com.advertlibrary.intf.OnDownloadListener;
import advert.sdk.com.advertlibrary.intf.UIProgressResponseListener;
import advert.sdk.com.advertlibrary.ui.SecondaryPresentation;
import advert.sdk.com.advertlibrary.ui.loopview.SecondaryPresentationLoopView;

import static android.content.Context.WINDOW_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * 管理ad
 */

public class ShowWindowAdvertUtils {

    private static WindowManager windowManager;
    private static View advertView;
    private static Context context;
    private static String apkUrl;
    private static SecondaryPresentation presentation;
    private static WindowManager.LayoutParams params;
    private static int advertTime;
    private static String TAG = ShowWindowAdvertUtils.class.getSimpleName();

    private static SecondaryPresentationLoopView presentationLoopView;

    /**
     * 点击关闭
     */
    static View.OnClickListener onclosedClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            remove();
        }
    };
    /**
     * 点击下载
     */
    static View.OnClickListener onDownlodClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            remove();
            DownloadUtils.download(apkUrl, new OnDownloadListener() {
                @Override
                public void onDownloadSuccess(File destFile) {
                    installApk(destFile, context);
                }

                @Override
                public void onDownloadFailed() {

                }
            }, new UIProgressResponseListener() {
                @Override
                public void onUIResponseProgress(long bytesRead, long contentLength, boolean done) {
                    if (contentLength != -1) {
                        //长度未知的情况下回返回-1
                        long ratio = (100 * bytesRead) / contentLength;
                        if (ratio % 5 == 0) {
                            DownloadUtils.showProgressnotifivation(context, (int) ratio);
                        }
                    }
                }
            });
        }
    };

    public static void initLoopView(SecondaryPresentationLoopView mPresentation, int advertType, int bannerLocation, String mapkUrl, String picUtils, int madvertTime, Context mcontext) {
        presentationLoopView = mPresentation;
//        presentationLoopView.setAd(R.drawable.rabbit);
        context = mcontext;
        apkUrl = mapkUrl;
        advertTime = madvertTime;
        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_TOAST);
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        /* 需要适配android版本 Unable to add window -- token null is not valid; is your activity running?
        https://blog.csdn.net/yanwenyuan0304/article/details/87868185*/
        if (Build.VERSION.SDK_INT > 25) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        if (advertType == AdvertConstant.INSERT_ADVERT_TYPE) {
            Log.e(TAG, "advertType == AdvertConstant.INSERT_ADVERT_TYPE 插屏广告");
            //params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.width = 480;
            params.height = 640;
//            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.CENTER;
        } else {
            Log.e(TAG, "非插屏广告");
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;//640
            params.gravity = bannerLocation == 1 ? Gravity.TOP : Gravity.BOTTOM;
        }

        if (windowManager != null && params != null) {
            remove();
        }
        windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        advertView = View.inflate(context, R.layout.dialog_advert_view, null);
        ImageView iv_dialog_advert = ((ImageView) advertView.findViewById(R.id.iv_dialog_advert));
        iv_dialog_advert.setOnClickListener(onDownlodClick);
        Picasso.with(context).load(picUtils).fit().into(iv_dialog_advert);
        advertView.findViewById(R.id.iv_dialog_closed).setOnClickListener(onclosedClick);
    }


    public static void init(SecondaryPresentation mPresentation, int advertType, int bannerLocation, String mapkUrl, String picUtils, int madvertTime, Context mcontext) {
        presentation = mPresentation;
        presentation.setAd(R.drawable.rabbit);
        context = mcontext;
        apkUrl = mapkUrl;
        advertTime = madvertTime;
        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_TOAST);
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        /* 需要适配android版本 Unable to add window -- token null is not valid; is your activity running?
        https://blog.csdn.net/yanwenyuan0304/article/details/87868185*/
        if (Build.VERSION.SDK_INT > 25) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        if (advertType == AdvertConstant.INSERT_ADVERT_TYPE) {
            Log.e(TAG, "advertType == AdvertConstant.INSERT_ADVERT_TYPE 插屏广告");
            //params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.width = 480;
            params.height = 640;
//            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.CENTER;
        } else {
            Log.e(TAG, "非插屏广告");
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;//640
            params.gravity = bannerLocation == 1 ? Gravity.TOP : Gravity.BOTTOM;
        }

        if (windowManager != null && params != null) {
            remove();
        }
        windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        advertView = View.inflate(context, R.layout.dialog_advert_view, null);
        ImageView iv_dialog_advert = ((ImageView) advertView.findViewById(R.id.iv_dialog_advert));
        iv_dialog_advert.setOnClickListener(onDownlodClick);
        Picasso.with(context).load(picUtils).fit().into(iv_dialog_advert);
        advertView.findViewById(R.id.iv_dialog_closed).setOnClickListener(onclosedClick);
    }

    /**
     * 显示广告 传入视图 和布局参数
     */
    public static void show() {
        Log.e(TAG, "show() 展示广告");
        windowManager.addView(advertView, params);
        presentation.show();
        //定时关闭
      /*  Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                remove();
            }
        }, advertTime);*/
    }

    /**
     * 显示广告 传入视图 和布局参数
     */
    public static void showLoopView() {
        Log.e(TAG, "show() 展示广告");
        windowManager.addView(advertView, params);
        presentationLoopView.show();
        //定时关闭
      /*  Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                remove();
            }
        }, advertTime);*/
    }
    /**
     * 关闭当前视图
     */
    public static void remove() {
        Log.e(TAG, "remove() 关闭当前视图");
        try {
            windowManager.removeView(advertView);
            presentation.dismiss();
            presentationLoopView.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 安装方法
    public static void installApk(File destFile, Context context) {

        Intent intent = new Intent();
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(destFile), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
