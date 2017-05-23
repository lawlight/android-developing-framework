package com.hz.lib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 搜索栏，可本地保存历史记录
 * Created by LiuPeng on 16/9/21.
 */
public class SearchView extends EditText {
    public SearchView(Context context) {
        super(context);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
