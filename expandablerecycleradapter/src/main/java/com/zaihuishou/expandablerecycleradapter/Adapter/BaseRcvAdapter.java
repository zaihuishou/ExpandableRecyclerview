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

    private ExpandCollapseListener mExpandCollapseListener;

    public void setExpandCollapseListener(ExpandCollapseListener expandCollapseListener) {
        mExpandCollapseListener = expandCollapseListener;
    }

    protected BaseRcvAdapter(List data) {
        if (data == null) return;
        this.mDataList = data;
        Log.i("BaseRcvAdapter", "data size:" + mDataList.size());
        mAttachedRecyclerViewPool = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public List<?> getDataList() {
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
        Object o = mDataList.get(position);
        if (o instanceof ParentListItem) {
            collapseParentListItem((ParentListItem) o, position, true);
        }
    }

    /**
     * expand parent item
     *
     * @param position The index of the item in the list being expanded
     */
    @Override
    public void onParentListItemExpanded(int position) {
        Log.i("BaseRcvAdapter", "onParentListItemExpanded position:" + position);
        Object o = mDataList.get(position);
        if (o instanceof ParentListItem) {
            expandParentListItem((ParentListItem) o, position, true);
        }
    }

    private void collapseParentListItem(ParentListItem parentWrapper, int parentIndex, boolean collapseTriggeredByListItemClick) {
        if (parentWrapper.isExpanded()) {
            parentWrapper.setExpanded(false);

            List<?> childItemList = parentWrapper.getChildItemList();
            if (childItemList != null) {
                int childListItemCount = childItemList.size();
                for (int i = childListItemCount - 1; i >= 0; i--) {
                    mDataList.remove(parentIndex + i + 1);
                }
                notifyItemRangeRemoved(parentIndex + 1, childListItemCount);
                notifyItemRangeChanged(parentIndex + 1, mDataList.size() - parentIndex - 1);
            }

            if (collapseTriggeredByListItemClick && mExpandCollapseListener != null) {
                int expandedCountBeforePosition = getExpandedItemCount(parentIndex);
                mExpandCollapseListener.onListItemCollapsed(parentIndex - expandedCountBeforePosition);
            }
        }
    }

    private void expandParentListItem(ParentListItem parentWrapper, int parentIndex, boolean expansionTriggeredByListItemClick) {
        if (!parentWrapper.isExpanded()) {
            parentWrapper.setExpanded(true);
            List<?> childItemList = parentWrapper.getChildItemList();
            if (childItemList != null && !childItemList.isEmpty()) {
                int childListItemCount = childItemList.size();
                for (int i = 0; i < childListItemCount; i++) {
                    mDataList.add(parentIndex + i + 1, childItemList.get(i));
                }
                notifyItemRangeInserted(parentIndex + 1, childListItemCount);
                int positionStart = parentIndex + childListItemCount;
                if (parentIndex != mDataList.size() - 1)
                    notifyItemRangeChanged(positionStart, mDataList.size() - positionStart);
            }
            if (expansionTriggeredByListItemClick && mExpandCollapseListener != null) {
                int expandedCountBeforePosition = getExpandedItemCount(parentIndex);
                mExpandCollapseListener.onListItemExpanded(parentIndex - expandedCountBeforePosition);
            }
        }
    }

    /**
     * Gets the number of expanded child list items before the specified position.
     *
     * @param position The index before which to return the number of expanded
     *                 child list items
     * @return The number of expanded child list items before the specified position
     */
    private int getExpandedItemCount(int position) {
        if (position == 0) {
            return 0;
        }

        int expandedCount = 0;
        for (int i = 0; i < position; i++) {
            Object listItem = getListItem(i);
            if (!(listItem instanceof ParentListItem)) {
                expandedCount++;
            }
        }
        return expandedCount;
    }

    /**
     * Gets the list item held at the specified adapter position.
     *
     * @param position The index of the list item to return
     * @return The list item at the specified position
     */
    protected Object getListItem(int position) {
        boolean indexInRange = position >= 0 && position < mDataList.size();
        if (indexInRange) {
            return mDataList.get(position);
        } else {
            return null;
        }
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
//        Log.i("BaseRcvAdapter", "onCreateViewHolder viewType:" + viewType);
        return new RcvAdapterItem(parent.getContext(), parent, getItemView(mItemType));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        Log.i("BaseRcvAdapter", "onBindViewHolder position:" + position);
        RcvAdapterItem rcvHolder = (RcvAdapterItem) holder;
        Object object = mDataList.get(position);
        if (object instanceof ParentListItem) {
            AbstractAdapterItem<Object> item = rcvHolder.getItem();
            if (item instanceof AbstractParentAdapterItem) {
                AbstractParentAdapterItem abstractParentAdapterItem = (AbstractParentAdapterItem) item;
                abstractParentAdapterItem.setParentListItemExpandCollapseListener(this);
            }
        }
        (rcvHolder).getItem().onUpdateViews(mDataList.get(position), position);
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

    public interface ExpandCollapseListener {

        /**
         * Called when a list item is expanded.
         *
         * @param position The index of the item in the list being expanded
         */
        void onListItemExpanded(int position);

        /**
         * Called when a list item is collapsed.
         *
         * @param position The index of the item in the list being collapsed
         */
        void onListItemCollapsed(int position);
    }
}
