package com.zaihuishou.expandablerecycleradapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * 创建者: tantan(谭志强)
 * 创建时间: 7/13/16.
 * 作者邮箱:tanzhiqiang@todayoffice.cn
 */

public class RcvAdapterItem extends RecyclerView.ViewHolder {

    protected AbstractAdapterItem<Object> mItem;

    public RcvAdapterItem(Context context, ViewGroup parent, AbstractAdapterItem<Object> item) {
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
