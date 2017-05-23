package com.hz.lib.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

/**
 * Created by LiuPeng on 2017/5/11.
 */

public class ViewUtils {

    /**
     * 遍历布局，并禁用所有子控件
     *
     * @param viewGroup
     *            布局对象
     */
    public static void disableSubControls(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                if (v instanceof Spinner) {
                    Spinner spinner = (Spinner) v;
                    spinner.setClickable(false);
                    spinner.setEnabled(false);
                } else if (v instanceof ListView) {
                    ((ListView) v).setClickable(false);
                    ((ListView) v).setEnabled(false);
                } else {
                    disableSubControls((ViewGroup) v);
                }
            } else if (v instanceof EditText) {
                ((EditText) v).setEnabled(false);
                ((EditText) v).setClickable(false);
            } else if (v instanceof Button) {
                ((Button) v).setEnabled(false);
            }
        }
    }
}
