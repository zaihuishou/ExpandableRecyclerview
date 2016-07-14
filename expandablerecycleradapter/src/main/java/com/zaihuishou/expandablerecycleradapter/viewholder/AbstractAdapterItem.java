package com.zaihuishou.expandablerecycleradapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * adapter(can not expand item) item must implments this class
 *
 * creater: zaihuishou
 * create time: 7/13/16.
 * author email:tanzhiqiang.cathy@gmail.com
 */
public abstract class AbstractAdapterItem<T extends Object> {

    /**
     * @return item`s layoutId
     */
    @LayoutRes
    public abstract int getLayoutResId();

    /**
     * init views
     *
     * @param root item root view
     */
    public abstract void onBindViews(final View root);

    /**
     * refresh view state
     */
    public abstract void onSetViews();

    /**
     * set data to view
     *
     * @param model    model
     * @param position item index
     */
    public abstract void onUpdateViews(T model, int position);

}