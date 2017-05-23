package me.nereo.multi_image_selector.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片浏览ViewPager，可设置是否支持手势缩放
 */
public class ImageViewPager extends ViewPager {

    private List<String> images;
    private List<View> views = new ArrayList<>();
    private ImageAdapter adapter = new ImageAdapter();

    public ImageViewPager(Context context) {
        super(context);
        init();
    }

    public ImageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        this.setAdapter(adapter);
    }

    public void setImages(List<String> images){
        this.images = images;
        //刷新显示
        views.clear();
        for(int i = 0 ; i < images.size(); i++){
            ImageView iv = new ImageView(getContext());
            views.add(iv);
        }
        adapter.notifyDataSetChanged();
    }

    public List<String> getImages() {
        return images;
    }

    public void remove(int position){
        images.remove(position);
        views.remove(position);
        adapter.notifyDataSetChanged();
    }

    class ImageAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView((View) object);
        }
        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            view.addView(views.get(position));
            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setSize(DensityUtil.getScreenWidth(),DensityUtil.getScreenHeight())
                    .setRadius(DensityUtil.dip2px(5))
                    .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
                    .build();
            x.image().bind((ImageView) views.get(position), images.get(position), imageOptions);
            return views.get(position);
        }
    }
}
