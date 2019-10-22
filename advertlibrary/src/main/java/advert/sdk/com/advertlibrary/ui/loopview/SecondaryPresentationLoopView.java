package advert.sdk.com.advertlibrary.ui.loopview;

import android.app.Presentation;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import advert.sdk.com.advertlibrary.R;

/**
 * 副屏
 */
public class SecondaryPresentationLoopView extends Presentation {

    boolean isRunning = false;
    private ViewPager viewPager;  //轮播图模块
    private int[] mImg;
    private int[] mImg_id;
    private String[] mDec;
    private ArrayList<ImageView> mImgList;
    private LinearLayout ll_dots_container;
    private TextView loop_dec;
    private int previousSelectedPosition = 0;
    private Context context;

    public SecondaryPresentationLoopView(Context context, Display display) {
        super(context, display);
        this.context = context;
    }

    public SecondaryPresentationLoopView(Context context, Display display, int theme) {
        super(context, display, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentation_loop_view);
        initLoopView();  //实现轮播图

    }

    private void initLoopView() {
        viewPager = (ViewPager) findViewById(R.id.loopviewpager);
        ll_dots_container = (LinearLayout) findViewById(R.id.ll_dots_loop);
        loop_dec = (TextView) findViewById(R.id.loop_dec);

        // 图片资源id数组
        mImg = new int[]{
                R.drawable.food_0,
                R.drawable.food_1,
                R.drawable.food_2,
                R.drawable.food_3,
                R.drawable.food_4
        };

        // 文本描述
        mDec = new String[]{
                "Test1",
                "Test2",
                "Test3",
                "Test4",
                "Test5"
        };

        mImg_id = new int[]{
                R.id.pager_img1,
                R.id.pager_img2,
                R.id.pager_img3,
                R.id.pager_img4,
                R.id.pager_img5
        };

        // 初始化要展示的5个ImageView
        mImgList = new ArrayList<ImageView>();
        ImageView imageView;
        View dotView;
        LinearLayout.LayoutParams layoutParams;
        for (int i = 0; i < mImg.length; i++) {
            //初始化要显示的图片对象
            imageView = new ImageView(context);
            imageView.setBackgroundResource(mImg[i]);
            imageView.setId(mImg_id[i]);
            mImgList.add(imageView);
            //加引导点
            dotView = new View(context);
            dotView.setBackgroundResource(R.drawable.dot);
            layoutParams = new LinearLayout.LayoutParams(10, 10);
            if (i != 0) {
                layoutParams.leftMargin = 10;
            }
            //设置默认所有都不可用
            dotView.setEnabled(false);
            ll_dots_container.addView(dotView, layoutParams);
        }

        ll_dots_container.getChildAt(0).setEnabled(true);
        loop_dec.setText(mDec[0]);
        previousSelectedPosition = 0;
        //设置适配器
        viewPager.setAdapter(new LoopViewAdapter(mImgList));
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) % mImgList.size();
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        viewPager.setCurrentItem(currentPosition);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                int newPosition = i % mImgList.size();
                loop_dec.setText(mDec[newPosition]);
                ll_dots_container.getChildAt(previousSelectedPosition).setEnabled(false);
                ll_dots_container.getChildAt(newPosition).setEnabled(true);
                previousSelectedPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        // 开启轮询
        new Thread() {
            public void run() {
                isRunning = true;
                while (isRunning) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //下一条，在Activity中可以使用
//                    context.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
//                        }
//                    });
//                    viewPager.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
//                        }
//                    });
                    loop_dec.post(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        }
                    });
                }
            }
        }.start();

    }
}
