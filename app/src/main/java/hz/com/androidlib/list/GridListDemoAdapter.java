package hz.com.androidlib.list;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hz.com.androidlib.R;

/**
 * Created by LiuPeng on 16/9/22.
 */
public class GridListDemoAdapter extends RecyclerView.Adapter<GridListDemoAdapter.ViewHolder> {

    List<ListDemoBean> list;


    public GridListDemoAdapter(List<ListDemoBean> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_demo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListDemoBean item = list.get(position);
        holder.tvText1.setText(item.text1);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ListTargetActivity.class);
                intent.putExtra("text", item.text1);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_image)
        ImageView ivImage;
        @Bind(R.id.tv_text1)
        TextView tvText1;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}