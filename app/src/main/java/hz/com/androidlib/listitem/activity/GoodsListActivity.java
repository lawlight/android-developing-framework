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
import hz.com.androidlib.listitem.adapter.GoodsAdapter;


/**
 * @Created by xiaotong on 2016/9/19.
 * @class description 商品列表界面
 */
public class GoodsListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private GoodsAdapter mAdapter;
    private List<ItemEntity> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sublist_layout);
        setTitle("淘宝商品列表");
        init();
    }

    private void init() {
        // 初始化RececlerView
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recycler_view);
        mDatas = new ArrayList<ItemEntity>();

        // 添加测试数据
        for (int i = 0; i < 100; i++) {
            ItemEntity entity = new ItemEntity();
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
                entity.setPrice("293");
            } else if (i % 7 == 0) {
                entity.setPrice("100");
            } else {
                entity.setPrice("368");
            }

            // 商品数量
            if (i % 3 == 0) {
                entity.setGoodsCount("2398");
            } else if (i % 6 == 0) {
                entity.setGoodsCount("1241");
            } else {
                entity.setGoodsCount("456");
            }
            mDatas.add(entity);
        }

        mAdapter = new GoodsAdapter(this, mDatas);
        //设置Adapter
        mRecyclerView.setAdapter(mAdapter);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
