package hz.com.androidlib.listitem.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hz.com.androidlib.R;
import hz.com.androidlib.listitem.ItemEntity;
import hz.com.androidlib.listitem.adapter.OrderAdapter;


/**
 * @Created by lenovo on 2016/9/19.
 * @class description 抢单界面
 */
public class GrabOrderActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private OrderAdapter mAdapter;
    private List<ItemEntity> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sublist_layout);
        setTitle("抢单列表");
        init();
    }

    private void init() {
        // 初始化RececlerView
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recycler_view);
        mDatas = new ArrayList<ItemEntity>();

        // 添加测试数据
        for (int i = 0; i < 100; i++) {
            ItemEntity entity = new ItemEntity();
            // 编号
            if (i % 5 == 0) {
                entity.setCodeId("301280342338129423");
            } else if (i % 3 == 0) {
                entity.setCodeId("3014432134241556631");
            } else {
                entity.setCodeId("310029033292884932");
            }

            // 名称
            if (i % 5 == 0) {
                entity.setName("中华老字号蚊香九里沙升级版加厚版无敌驱蚊婴幼儿专用");
            } else if (i % 3 == 0) {
                entity.setName("泰国进口香蕉芝麻香蕉薄皮保存长久新鲜国庆巨献");
            } else {
                entity.setName("鲜香美食 无公害瓜果 补充微量元素 保持健康");
            }

            // 价格
            if (i % 4 == 0) {
                entity.setPrice("293元");
            } else if (i % 7 == 0) {
                entity.setPrice("100元");
            } else {
                entity.setPrice("368元");
            }

            // 商品数量
            if (i % 3 == 0) {
                entity.setGoodsCount("12元");
            } else if (i % 6 == 0) {
                entity.setGoodsCount("9元");
            } else {
                entity.setGoodsCount("10元");
            }

            // 日期
            if (i % 3 == 0) {
                entity.setTime("2016年9月19日");
            } else if (i % 6 == 0) {
                entity.setTime("2016年6月21日");
            } else {
                entity.setTime("2016年5月13日");
            }

            // 地址
            if (i % 3 == 0) {
                entity.setAddress("山东省青岛市市北区青岛市延安三路67号");
            } else if (i % 4 == 0) {
                entity.setAddress("山东省青岛市市北区登州路56-1号");
            } else {
                entity.setAddress("山东省青岛市李沧区九水东路铜川路77号");
            }

            mDatas.add(entity);
        }

        mAdapter = new OrderAdapter(this, mDatas);
        //设置Adapter
        mRecyclerView.setAdapter(mAdapter);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
