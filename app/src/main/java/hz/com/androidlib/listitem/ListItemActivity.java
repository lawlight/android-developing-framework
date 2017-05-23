package hz.com.androidlib.listitem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hz.com.androidlib.R;
import hz.com.androidlib.listitem.activity.GoodsListActivity;
import hz.com.androidlib.listitem.activity.GrabOrderActivity;
import hz.com.androidlib.listitem.activity.NewsListActivity;
import hz.com.androidlib.listitem.activity.WaterFallActivity;

/**
 * @Created by lenovo on 2016/9/21.
 * @class description 各种列表项
 */
public class ListItemActivity extends AppCompatActivity {


    @Bind(R.id.rl_item_item0)
    RelativeLayout item0Btn;

    @Bind(R.id.rl_item_item1)
    RelativeLayout item1Btn;

    @Bind(R.id.rl_item_item2)
    RelativeLayout item2Btn;

    @Bind(R.id.rl_item_item3)
    RelativeLayout item3Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_item_demo);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rl_item_item0)
    public void Item0Btn() {
        Intent i = new Intent(ListItemActivity.this, GoodsListActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.rl_item_item1)
    public void Item1Btn() {
        Intent i = new Intent(ListItemActivity.this, GrabOrderActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.rl_item_item2)
    public void Item2Btn() {
        Intent i = new Intent(ListItemActivity.this, NewsListActivity.class);
        startActivity(i);
    }


    @OnClick(R.id.rl_item_item3)
    public void Item3Btn() {
        Intent i = new Intent(ListItemActivity.this, WaterFallActivity.class);
        startActivity(i);
    }
}
