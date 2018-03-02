package hz.com.androidlib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hz.com.androidlib.bluetooth.BLEActivity;
import hz.com.androidlib.contact.ContactActivity;
import hz.com.androidlib.dialog.DialogDemoActivity;
import hz.com.androidlib.gesture.GestureDemoActivity;
import hz.com.androidlib.guide.GuideActivity;
import hz.com.androidlib.hellocharts.ColumnChartActivity;
import hz.com.androidlib.hellocharts.LineChartActivity;
import hz.com.androidlib.index.ShopIndexDemoActivity;
import hz.com.androidlib.index.TabIndexDemoActivity;
import hz.com.androidlib.list.ListDemoActivity;
import hz.com.androidlib.listitem.ListItemActivity;
import hz.com.androidlib.mpcharts.MPColumnActivity;
import hz.com.androidlib.mpcharts.MPLineActivity;
import hz.com.androidlib.permission.PermissionDemoActivity;
import hz.com.androidlib.record.RecordDemoActivity;
import hz.com.androidlib.utils.UtilsDemoActivity;

/**
 * DEMO项目
 */
public class MainActivity extends AppCompatActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //权限申请
//        PermissionGen.with(this)
//                .addRequestCode(100)
//                .permissions(
//                        android.Manifest.permission.CAMERA)
//                .request();

        List<MainItem> list = new ArrayList<>();
        list.add(new MainItem("商城首页组件模板", "包括轮播图、秒杀倒计时、动态文字等的使用", new Intent(this, ShopIndexDemoActivity.class)));
        list.add(new MainItem("底部按钮首页模板", "由4个底部按钮进行fragment切换的首页模板", new Intent(this, TabIndexDemoActivity.class)));
        list.add(new MainItem("引导页demo", "应用第一次启动时可用的引导页demo", new Intent(this, GuideActivity.class)));
        list.add(new MainItem("分页列表与网络访问demo", "分页列表demo", new Intent(this, ListDemoActivity.class)));
        list.add(new MainItem("多图选择demo", "多图选择、图片列表、拍照的demo", new Intent(this, ImageActivity.class)));
        //list.add(new MainItem("二维码demo", "二维码扫描与生成demo", new Intent(this, QRMainActivity.class)));
        list.add(new MainItem("工具类demo", "工具类的使用演示demo", new Intent(this, UtilsDemoActivity.class)));
        list.add(new MainItem("日历demo", "农历日历demo", new Intent(this, CalenderActivity.class)));
        list.add(new MainItem("音频录制demo", "录制mp3音频的demo", new Intent(this, RecordDemoActivity.class)));

        // 3.0新增
        list.add(new MainItem("权限请求、判断", "优化用户操作引导的提示", new Intent(this, PermissionDemoActivity.class)));
        list.add(new MainItem("自定义Dialog", "各类弹出框样式与实现方法", new Intent(this, DialogDemoActivity.class)));
        list.add(new MainItem("通讯录字母排序", "带搜索框", new Intent(this, ContactActivity.class)));
        list.add(new MainItem("各种常用列表项", "淘宝、腾讯新闻、抢单等列表样式", new Intent(this, ListItemActivity.class)));
        list.add(new MainItem("手势集合Demo", "第一个例子：列表项向左滑展示更多按钮", new Intent(this, GestureDemoActivity.class)));

        list.add(new MainItem("蓝牙", "蓝牙通信", new Intent(this, BLEActivity.class)));

        list.add(new MainItem("MPChart", "折线图", new Intent(this, MPLineActivity.class)));
        list.add(new MainItem("MPChart", "柱状图", new Intent(this, MPColumnActivity.class)));

        list.add(new MainItem("Hellocharts", "折线图", new Intent(this, LineChartActivity.class)));
        list.add(new MainItem("Hellocharts", "柱状图", new Intent(this, ColumnChartActivity.class)));

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MainAdapter(list));
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 摇一摇用户反馈-配置代码
         * */
        // 自定义摇一摇的灵敏度，默认为950，数值越小灵敏度越高。
//        PgyFeedbackShakeManager.setShakingThreshold(1000);
        // 以对话框的形式弹出
//        PgyFeedbackShakeManager.register(MainActivity.this);
        // 以Activity的形式打开，这种情况下必须在AndroidManifest.xml配置FeedbackActivity
        // 打开沉浸式,默认为false
        // FeedbackActivity.setBarImmersive(true);
//        PgyFeedbackShakeManager.register(MainActivity.this, false);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        PgyFeedbackShakeManager.unregister();
    }


    /**
     * 权限申请回调
     */

//    @PermissionSuccess(requestCode = 100)
//    public void doSomething(){
//        Intent intent2 = new Intent(this, ScanActivity.class);
//        this.startActivityForResult(intent2, 111);
//    }
//
//    @PermissionFail(requestCode = 100)
//    public void doFailSomething(){
//        Toast.makeText(this, "打开相机失败，请设置app拍照权限", Toast.LENGTH_SHORT).show();
//    }
}
