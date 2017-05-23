package hz.com.androidlib.listitem.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import hz.com.androidlib.R;
import hz.com.androidlib.listitem.MultiColumnListView;


/**
 * @Created by lenovo on 2016/9/19.
 * @class description 瀑布流listView
 */
public class WaterFallActivity extends AppCompatActivity {

    private PLAAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fall_layout);

        init();
    }


    private void init() {
        MultiColumnListView listView = (MultiColumnListView) findViewById(R.id.list);
        mAdapter = new PLAAdapter(this);

        String a = "记者从市海洋与渔业局了解到，青岛市崂山湾女儿岛南部海域，将建设一座人工鱼礁199.7918公顷，其中人工鱼礁群11块，透水构筑物总用海面积16.0800公顷，投放人工鱼礁10.9246万空方。";
        String b = "“公交车司机的话中话”——“快上来，快上来，不要堵在车门口。”当司机这么喊的时候，你该警觉了。";
        String c = "如果你来青岛，它的好尽在不言中。青岛，中国最具幸福感的城市，只一句话，便已道尽了一切美好。有着优美的海滨，空旷的崂山与弥漫着异域风情的教堂，却没有太多的人群。青岛，它几乎集合了幸福感需要拥有的所有条件，从头到尾都弥漫着一股浓浓的幸福滋味。幸运的青岛人，住在一个集美景与现代化于一体的城市里，享受着小城市的慢节奏，却有着大城市的高质量生活。";

        for (int i = 0; i < 30; ++i) {
//            StringBuilder builder = new StringBuilder();
//            for (int j = mAdapter.getCount(), max = (i * 1234) % 500; j < max; j++)
//                builder.append(i).append(' ');
//            mAdapter.add(builder.toString());
            if (i % 2 == 0) {
                mAdapter.add(a);
            } else if (i % 3 == 0) {
                mAdapter.add(b);
            } else {
                mAdapter.add(c);
            }
        }

        listView.setAdapter(mAdapter);
    }

    private static class PLAAdapter extends ArrayAdapter<String> {
        public PLAAdapter(Context context) {
            super(context, R.layout.fall_item, android.R.id.text1);
        }
    }


}
