package hz.com.androidlib.listitem.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import hz.com.androidlib.R;
import hz.com.androidlib.listitem.ItemEntity;


/**
 * @Created by lenovo on 2016/9/19.
 */
public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ItemViewHolder> {

    private List<ItemEntity> mDatas;
    private LayoutInflater mInflater;
    private Context mContext;
    private Typeface mTypeFaceLight;

    public GoodsAdapter(Context context, List<ItemEntity> mDatas) {
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
        mContext = context;
//        mTypeFaceLight = Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int i) {

        if (i % 3 == 0) {
            itemViewHolder.goodsPic.setImageResource(R.mipmap.image_5);
        } else if (i % 4 == 0) {
            itemViewHolder.goodsPic.setImageResource(R.mipmap.image_6);
        } else if (i % 5 == 0) {
            itemViewHolder.goodsPic.setImageResource(R.mipmap.image_7);
        } else {
            itemViewHolder.goodsPic.setImageResource(R.mipmap.image_8);
        }

        itemViewHolder.goodsName.setText(mDatas.get(i).getName());
//        itemViewHolder.goodsName.setTypeface(mTypeFaceLight);
        itemViewHolder.goodsPrice.setText(mDatas.get(i).getPrice());
        itemViewHolder.goodsCount.setText(mDatas.get(i).getGoodsCount() + "人付款");
        itemViewHolder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "第" + i + "项 弹出更多窗口", Toast.LENGTH_SHORT).show();
            }
        });

        if (i == 0) {
            itemViewHolder.line.setVisibility(View.INVISIBLE);
        } else {
            itemViewHolder.line.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        /**
         * 使用RecyclerView，ViewHolder是可以复用的。这和使用ListView的VIewHolder复用是一样的
         * ViewHolder创建的个数好像是可见item的个数+3
         */
        ItemViewHolder holder = new ItemViewHolder(mInflater.inflate(
                R.layout.goods_list_item, viewGroup, false));
        return holder;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView goodsName;
        private ImageView goodsPic;
        private TextView goodsPrice;
        private TextView goodsCount;
        private ImageView moreBtn;
        private View line;

        public ItemViewHolder(View itemView) {
            super(itemView);
            goodsName = (TextView) itemView.findViewById(R.id.tv_goods_name);
            goodsPic = (ImageView) itemView.findViewById(R.id.iv_goods_pic);
            goodsPrice = (TextView) itemView.findViewById(R.id.tv_goods_price);
            goodsCount = (TextView) itemView.findViewById(R.id.tv_goods_sold_count);
            moreBtn = (ImageView) itemView.findViewById(R.id.iv_more);
            line = (View) itemView.findViewById(R.id.divide_line);


        }

    }
}
