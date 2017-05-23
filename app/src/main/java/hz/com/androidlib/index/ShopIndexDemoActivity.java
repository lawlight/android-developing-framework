package hz.com.androidlib.index;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.hz.lib.utils.DialogUtils;
import com.hz.lib.views.BannerImageViewHolder;
import com.hz.lib.views.CountdownView;
import com.hz.lib.views.TextSwitchView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hz.com.androidlib.R;

/**
 * 商城综合首页demo
 */
public class ShopIndexDemoActivity extends AppCompatActivity {

    @Bind(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @Bind(R.id.countdownView1)
    CountdownView countdownView1;
    @Bind(R.id.countdownView2)
    CountdownView countdownView2;
    @Bind(R.id.countdownView3)
    CountdownView countdownView3;
    @Bind(R.id.textSwitchView)
    TextSwitchView textSwitchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_index_demo);
        ButterKnife.bind(this);

        final List<String> images = new ArrayList<>();
        images.add("http://img5.imgtn.bdimg.com/it/u=3322081817,934507632&fm=21&gp=0.jpg");
        images.add("http://img1.imgtn.bdimg.com/it/u=3979657601,2834339442&fm=21&gp=0.jpg");
        images.add("http://img1.imgtn.bdimg.com/it/u=1960926965,3700824252&fm=21&gp=0.jpg");

        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        convenientBanner.setPages(new CBViewHolderCreator<BannerImageViewHolder>() {
            @Override
            public BannerImageViewHolder createHolder() {
                return new BannerImageViewHolder();
            }
        }, images);
        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
        //.setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
        //设置指示器的方向
        //.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
        //设置翻页的效果，不需要翻页效果可用不设
        //.setPageTransformer(Transformer.DefaultTransformer);    //集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
        //convenientBanner.setManualPageable(false);//设置不能手动影响
        //自动轮播时间
        convenientBanner.startTurning(5000);

        Calendar target1 = Calendar.getInstance();
        target1.add(Calendar.SECOND, 5);
        Calendar target2 = Calendar.getInstance();
        target2.add(Calendar.HOUR, 1);
        Calendar target3 = Calendar.getInstance();
        target3.add(Calendar.HOUR, 200);

        countdownView1.setTargetTime(target1);
        countdownView2.setTargetTime(target2);
        countdownView3.setTargetTime(target3);

        countdownView1.setOnCountdownFinishListener(new CountdownView.OnCountdownFinishListener() {
            @Override
            public void onFinish() {
                DialogUtils.getInstance(ShopIndexDemoActivity.this).showShortToast("倒计时完成~~~");
            }
        });

        countdownView2.setShowDays(false);
        countdownView2.setTextColor(Color.parseColor("#333333"));
        countdownView2.setTextSize(30);
        countdownView2.setTextBackgroundResource(R.drawable.bg_calendar);

        countdownView1.startCountDown();
        countdownView2.startCountDown();
        countdownView3.startCountDown();

        //新闻头条滚动
        List<String> texts = new ArrayList<>();
        texts.add("说到微信小程序，你可能不知道这些内幕");
        texts.add("雅虎宣布信息泄露 全球影响5亿账户");
        texts.add("成都80后成最具国际影响力中国科学家");
        texts.add("懒癌患者福音，耐克这款鞋子会自动系鞋带");
        texts.add("米5清仓再降价！小米5S跑分力压乐Pro3");
        texts.add("生逢其时,可惜是一次奢侈的体验");
        texts.add("百度实习生离职一年后被内部开除");

        textSwitchView.setDelay(5000);
        textSwitchView.setOnTextClickListener(new TextSwitchView.OnTextClickListener() {
            @Override
            public void onClick(int position, String text) {
                DialogUtils.getInstance(ShopIndexDemoActivity.this).showShortToast("点击了第"+position+"项，内容为"+text);
            }
        });

        textSwitchView.setOnTextSwitchListener(new TextSwitchView.OnTextSwitchListener() {
            @Override
            public void onSwitch(int position, String text) {
                DialogUtils.getInstance(ShopIndexDemoActivity.this).showShortToast("切换到第"+position+"项，内容为"+text);
            }
        });

        textSwitchView.setTexts(texts);

        textSwitchView.startSwitch();

    }

    @Override
    protected void onPause() {
        super.onPause();
        textSwitchView.stopSwitch();
    }
}
