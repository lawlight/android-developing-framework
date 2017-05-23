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
import hz.com.androidlib.listitem.adapter.NewsAdapter;


/**
 * @Created by lenovo on 2016/9/19.
 * @class description 新闻列表
 */
public class NewsListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private List<ItemEntity> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sublist_layout);
        setTitle("新闻列表");
        init();
    }

    private void init() {
        // 初始化RececlerView
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recycler_view);
        mDatas = new ArrayList<ItemEntity>();

        // 添加测试数据
        for (int i = 0; i < 30; i++) {
            ItemEntity entity = new ItemEntity();

            // 名称
            if (i % 5 == 0) {
                entity.setName("乔良：中国应向美学做大国\n随时做好开战准备");
            } else if (i % 3 == 0) {
                entity.setName("超八成日本人“对中国无好感”\n但越来越多精英去中国");
            } else {
                entity.setName("日本战犯辩护律师回忆60年前沈阳审判：他们跪在那磕头谢罪");
            }

            // 价格
            if (i % 4 == 0) {
                entity.setPrice("293评");
            } else if (i % 7 == 0) {
                entity.setPrice("100评");
            } else {
                entity.setPrice("368评");
            }

            mDatas.add(entity);
        }

        mAdapter = new NewsAdapter(this, mDatas);
        //设置Adapter
        mRecyclerView.setAdapter(mAdapter);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
