package com.hz.lib.views;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;

import org.xutils.x;

/**
 * 轮播图适配器，一张图片
 * Created by LiuPeng on 16/9/21.
 */
public class BannerImageViewHolder implements Holder<String> {

    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, final int position, String data) {
        x.image().bind(imageView, data);
    }
}
