package com.zaihuishou.expandablerecycleradapter.viewholder;

import android.util.SparseArray;

/**
 * creater: zaihuishou
 * create time: 7/13/16.
 * email:tanzhiqiang.cathy@gmail.com
 */

public class AdapterItemUtil {

    private SparseArray<Object> typeSArr = new SparseArray<>();

    /**
     * @param type item的类型
     * @return 通过object类型的type来得到int类型的type
     */
    public int getIntType(Object type) {
        int index = typeSArr.indexOfValue(type);
        if (index == -1) {
            index = typeSArr.size();
            // 如果没用这个type，就存入这个type
            typeSArr.put(index, type);
        }
        return index;
    }

}
