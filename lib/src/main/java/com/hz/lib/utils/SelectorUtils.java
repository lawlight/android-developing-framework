package com.hz.lib.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * selector drawable 工具类,可改变select的颜色
 * Created by LiuPeng on 16/9/23.
 */

public class SelectorUtils {
    /**
     * 动态修改selector中图片的背景颜色
     *
     * @param drawable
     *            selectorDrawable,格式参考seletor_pressed.xml
     * @param rgbColors
     *            默认以及按下状态的颜色值
     */
    public static void changeViewColor(StateListDrawable drawable, int[] rgbColors) {
        Drawable.ConstantState cs = drawable.getConstantState();
        if (rgbColors.length < 2) {
            return;
        }
        try {
            // 通过反射调用getChildren方法获取xml文件中写的drawable数组
            Method method = cs.getClass().getMethod("getChildren");
            method.setAccessible(true);
            Object obj = method.invoke(cs);
            Drawable[] drawables = (Drawable[]) obj;

            for (int i = 0; i < drawables.length; i++) {
                // 接下来我们要通过遍历的方式对每个drawable对象进行修改颜色值
                GradientDrawable gd = (GradientDrawable) drawables[i];
                if (gd == null) {
                    break;
                }
                if (i == 0) {
                    // 我们对按下的状态做浅色处理
                    gd.setColor(rgbColors[0]);
                } else {
                    // 对默认状态做深色处理
                    gd.setColor(rgbColors[1]);
                }
            }
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
