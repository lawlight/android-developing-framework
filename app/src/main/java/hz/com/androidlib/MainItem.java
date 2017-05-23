package hz.com.androidlib;

import android.content.Intent;

/**
 * 首页item
 * Created by LiuPeng on 16/9/7.
 */
public class MainItem {
    public String title1;
    public String title2;
    public Intent intent;

    public MainItem(String title1, String title2, Intent intent) {
        this.title1 = title1;
        this.title2 = title2;
        this.intent = intent;
    }
}
