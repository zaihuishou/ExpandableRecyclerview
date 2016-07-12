package com.zaihuishou.expandablerecycleradapter.ViewHolder;

import android.view.View;

import com.zaihuishou.expandablerecycleradapter.Model.ParentListItem;


/**
 * Keeps track of expanded state and holds callbacks which can be used to
 * trigger expansion-based events.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public abstract class AbstractParentAdapterItem extends AbstractAdapterItem implements View.OnClickListener {

    private int itemIndex = -1;
    private ParentListItemExpandCollapseListener mParentListItemExpandCollapseListener;
    private ParentListItem mParentListItem;

    @Override
    public void onUpdateViews(java.lang.Object model, int position) {
        this.itemIndex = position;
        if (model instanceof ParentListItem)
            mParentListItem = (ParentListItem) model;
    }

    @Override
    public void onBindViews(View root) {
        root.setOnClickListener(this);
    }

    /**
     * implementations to be notified of expand/collapse state change events.
     */
    public interface ParentListItemExpandCollapseListener {

        /**
         * Called when a list item is expanded.
         *
         * @param position The index of the item in the list being expanded
         */
        void onParentListItemExpanded(int position);

        /**
         * Called when a list item is collapsed.
         *
         * @param position The index of the item in the list being collapsed
         */
        void onParentListItemCollapsed(int position);

    }

    /**
     * Callback triggered when expansion state is changed, but not during
     * initialization.
     * <p>
     * Useful for implementing animations on expansion.
     *
     * @param expanded true if view is expanded before expansion is toggled,
     *                 false if not
     */
    public abstract void onExpansionToggled(boolean expanded);

    /**
     * Getter for the {@link ParentListItemExpandCollapseListener} implemented in
     *
     * @return The {@link ParentListItemExpandCollapseListener} set in the {@link AbstractParentAdapterItem}
     */
    public ParentListItemExpandCollapseListener getParentListItemExpandCollapseListener() {
        return mParentListItemExpandCollapseListener;
    }

    /**
     * Setter for the {@link ParentListItemExpandCollapseListener} implemented in
     *
     * @param parentListItemExpandCollapseListener The {@link ParentListItemExpandCollapseListener} to set on the {@link AbstractParentAdapterItem}
     */
    public void setParentListItemExpandCollapseListener(ParentListItemExpandCollapseListener parentListItemExpandCollapseListener) {
        mParentListItemExpandCollapseListener = parentListItemExpandCollapseListener;
    }

    /**
     * {@link android.view.View.OnClickListener} to listen for click events on
     * the entire parent {@link View}.
     * <p>
     * Only registered if {@link #shouldItemViewClickToggleExpansion()} is true.
     *
     * @param v The {@link View} that is the trigger for expansion
     */
    @Override
    public void onClick(View v) {
        if (mParentListItem != null) {
            if (mParentListItem.isExpanded()) {
                collapseView();
            } else {
                expandView();
            }
        }
    }

    /**
     * Used to determine whether a click in the entire parent {@link View}
     * should trigger row expansion.
     * <p>
     * If you return false, you can call {@link #expandView()} to trigger an
     * expansion in response to a another event or {@link #collapseView()} to
     * trigger a collapse.
     *
     * @return true to set an {@link android.view.View.OnClickListener} on the item view
     */
    public boolean shouldItemViewClickToggleExpansion() {
        return true;
    }

    /**
     * Triggers expansion of the parent.
     */
    protected void expandView() {
        onExpansionToggled(false);
        if (mParentListItemExpandCollapseListener != null) {
            mParentListItemExpandCollapseListener.onParentListItemExpanded(itemIndex);
        }
    }

    /**
     * Triggers collapse of the parent.
     */
    protected void collapseView() {
        onExpansionToggled(true);
        if (mParentListItemExpandCollapseListener != null) {
            mParentListItemExpandCollapseListener.onParentListItemCollapsed(itemIndex);
        }
    }
}
