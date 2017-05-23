package com.hz.lib.base;

import java.io.Serializable;

/**
 * 键值对的bean
 */
public class KV implements Serializable{
    public String key;
    public Object value;
    public boolean isChecked;

    public KV(String key, Object value){
        this.key = key;
        this.value = value;
        this.isChecked = false;
    }

    /**
     * 获取值的字符串
     * @return
     */
    public String getValueString(){
        return value == null ? null : value.toString();
    }
}
