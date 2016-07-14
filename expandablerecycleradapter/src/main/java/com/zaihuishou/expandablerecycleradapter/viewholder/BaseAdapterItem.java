package com.zaihuishou.expandablerecycleradapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * creater: zaihuishou
 * create time: 7/13/16.
 * email:tanzhiqiang.cathy@gmail.com
 */

public class BaseAdapterItem extends RecyclerView.ViewHolder {

    protected AbstractAdapterItem<Object> mItem;

    public BaseAdapterItem(Context context, ViewGroup parent, AbstractAdapterItem<Object> item) {
        super(LayoutInflater.from(context).inflate(item.getLayoutResId(), parent, false));
        itemView.setClickable(true);
        mItem = item;
        mItem.onBindViews(itemView);
        mItem.onSetViews();
    }

    public AbstractAdapterItem<Object> getItem() {
        return mItem;
    }
}
