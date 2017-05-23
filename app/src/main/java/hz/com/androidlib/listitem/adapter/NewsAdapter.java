package hz.com.androidlib.listitem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hz.com.androidlib.R;
import hz.com.androidlib.listitem.ItemEntity;


/**
 * @Created by lenovo on 2016/9/19.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ItemViewHolder> {

    private List<ItemEntity> mDatas;
    private LayoutInflater mInflater;
    private Context mContext;
//    private Typeface mTypeFaceLight;
    private int windowWidth;

    public NewsAdapter(Context context, List<ItemEntity> mDatas) {
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
        mContext = context;
//        mTypeFaceLight = Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");
        // 获取屏幕宽高
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowWidth = wm.getDefaultDisplay().getWidth();

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int i) {


        ViewGroup.LayoutParams picParams = itemViewHolder.newsPic.getLayoutParams();
        picParams.width = windowWidth / 3;
        picParams.height = picParams.width / 4 * 3;
        itemViewHolder.newsPic.setLayoutParams(picParams);

        if (i % 3 == 0) {
            itemViewHolder.newsPic.setImageResource(R.mipmap.image_5);
        } else if (i % 4 == 0) {
            itemViewHolder.newsPic.setImageResource(R.mipmap.image_6);
        } else if (i % 5 == 0) {
            itemViewHolder.newsPic.setImageResource(R.mipmap.image_7);
        } else {
            itemViewHolder.newsPic.setImageResource(R.mipmap.image_8);
        }

        itemViewHolder.newsName.setText(mDatas.get(i).getName() + "");
        itemViewHolder.newsDiscuss.setText(mDatas.get(i).getPrice() + "");

//        itemViewHolder.moreBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, "第" + i + "项 弹出更多窗口", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        /**
         * 使用RecyclerView，ViewHolder是可以复用的。这和使用ListView的VIewHolder复用是一样的
         * ViewHolder创建的个数好像是可见item的个数+3
         */
        ItemViewHolder holder = new ItemViewHolder(mInflater.inflate(
                R.layout.news_list_item, viewGroup, false));
        return holder;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView newsName;
        private ImageView newsPic;
        private TextView newsDiscuss;


        public ItemViewHolder(View itemView) {
            super(itemView);
            newsName = (TextView) itemView.findViewById(R.id.tv_news_name);
            newsPic = (ImageView) itemView.findViewById(R.id.iv_news_pic);
            newsDiscuss = (TextView) itemView.findViewById(R.id.tv_discuss_count);
        }

    }
}
