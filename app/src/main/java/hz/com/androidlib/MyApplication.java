package hz.com.androidlib;

import android.app.Application;

import com.umeng.socialize.PlatformConfig;

import org.xutils.x;

/**
 * 应用Application
 * Created by LiuPeng on 16/6/12.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);

        //友盟社会化账号注册
        //微信 appid appsecret
        PlatformConfig.setWeixin("wxd189aa50bf799ad4", "de866ef3707cdf6926a4c18a8b741cd2");
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo("135679294", "2df1e40111eaf9e575de0af5e0f55514", "http://www.sina.com");
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone("1105615322", "Yqds9jhDUuzuvYfL");


        // 注册Crash接口-蒲公英
//        PgyCrashManager.register(this);
        // 取消注册
//        PgyCrashManager.unregister();

        // 注册Update接口-蒲公英
//        PgyUpdateManager.register(this);
//        PgyUpdateManager.unregister();

    }
}
