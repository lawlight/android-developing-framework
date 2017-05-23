package me.nereo.multi_image_selector.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.xutils.common.util.DensityUtil;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.ImageViewPagerActivity;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.nereo.multi_image_selector.R;


/**
 * 仿微信的图片选择器，扩展自me.nereo.multi_image_selector<br/>
 * 调用regitser方法注册view
 * 需要在使用此view所在的Activity中重写onActivityResult调用此类公共方法onActivityResult
 *
 */
public class ImageSelectorView extends RecyclerView {

    public static final int REQUEST_SELECT = 3000;
    public static final int REQUEST_PREVIEW = 4000;

    //最大图片选择数
    private int maxCount = 9;
    //是否显示相机
    private boolean showCamera = true;
    //是否多选
    private boolean multi = true;

    private Object source;
    private int tag;

    ImageAdapter adapter;
    //图片列表
    List<String> images = new ArrayList<>();

    public ImageSelectorView(Context context) {
        super(context);
        init();
    }

    public ImageSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        images.clear();
        this.setHasFixedSize(true);
        //计算每行可以显示多少个
        int width = DensityUtil.getScreenWidth();
        int itemWidth = DensityUtil.dip2px(68);
        FullyGridLayoutManager gridLayoutManager = new FullyGridLayoutManager(getContext(), width/itemWidth);
        this.setLayoutManager(gridLayoutManager);
        adapter = new ImageAdapter();
        this.setAdapter(adapter);
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }

    public boolean isMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    public List<String> getImages() {
        return images;
    }

    public ImageSelectorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 更新图片，在使用此view的Activity的onActivityResult方法中调用
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_SELECT + tag){
                // Get the result list of select image paths
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                //判断是否超出最大值？
                if(path.size() + images.size() > maxCount){
                    Toast.makeText(getContext(), "您最多可选择" + maxCount + "张图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                images.addAll(path);
                adapter.notifyDataSetChanged();
            }else if(requestCode == REQUEST_PREVIEW + tag){
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                images.clear();
                images.addAll(path);
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 注册view
     * @param source 传此view所在的Activity或Fragment
     * @param tag view的tag值，如果一个Activity或Fragment中有多个图片选择view，则tag值不能相同
     */
    public void register(Object source, int tag) {
        this.source = source;
        this.tag = tag;
    }

    class ImageAdapter extends RecyclerView.Adapter<ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            if(position == images.size()){
                if(images.size() >= maxCount){
                    holder.imageView.setVisibility(GONE);
                }else {
                    holder.imageView.setVisibility(View.VISIBLE);
                    holder.imageView.setImageResource(R.mipmap.ic_add);
                    holder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //显示图片选择Activity
                            Intent intent = new Intent(getContext(), MultiImageSelectorActivity.class);
                            // whether show camera
                            intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);
                            // max select image amount
                            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxCount - images.size());
                            // select mode
                            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
                                    multi ? MultiImageSelectorActivity.MODE_MULTI
                                            : MultiImageSelectorActivity.MODE_SINGLE);
                            if(source instanceof Activity){
                                ((Activity)source).startActivityForResult(intent, REQUEST_SELECT + tag);
                            }else if(source instanceof Fragment){
                                ((Fragment)source).startActivityForResult(intent, REQUEST_SELECT + tag);
                            }else{
                                throw new UnsupportedOperationException("请先调用register注册source和tag");
                            }

                        }
                    });
                }
            }else{
                holder.imageView.setVisibility(VISIBLE);
                //绑定图片
                x.image().bind(holder.imageView, images.get(position));
                //点击事件
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //显示图片轮播Activity，右上角有删除按钮
                        Intent intent = new Intent(getContext(), ImageViewPagerActivity.class);
                        intent.putExtra(ImageViewPagerActivity.EXTRA_SHOW_DELETE, true);
                        intent.putStringArrayListExtra("images", (ArrayList<String>) images);
                        intent.putExtra("index", position);
                        if(source instanceof Activity){
                            ((Activity)source).startActivityForResult(intent, REQUEST_PREVIEW + tag);
                        }else if(source instanceof Fragment){
                            ((Fragment)source).startActivityForResult(intent, REQUEST_PREVIEW + tag);
                        }else{
                            throw new UnsupportedOperationException("请先调用register注册source和tag");
                        }
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return images.size() + 1;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
