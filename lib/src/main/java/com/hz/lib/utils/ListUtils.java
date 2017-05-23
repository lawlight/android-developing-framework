package com.hz.lib.utils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 列表工具类
 */
public class ListUtils<T> {

    /**
     * 删除列表中的重复项
     *
     * @param list 操作的列表
     * @return 操作完成后的列表，会返回list本身
     */
    public List<T> removeDuplicate(List<T> list) {
        Set set = new LinkedHashSet<T>();
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }
}
