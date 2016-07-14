package com.zaihuishou.expandablerecycleradapter.model;

import java.util.List;

/**
 * Interface for implementing required methods in a parent list item.
 * creater: zaihuishou
 * create time: 7/13/16.
 * author email:tanzhiqiang.cathy@gmail.com
 */
public interface ExpandableListItem {

    /**
     * Getter for the list of this parent list item's child list items.
     *
     * @return A {@link List} of the children of this {@link ExpandableListItem}
     */
    List<?> getChildItemList();

    /**
     * @return true if expanded, false if not
     */
    boolean isExpanded();

    /**
     * set expand state
     * @param isExpanded
     */
    void setExpanded(boolean isExpanded);
}