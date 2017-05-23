package hz.com.androidlib.permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hz.com.androidlib.R;

/**
 * @Created by zxt on 2016/9/21.
 * @class description 各类权限请求与判断
 */
public class PermissionDemoActivity extends AppCompatActivity {


    @Bind(R.id.rl_item0)
    RelativeLayout item0Btn;

    @Bind(R.id.rl_item1)
    RelativeLayout item1Btn;

    @Bind(R.id.rl_item2)
    RelativeLayout item2Btn;


    private static final int REQUEST_SYS_CAMERA = 0x100;
    private static final int REQUEST_PERMISSION_ACCESS_COURSE_LOCATION = 0x101;

    private LocationManager lm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_permission_demo);
        ButterKnife.bind(this);
        setTitle("权限判断与操作引导Demo");

        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }


    @OnClick(R.id.rl_item0)
    public void Item0Btn() {
        // 跳转到应用详情
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            i.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            i.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            i.setAction(Intent.ACTION_VIEW);
            i.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            i.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(i);
    }

    @OnClick(R.id.rl_item1)
    public void Item1Btn() {
        if (CommonUtils.cameraIsUseable()) {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i, REQUEST_SYS_CAMERA);
        } else {
            CommonUtils.showGoToSettingDialog(PermissionDemoActivity.this, null);
        }
    }

    @OnClick(R.id.rl_item2)
    public void Item2Btn() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSION_ACCESS_COURSE_LOCATION);
        } else {
            getLocation();
        }
    }


    /**
     * 获取经纬度
     */
    private void getLocation() {
        // targetSdkVersion 23时，捕获异常
        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1,
                    new DemoLocationListener());

            Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Toast.makeText(PermissionDemoActivity.this, "定位权限获取成功\n纬度Latitude: " + loc.getLatitude()
                    + "\n经度Longitude: " + loc.getLongitude(), Toast.LENGTH_LONG).show();

        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_ACCESS_COURSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    getLocation();
                } else {
                    // Permission Denied
                    CommonUtils.showGoToSettingDialog(PermissionDemoActivity.this, "定位权限获取失败");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private class DemoLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Toast.makeText(PermissionDemoActivity.this, location.getLatitude() +
                    ":" + location.getLongitude(), Toast.LENGTH_LONG).show();

            System.out.println(location.getLatitude() + ":" + location.getLongitude());

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

    }
}
