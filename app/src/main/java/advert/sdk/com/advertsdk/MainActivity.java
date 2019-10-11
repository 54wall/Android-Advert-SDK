package advert.sdk.com.advertsdk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import advert.sdk.com.advertlibrary.utils.PermissionUtils;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private final static String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.VIBRATE", "android.permission.INTERNET"};
    private final static int REQUEST_CODE_PERMISSIONS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions(MainActivity.this);
        //允许出现在其他应用上 https://blog.csdn.net/u014203484/article/details/78233980
//        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//        intent.setData(Uri.parse("package:" + getPackageName()));
//        startActivityForResult(intent, 100);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "不影响点击", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void requestPermissions(Context mContext) {
        PermissionUtils.checkAndRequestMorePermissions(mContext, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS,
                new PermissionUtils.PermissionRequestSuccessCallBack() {
                    @Override
                    public void onHasPermission() {
                        // 权限已被授予
                        Log.e(TAG, "权限已被授予");

                    }
                });
    }
}
