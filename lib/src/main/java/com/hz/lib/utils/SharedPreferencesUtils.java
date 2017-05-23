package com.hz.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Field;

/**
 * SharedPreferences 工具类
 * 可使用getObject和setObject方法来快速设置对bean类的保存和获取
 */
public class SharedPreferencesUtils {

    private static SharedPreferencesUtils mSharedUtils;
    private Context context;

    public static SharedPreferencesUtils getInstance(Context context){
        if(mSharedUtils == null){
            mSharedUtils = new SharedPreferencesUtils();
        }
        mSharedUtils.context = context;
        return mSharedUtils;
    }

    /**
     * 获取对象实例
     * @param clazz 对象的类
     * @return
     */
    public <T> T getObject(Class<T> clazz){
        SharedPreferences sp = context.getSharedPreferences(clazz.getName(), Activity.MODE_PRIVATE);
        T object = null;
        try {
            object = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for(int i = 0;i<fields.length; i++){
                Field field = fields[i];
                field.setAccessible(true);
                String fieldName = field.getName();
                String sharedKey = clazz.getName()+"_"+fieldName;
                Class dataType = field.getType();
                if( dataType== int.class || dataType == Integer.class){
                    field.set(object, sp.getInt(sharedKey, 0));
                }else if(dataType == long.class || dataType == Long.class){
                    field.set(object, sp.getLong(sharedKey, 0));
                }else if(dataType == float.class ||dataType == Float.class){
                    field.set(object, sp.getFloat(sharedKey, 0));
                }else if(dataType == double.class || dataType == Double.class){
                    field.set(object, (double) sp.getFloat(sharedKey, 0));
                }else if (dataType == boolean.class || dataType == Boolean.class) {
                    field.set(object, sp.getBoolean(sharedKey, false));
                }else{
                    field.set(object, sp.getString(sharedKey, null));
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 设置对象
     * @param object 类实例
     */
    public void setObject(Object object){
        Class clazz = object.getClass();
        SharedPreferences sp = context.getSharedPreferences(clazz.getName(), Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for(int i = 0; i < fields.length; i++){
                Field field = fields[i];
                field.setAccessible(true);
                Class dataType = field.getType();
                String fieldName = field.getName();
                String sharedKey = clazz.getName()+"_"+fieldName;
                Object value = field.get(object);
                if(dataType== int.class || dataType == Integer.class){
                    editor.putInt(sharedKey, Integer.parseInt(value.toString()));
                }else if(dataType == long.class || dataType == Long.class){
                    editor.putLong(sharedKey, Long.parseLong(value.toString()));
                }else if(dataType == float.class ||dataType == Float.class
                        ||dataType == double.class || dataType == Double.class){
                    editor.putFloat(sharedKey, Float.parseFloat(value.toString()));
                }else if (dataType == boolean.class || dataType == Boolean.class) {
                    editor.putBoolean(sharedKey, Boolean.parseBoolean(value.toString()));
                }else{
                    editor.putString(sharedKey, value.toString());
                }
            }
            editor.commit();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除对象
     * @param clazz
     */
    public void clearObject(Class clazz){
        SharedPreferences sp = context.getSharedPreferences(clazz.getName(), Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();
    }
}
