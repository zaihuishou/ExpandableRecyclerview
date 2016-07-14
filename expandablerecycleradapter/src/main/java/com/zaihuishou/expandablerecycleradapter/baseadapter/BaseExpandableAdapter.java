package com.zaihuishou.expandablerecycleradapter.baseadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zaihuishou.expandablerecycleradapter.model.ParentListItem;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractAdapterItem;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractParentAdapterItem;
import com.zaihuishou.expandablerecycleradapter.viewholder.AdapterItemUtil;
import com.zaihuishou.expandablerecycleradapter.viewholder.RcvAdapterItem;

import java.util.List;

public abstract class BaseExpandableAdapter extends RecyclerView.Adapter implements AbstractParentAdapterItem.ParentListItemExpandCollapseListener {

    protected List<Object> mDataList;

    private Object mItemType;

    private AdapterItemUtil mUtil = new AdapterItemUtil();

//    private List<RecyclerView> mAttachedRecyclerViewPool;

    private ExpandCollapseListener mExpandCollapseListener;

    public void setExpandCollapseListener(ExpandCollapseListener expandCollapseListener) {
        mExpandCollapseListener = expandCollapseListener;
    }

    protected BaseExpandableAdapter(List data) {
        if (data == null) return;
        this.mDataList = data;
//        mAttachedRecyclerViewPool = new ArrayList<>();
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
     *
     * @param data items
     */
    public void updateData(@NonNull List<Object> data) {
        mDataList = data;
        notifyDataSetChanged();
    }

    /**
     * add an item
     *
     * @param position intem index
     * @param o        item
     */
    public void addItem(int position, Object o) {
        if (isDataListNotEmpty() && position >= 0) {
            mDataList.add(position, o);
            notifyItemInserted(position);
        }
    }

    /**
     * add an item
     *
     * @param o item object
     */
    public void addItem(Object o) {
        if (isDataListNotEmpty()) {
            mDataList.add(o);
            int size = mDataList.size();
            notifyItemInserted(size - 1);
        }
    }

    /**
     * add items
     *
     * @param position index
     * @param objects  list objects
     */
    public void addRangeItem(int position, List<Object> objects) {
        if (isDataListNotEmpty() && position <= mDataList.size() && position >= 0) {
            mDataList.addAll(position, objects);
            notifyItemRangeInserted(position, position + objects.size());
        }
    }

    /**
     * modify an exit item
     *
     * @param position index
     * @param newObj   the new object
     */
    public void modifyItem(int position, Object newObj) {
        if (isDataListNotEmpty() && position < mDataList.size() && position >= 0) {
            mDataList.set(position, newObj);
            notifyItemChanged(position);
        }
    }

    /**
     * remove item
     *
     * @param position index
     */
    public void removedItem(int position) {
        if (isDataListNotEmpty() && position < mDataList.size() && position >= 0) {
            mDataList.remove(position);
            notifyItemRemoved(position);
        }

    }

    private boolean isDataListNotEmpty() {
        return mDataList != null && !mDataList.isEmpty();
    }


    @Override
    public void onParentListItemCollapsed(int position) {
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
                    int index = parentIndex + i + 1;
                    Object o = mDataList.get(index);
                    if (o instanceof ParentListItem) {
                        ParentListItem parentListItem = (ParentListItem) o;
                        if (parentListItem.isExpanded()) {
                            collapseParentListItem(parentListItem, index, false);
                        }
                    }
                    mDataList.remove(index);
                }
                notifyItemRangeRemoved(parentIndex + 1, childListItemCount);
                parentWrapper.setExpanded(false);
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
            List<?> childItemList = parentWrapper.getChildItemList();
            if (childItemList != null && !childItemList.isEmpty()) {
                parentWrapper.setExpanded(true);
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
     *
     * @param position item index
     * @return item view type
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

//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//        mAttachedRecyclerViewPool.add(recyclerView);
//    }
//
//    @Override
//    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
//        super.onDetachedFromRecyclerView(recyclerView);
//        mAttachedRecyclerViewPool.remove(recyclerView);
//    }

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
            AbstractParentAdapterItem abstractParentAdapterItem = (AbstractParentAdapterItem) rcvHolder.getItem();
            abstractParentAdapterItem.setParentListItemExpandCollapseListener(this);
        }
        (rcvHolder).getItem().onUpdateViews(mDataList.get(position), position);
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
