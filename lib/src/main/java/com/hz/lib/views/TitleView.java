package com.hz.lib.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hz.lib.R;


/**
 * 通用的标题栏，可设置左右按钮、标题内容
 * 默认左按钮显示后退、有按钮隐藏
 * Created by LiuPeng on 2016/5/23.
 */
public class TitleView extends LinearLayout {

    Context mContext;

    ImageButton ibtnLeft;
    ImageButton ibtnRight;
    TextView tvTitle;

    //属性值
    private String titleText;
    private boolean isShowLeftButton;
    private boolean isShowRightButton;

    public TitleView(Context context) {
        super(context);
        initView(context);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获取自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleView);
        titleText = a.getString(R.styleable.TitleView_titleText);
        if (titleText == null) {
            titleText = getResources().getString(R.string.app_name);
        }

        isShowLeftButton = a.getBoolean(R.styleable.TitleView_isShowLeftButton, true);
        isShowRightButton = a.getBoolean(R.styleable.TitleView_isShowRightButton, false);

        initView(context);
    }

    /**
     * 初始化布局
     *
     * @param context
     */
    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_title, this, false);
        addView(view);
        ibtnLeft = (ImageButton) view.findViewById(R.id.ibtn_left);
        ibtnRight = (ImageButton) view.findViewById(R.id.ibtn_right);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);

        //左侧返回键
        if (isShowLeftButton) {
            ibtnLeft.setVisibility(View.VISIBLE);
            //默认的左按钮点击事件，finish当前Activity
            ibtnLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext instanceof Activity) {
                        ((Activity) mContext).finish();
                    }
                }
            });
        } else {
            ibtnLeft.setVisibility(View.INVISIBLE);
        }
        //右侧按键
        ibtnRight.setVisibility(isShowRightButton ? VISIBLE : INVISIBLE);
        //标题
        tvTitle.setText(titleText);
    }

    public ImageButton getLeftImageButton() {
        return ibtnLeft;
    }

    public ImageButton getRightImageButton() {
        return ibtnRight;
    }

    public TextView getTitleTextView() {
        return tvTitle;
    }
}
