package com.zaihuishou.expandablerecycleradapter.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zaihuishou.expandablerecycleradapter.model.ExpandableListItem;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractAdapterItem;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractExpandableAdapterItem;
import com.zaihuishou.expandablerecycleradapter.viewholder.AdapterItemUtil;
import com.zaihuishou.expandablerecycleradapter.viewholder.BaseAdapterItem;

import java.util.ArrayList;
import java.util.List;

/**
 * this adapter is implementation of RecyclerView.Adapter
 * creater: zaihuishou
 * create time: 7/13/16.
 * author email:tanzhiqiang.cathy@gmail.com
 */
public abstract class BaseExpandableAdapter extends RecyclerView.Adapter implements AbstractExpandableAdapterItem.ParentListItemExpandCollapseListener {

    protected List<Object> mDataList;

    private Object mItemType;

    private AdapterItemUtil mUtil = new AdapterItemUtil();

    private ExpandCollapseListener mExpandCollapseListener;

    public void setExpandCollapseListener(ExpandCollapseListener expandCollapseListener) {
        mExpandCollapseListener = expandCollapseListener;
    }

    protected BaseExpandableAdapter(List data) {
        if (data == null) return;
        this.mDataList = data;
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    /**
     * @return data list
     */
    public List<?> getDataList() {
        return mDataList;
    }

    /**
     * notifyDataSetChanged
     *
     * @param data items
     */
    public void updateData(@NonNull List<Object> data) {
        if (data != null && !data.isEmpty()) {
            mDataList = data;
            notifyDataSetChanged();
        }
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
        if (o instanceof ExpandableListItem) {
            collapseParentListItem((ExpandableListItem) o, position, true);
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
        if (o instanceof ExpandableListItem) {
            expandParentListItem((ExpandableListItem) o, position, true);
        }
    }

    /**
     * @param expandableListItem               {@link ExpandableListItem}
     * @param parentIndex                      item index
     * @param collapseTriggeredByListItemClick
     */
    private void collapseParentListItem(ExpandableListItem expandableListItem, int parentIndex, boolean collapseTriggeredByListItemClick) {
        if (expandableListItem.isExpanded()) {
            expandableListItem.setExpanded(false);

            List<?> childItemList = expandableListItem.getChildItemList();
            if (childItemList != null) {
                int childListItemCount = childItemList.size();
                for (int i = childListItemCount - 1; i >= 0; i--) {
                    int index = parentIndex + i + 1;
                    Object o = mDataList.get(index);
                    if (o instanceof ExpandableListItem) {
                        ExpandableListItem parentListItem = (ExpandableListItem) o;
                        if (parentListItem.isExpanded()) {
                            collapseParentListItem(parentListItem, index, false);
                        }
                    }
                    mDataList.remove(index);
                }
                notifyItemRangeRemoved(parentIndex + 1, childListItemCount);
                expandableListItem.setExpanded(false);
                notifyItemRangeChanged(parentIndex + 1, mDataList.size() - parentIndex - 1);
            }

            if (collapseTriggeredByListItemClick && mExpandCollapseListener != null) {
                int expandedCountBeforePosition = getExpandedItemCount(parentIndex);
                mExpandCollapseListener.onListItemCollapsed(parentIndex - expandedCountBeforePosition);
            }
        }
    }

    /**
     * collaspe all item
     */
    public void collaspeAll() {
        if (mDataList != null && !mDataList.isEmpty()) {
            final int size = mDataList.size();
            ArrayList<Object> expandableListItems = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Object o = mDataList.get(i);
                if (o instanceof ExpandableListItem) {
                    ExpandableListItem expandableListItem = (ExpandableListItem) o;
                    if (expandableListItem.isExpanded()) {
                        expandableListItems.add(o);
                    }
                }
            }
            if (expandableListItems != null && !expandableListItems.isEmpty()) {
                final int expandedItemSize = expandableListItems.size();
                if (expandedItemSize > 0) {
                    for (int i = 0; i < expandedItemSize; i++) {
                        Object o = expandableListItems.get(i);
                        int indexOf = mDataList.indexOf(o);
                        if (indexOf >= 0)
                            collapseParentListItem((ExpandableListItem) o, indexOf, true);
                    }
                }
            }
        }
    }

    private void expandParentListItem(ExpandableListItem parentWrapper, int parentIndex, boolean expansionTriggeredByListItemClick) {
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
            if (!(listItem instanceof ExpandableListItem)) {
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

    @NonNull
    public abstract AbstractAdapterItem<Object> getItemView(Object type);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseAdapterItem(parent.getContext(), parent, getItemView(mItemType));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseAdapterItem rcvHolder = (BaseAdapterItem) holder;
        Object object = mDataList.get(position);
        if (object instanceof ExpandableListItem) {
            AbstractExpandableAdapterItem abstractParentAdapterItem = (AbstractExpandableAdapterItem) rcvHolder.getItem();
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
