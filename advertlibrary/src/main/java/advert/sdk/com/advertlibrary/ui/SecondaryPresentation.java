package advert.sdk.com.advertlibrary.ui;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;


import advert.sdk.com.advertlibrary.R;

/**
 * 副屏
 */
public class SecondaryPresentation extends Presentation {

    private ImageView mIvAd;

    public SecondaryPresentation(Context context, Display display) {
        super(context, display);
    }

    public SecondaryPresentation(Context context, Display display, int theme) {
        super(context, display, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentation_secondary);
        mIvAd = findViewById(R.id.iv_ad);

    }

    public void setAd(int res) {
        mIvAd.setImageResource(res);
    }


}
