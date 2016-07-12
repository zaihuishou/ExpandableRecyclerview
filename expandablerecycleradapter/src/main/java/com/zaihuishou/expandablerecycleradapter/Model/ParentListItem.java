package com.zaihuishou.expandablerecycleradapter.Model;

import java.util.List;

/**
 * Interface for implementing required methods in a parent list item.
 */
public interface ParentListItem {

    /**
     * Getter for the list of this parent list item's child list items.
     *
     * @return A {@link List} of the children of this {@link ParentListItem}
     */
    List<?> getChildItemList();

    /**
     * @return true if expanded, false if not
     */
    boolean isExpanded();

    void setExpanded(boolean isExpanded);
}