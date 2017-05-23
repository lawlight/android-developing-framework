package com.hz.lib.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.hz.lib.R;

import java.util.List;

/**
 * 热点新闻文字滚动栏
 * Created by LiuPeng on 16/9/23.
 */
public class TextSwitchView extends FrameLayout implements TextSwitcher.ViewFactory{

    public interface OnTextClickListener{
        void onClick(int position, String text);
    }

    public interface OnTextSwitchListener{
        void onSwitch(int position, String text);
    }

    public void setOnTextSwitchListener(OnTextSwitchListener onTextSwitchListener) {
        this.onTextSwitchListener = onTextSwitchListener;
    }

    OnTextClickListener onTextClickListener;
    OnTextSwitchListener onTextSwitchListener;

    /**
     * 设置点击监听
     * @param onTextClickListener
     */
    public void setOnTextClickListener(OnTextClickListener onTextClickListener) {
        this.onTextClickListener = onTextClickListener;
    }

    TextSwitcher switcher;
    private List<String> texts;

    //记录当前位置
    private int position = -1;

    //是否已开启
    private boolean start = false;

    public int getPosition() {
        return position;
    }

    private int delay = 5000;

    /**
     * 切换延时，默认5000毫秒
     * @param delay 延时（毫秒）
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    public TextSwitchView(Context context) {
        super(context);
        initView(context);
    }

    public TextSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.view_text_switch, null);
        addView(view);
        switcher = (TextSwitcher) view.findViewById(R.id.textSwitcher);
        switcher.setFactory(this);
        switcher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onTextClickListener != null && position > -1 && texts != null){
                    onTextClickListener.onClick(position, texts.get(position));
                }
            }
        });
    }

    /**
     * 设置文本
     * @param texts
     */
    public void setTexts(List<String> texts){
        this.texts = texts;
        position = -1;
    }

    public List<String> getTexts() {
        return texts;
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(start){
                position = (position + 1) % texts.size();
                switcher.setText(texts.get(position));
                if(onTextSwitchListener != null){
                    onTextSwitchListener.onSwitch(position, texts.get(position));
                }
                handler.postDelayed(runnable, delay);
            }
        }
    };

    /**
     * 进出动画，默认为从下向上滚动
     */
    public void setAnimationType(Animation in, Animation out){
        switcher.setInAnimation(in);
        switcher.setOutAnimation(out);
    }

    @Override
    public View makeView() {
        TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.view_text_switch_textview, null);
        return tv;
    }

    public void startSwitch(){
        if(!start){
            handler.post(runnable);
            start = true;
        }
    }

    public void stopSwitch(){
        start = false;
    }
}
