package com.zaihuishou.expandablerecycleradapter.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zaihuishou.expandablerecycleradapter.Model.ParentListItem;
import com.zaihuishou.expandablerecycleradapter.ViewHolder.AbstractAdapterItem;
import com.zaihuishou.expandablerecycleradapter.ViewHolder.AbstractParentAdapterItem;
import com.zaihuishou.expandablerecycleradapter.ViewHolder.AbstractParentAdapterItem.ParentListItemExpandCollapseListener;
import com.zaihuishou.expandablerecycleradapter.ViewHolder.AdapterItemUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRcvAdapter extends RecyclerView.Adapter implements ParentListItemExpandCollapseListener {

    protected List<Object> mDataList;

    private Object mItemType;

    private AdapterItemUtil mUtil = new AdapterItemUtil();

    private List<RecyclerView> mAttachedRecyclerViewPool;

    protected BaseRcvAdapter(List data) {
        this.mDataList = data;
        mAttachedRecyclerViewPool = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public List<Object> getDataList() {
        return mDataList;
    }

    /**
     * 可以被复写用于单条刷新等
     */
    public void updateData(@NonNull List<Object> data) {
        mDataList = data;
        notifyDataSetChanged();
    }

    public void add(int position, Object t) {
        mDataList.add(position, t);
        notifyItemInserted(position);
    }

    public void add(int position, List<Object> ts) {
        mDataList.addAll(position, ts);
        notifyItemRangeInserted(position, position + ts.size());
    }

    public void remove(int position) {
        if (position < mDataList.size()) {
            mDataList.remove(position);
        }
        notifyItemRemoved(position);
    }

    @Override
    public void onParentListItemCollapsed(int position) {
        Log.i("BaseRcvAdapter", "onParentListItemCollapsed position:" + position);
    }

    @Override
    public void onParentListItemExpanded(int position) {
        Log.i("BaseRcvAdapter", "onParentListItemExpanded position:" + position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * instead by{@link #getItemViewType(Object)}
     */
    @Deprecated
    @Override
    public int getItemViewType(int position) {
        mItemType = getItemViewType(mDataList.get(position));
        return mUtil.getIntType(mItemType);
    }

    public Object getItemViewType(Object t) {
        return -1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mAttachedRecyclerViewPool.add(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mAttachedRecyclerViewPool.remove(recyclerView);
    }

    @NonNull
    public abstract AbstractAdapterItem<Object> getItemView(Object type);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RcvAdapterItem(parent.getContext(), parent, getItemView(mItemType));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RcvAdapterItem rcvHolder = (RcvAdapterItem) holder;
        Object object = mDataList.get(position);
        if (object instanceof ParentListItem) {
            AbstractAdapterItem<Object> item = rcvHolder.getItem();
            if (item instanceof AbstractParentAdapterItem) {
                AbstractParentAdapterItem abstractParentAdapterItem = (AbstractParentAdapterItem) item;
                abstractParentAdapterItem.setParentListItemExpandCollapseListener(this);
            }
        }
        (rcvHolder).getItem().onUpdateViews(object, position);
    }


    private class RcvAdapterItem extends RecyclerView.ViewHolder {

        protected AbstractAdapterItem<Object> mItem;

        protected RcvAdapterItem(Context context, ViewGroup parent, AbstractAdapterItem<Object> item) {
            super(LayoutInflater.from(context).inflate(item.getLayoutResId(), parent, false));
            itemView.setClickable(true);
            mItem = item;
            mItem.onBindViews(itemView);
            mItem.onSetViews();
        }

        protected AbstractAdapterItem<Object> getItem() {
            return mItem;
        }

    }


}
