package com.zaihuishou.expandablerecycleradapter.adapter;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zaihuishou.expandablerecycleradapter.model.ExpandableListItem;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractAdapterItem;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractExpandableAdapterItem;
import com.zaihuishou.expandablerecycleradapter.viewholder.AdapterItemUtil;
import com.zaihuishou.expandablerecycleradapter.viewholder.BaseAdapterItem;

import java.util.ArrayList;
import java.util.Iterator;
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

    private List<RecyclerView> mRecyclerViewList;

    public void setExpandCollapseListener(ExpandCollapseListener expandCollapseListener) {
        mExpandCollapseListener = expandCollapseListener;
    }

    protected BaseExpandableAdapter(List data) {
        if (data == null) return;
        this.mDataList = data;
        checkDefaultExpand();
        mRecyclerViewList = new ArrayList<>();
    }

    /**
     * check has item is expanded by default
     */
    private void checkDefaultExpand() {
        ArrayMap<Object, List<?>> childArrayMap = new ArrayMap<>();
        Iterator<Object> iterator = mDataList.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            if (next instanceof ExpandableListItem) {
                ExpandableListItem expandableListItem = (ExpandableListItem) next;
                if (expandableListItem.isExpanded()) {
                    List<?> childItemList = expandableListItem.getChildItemList();
                    if (childItemList != null && !childItemList.isEmpty()) {
                        childArrayMap.put(next, childItemList);
                    }
                }
            }
        }
        int size = childArrayMap.size();
        if (size == 0) return;
        for (int i = 0; i < size; i++) {
            Object o = childArrayMap.keyAt(i);
            List<?> objects = childArrayMap.valueAt(i);
            int indexOf = mDataList.indexOf(o);
            mDataList.addAll(indexOf + 1, objects);
        }

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
            checkDefaultExpand();
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
        try {
            Object o = mDataList.get(position);
            if (o instanceof ExpandableListItem) {
                expandParentListItem((ExpandableListItem) o, position, true, false);
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }

    /**
     * @param expandableListItem               {@link ExpandableListItem}
     * @param parentIndex                      item index
     * @param collapseTriggeredByListItemClick
     */
    private void collapseParentListItem(ExpandableListItem expandableListItem, int parentIndex, boolean collapseTriggeredByListItemClick) {
        if (expandableListItem.isExpanded()) {
            List<?> childItemList = expandableListItem.getChildItemList();
            if (childItemList != null && !childItemList.isEmpty()) {
                notifyItemExpandedOrCollapsed(parentIndex, false);
                int childListItemCount = childItemList.size();
                for (int i = childListItemCount - 1; i >= 0; i--) {
                    int index = parentIndex + i + 1;
                    Object o = mDataList.get(index);
                    if (o instanceof ExpandableListItem) {
                        ExpandableListItem parentListItem;
                        try {
                            parentListItem = (ExpandableListItem) o;
                            collapseParentListItem(parentListItem, index, false);
                        } catch (Exception e) {
                            e.printStackTrace();
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
     * notify item state changed
     *
     * @param parentIndex
     * @param isExpand
     */
    private void notifyItemExpandedOrCollapsed(int parentIndex, boolean isExpand) {
        if (mRecyclerViewList != null && !mRecyclerViewList.isEmpty()) {
            RecyclerView recyclerView = mRecyclerViewList.get(0);
            BaseAdapterItem viewHolderForAdapterPosition = (BaseAdapterItem) recyclerView.findViewHolderForAdapterPosition(parentIndex);
            try {

                AbstractAdapterItem<Object> item = viewHolderForAdapterPosition.getItem();
                if (item != null && item instanceof AbstractExpandableAdapterItem) {
                    AbstractExpandableAdapterItem abstractExpandableAdapterItem = (AbstractExpandableAdapterItem) item;
                    abstractExpandableAdapterItem.onExpansionToggled(isExpand);
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * Collapses all parents in the list.
     */
    public void collapseAllParents() {
        if (mDataList != null && !mDataList.isEmpty()) {
            ArrayList<Object> expandableListItems = getParents(true);
            if (expandableListItems != null && !expandableListItems.isEmpty()) {
                final int expandedItemSize = expandableListItems.size();
                if (expandedItemSize > 0) {
                    for (int i = 0; i < expandedItemSize; i++) {
                        Object o = expandableListItems.get(i);
                        int indexOf = mDataList.indexOf(o);
                        if (indexOf >= 0)
                            collapseParentListItem((ExpandableListItem) o, indexOf, false);
                    }
                }
            }
        }
    }

    /**
     * @param isExpanded
     * @return return all parents
     */
    @NonNull
    private ArrayList<Object> getParents(boolean isExpanded) {
        final int size = mDataList.size();
        ArrayList<Object> expandableListItems = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Object o = mDataList.get(i);
            if (o instanceof ExpandableListItem) {
                ExpandableListItem expandableListItem = (ExpandableListItem) o;
                if (isExpanded) {
                    if (expandableListItem.isExpanded())
                        expandableListItems.add(o);
                } else {
                    if (!expandableListItem.isExpanded())
                        expandableListItems.add(o);
                }
            }
        }
        return expandableListItems;
    }

    /**
     * expand index item (Notice: only fixed to just adjust triple level, not available for higher level!!!!!!!)
     *
     * @param expandableListItem
     * @param parentIndex                       The index of the parent to collapse
     * @param expansionTriggeredByListItemClick
     */
    protected void expandParentListItem(ExpandableListItem expandableListItem, int parentIndex, boolean expansionTriggeredByListItemClick, boolean isExpandAllChildren) {
        if (!expandableListItem.isExpanded()) {
            List<?> childItemList = expandableListItem.getChildItemList();
            if (childItemList != null && !childItemList.isEmpty()) {
                expandableListItem.setExpanded(true);
                int childListItemCount = childItemList.size();
                for (int i = 0; i < childListItemCount; i++) {
                    Object o = childItemList.get(i);
                    int newIndex = parentIndex + i + 1;
                    if (isExpandAllChildren && i > 0) {
                        for(int j = 0; j < i; j++){
                            Object childBefore = childItemList.get(j);
                            if(childBefore instanceof ExpandableListItem){
                                newIndex += ((ExpandableListItem) childBefore).getChildItemList().size();
                            }
                        }
                    }
                    mDataList.add(newIndex, o);
                    notifyItemInserted(newIndex);
                    if (isExpandAllChildren)
                        if (o instanceof ExpandableListItem) {
//                            notifyItemInserted(newIndex);
//                            if (parentIndex != mDataList.size() - 1)
//                                notifyItemRangeChanged(parentIndex + 1, mDataList.size() - parentIndex - 1);
                            expandParentListItem((ExpandableListItem) o, newIndex, expansionTriggeredByListItemClick, isExpandAllChildren);
                        }
                }
//                notifyItemRangeInserted(parentIndex + 1, childListItemCount);
                int positionStart = parentIndex + childListItemCount;
                if (parentIndex != mDataList.size() - 1)
                    notifyItemRangeChanged(positionStart, mDataList.size() - positionStart);

//                notifyItemExpandedOrCollapsed(parentIndex, true);
            }
            if (expansionTriggeredByListItemClick && mExpandCollapseListener != null) {
                int expandedCountBeforePosition = getExpandedItemCount(parentIndex);
                mExpandCollapseListener.onListItemExpanded(parentIndex - expandedCountBeforePosition);
            }
        }
    }

    /**
     * expand specified parent item
     *
     * @param parentIndex The index of the parent to expand
     */
    public void expandParent(int parentIndex) {
        if (mDataList != null && !mDataList.isEmpty() && parentIndex >= 0 && parentIndex < mDataList.size()) {
            Object o = mDataList.get(parentIndex);
            if (o instanceof ExpandableListItem) {
                expandParentListItem((ExpandableListItem) o, parentIndex, false, false);
            }
        }
    }

    /**
     * expand all parents item
     */
    public void expandAllParents() {
        ArrayList<Object> expandableListItems = getParents(false);
        if (expandableListItems != null && !expandableListItems.isEmpty()) {
            final int expandedItemSize = expandableListItems.size();
            if (expandedItemSize > 0) {
                for (int i = 0; i < expandedItemSize; i++) {
                    Object o = expandableListItems.get(i);
                    int indexOf = mDataList.indexOf(o);
                    if (indexOf >= 0)
                        expandParentListItem((ExpandableListItem) o, indexOf, false, true);
                }
            }
        }
    }

//    private void expandOrCollaspeParents(boolean isExpand) {
//
//    }

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

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerViewList.add(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecyclerViewList.remove(recyclerView);
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
