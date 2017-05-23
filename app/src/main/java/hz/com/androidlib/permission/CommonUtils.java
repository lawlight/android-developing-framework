package hz.com.androidlib.permission;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * @Created by lenovo on 2016/9/20.
 */
public class CommonUtils {

    /**
     * 返回true 表示可以使用  返回false表示不可以使用
     */
    public static boolean cameraIsUseable() {
        boolean isUseable = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isUseable = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isUseable;
            }
        }
        return isUseable;
    }


    /**
     * 显示缺失权限提示
     */
    public static void showGoToSettingDialog(final Context c, final String hint) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。\n\n请点击\"设置\"-\"权限\"-打开所需权限。\n\n最后点击两次后退按钮，即可返回。");
        // 拒绝, 退出应用
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (hint != null) {
                    Toast.makeText(c, hint, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CommonUtils.startAppSettings(c);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public static void startAppSettings(Context c) {
        // 跳转到应用详情
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            i.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            i.setData(Uri.fromParts("package", c.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            i.setAction(Intent.ACTION_VIEW);
            i.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            i.putExtra("com.android.settings.ApplicationPkgName", c.getPackageName());
        }
        c.startActivity(i);
    }

}
