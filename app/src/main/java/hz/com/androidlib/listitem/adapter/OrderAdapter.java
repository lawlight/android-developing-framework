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
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ItemViewHolder> {

    private List<ItemEntity> mDatas;
    private LayoutInflater mInflater;
    private Context mContext;
    private Typeface mTypeFaceLight;

    public OrderAdapter(Context context, List<ItemEntity> mDatas) {
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


        itemViewHolder.orderCode.setText(mDatas.get(i).getCodeId());
//        itemViewHolder.orderName.setTypeface(mTypeFaceLight);
        itemViewHolder.orderName.setText(mDatas.get(i).getName());
        itemViewHolder.orderPrice.setText(mDatas.get(i).getPrice());
        itemViewHolder.orderCount.setText(mDatas.get(i).getGoodsCount());
        itemViewHolder.orderTime.setText(mDatas.get(i).getTime());
        itemViewHolder.orderAddress.setText(mDatas.get(i).getAddress());
        itemViewHolder.grabPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "第" + i + "项 抢单成功", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        /**
         * 使用RecyclerView，ViewHolder是可以复用的。这和使用ListView的VIewHolder复用是一样的
         * ViewHolder创建的个数好像是可见item的个数+3
         */
        ItemViewHolder holder = new ItemViewHolder(mInflater.inflate(
                R.layout.grab_order_list_item, viewGroup, false));
        return holder;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView grabPic;
        private TextView orderName;
        private TextView orderCode;
        private TextView orderPrice;
        private TextView orderCount;
        private TextView orderTime;
        private TextView orderAddress;

        public ItemViewHolder(View itemView) {
            super(itemView);
            orderCode = (TextView) itemView.findViewById(R.id.tv_order_number);
            orderName = (TextView) itemView.findViewById(R.id.tv_order_name);
            orderPrice = (TextView) itemView.findViewById(R.id.tv_order_account);
            orderCount = (TextView) itemView.findViewById(R.id.tv_order_deliver_account);
            orderTime = (TextView) itemView.findViewById(R.id.tv_order_deliver_time);
            orderAddress = (TextView) itemView.findViewById(R.id.tv_order_deliver_address);
            grabPic = (ImageView) itemView.findViewById(R.id.iv_grab_btn);
        }

    }
}
