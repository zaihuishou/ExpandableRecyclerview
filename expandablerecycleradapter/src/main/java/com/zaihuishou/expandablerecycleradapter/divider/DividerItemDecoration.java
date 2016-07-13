package com.zaihuishou.expandablerecycleradapter.divider;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractAdapterItem;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractParentAdapterItem;
import com.zaihuishou.expandablerecycleradapter.viewholder.RcvAdapterItem;

/**
 * Created by zhiqiang on 15-11-12.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * 分割线
     */
    private Drawable dividerDrawable;
    /**
     * 分割线距离左边或者顶部的边距
     */
    private int mLeftOrTopPadding;
    /**
     * 分割线距离右边后者下边的边距
     */
    private int mRightOrBottomPadding;

    /**
     * 分割线高度
     */
    private int mHeight;


    /**
     * 全参构造器
     *
     * @param divider              <p>分割线</p>
     * @param leftOrTopPadding     <p>分割线距离左边或者顶部的边距，不传默认为0 <br/> 当布局管理器用的是
     *                             {@link LinearLayoutManager#VERTICAL}时候为左边边距,
     *                             {@link LinearLayoutManager#HORIZONTAL}为顶部边距</p>
     * @param rightOrBottomPadding <p>分割线距离右边后者下边的边距,不传默认为0<br/>当布局管理器用的是
     *                             {@link LinearLayoutManager#VERTICAL}时候为右边边距,
     *                             {@link LinearLayoutManager#HORIZONTAL}为下边边距</p>
     * @param height               <p>分割线的高度,默认为分割线资源文件高度
     */
    public DividerItemDecoration(Drawable divider, int leftOrTopPadding, int rightOrBottomPadding, int height) {
        dividerDrawable = divider;
        this.mLeftOrTopPadding = leftOrTopPadding;
        this.mRightOrBottomPadding = rightOrBottomPadding;
        this.mHeight = height;
    }

    /**
     * @param divider <p>分割线</p>
     * @param height  <p>分割线的高度
     */
    public DividerItemDecoration(Drawable divider, int height) {
        dividerDrawable = divider;
        this.mLeftOrTopPadding = 0;
        this.mRightOrBottomPadding = 0;
        this.mHeight = height;
    }

    public DividerItemDecoration(Drawable divider) {
        dividerDrawable = divider;
        this.mLeftOrTopPadding = 0;
        this.mRightOrBottomPadding = 0;
        this.mHeight = divider.getIntrinsicHeight();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (dividerDrawable == null) {
            return;
        }
        //如果是第一个item，不需要divider，所以直接return
        if (parent.getChildLayoutPosition(view) < 1) {
            return;
        }
        //相当于给itemView设置margin，给divider预留空间
        int layoutOrientation = getOrientation(parent);
        if (layoutOrientation == LinearLayoutManager.VERTICAL) {
            outRect.top = mHeight;
        } else if (layoutOrientation == LinearLayoutManager.HORIZONTAL) {
            outRect.left = mHeight;
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (dividerDrawable == null) {
            return;
        }
        LinearLayoutManager layoutManager = getLinearLayoutManger(parent);
        if (layoutManager == null) {
            return;
        }
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        int orientation = getOrientation(layoutManager);
        int childCount = parent.getChildCount();
        if (orientation == LinearLayoutManager.VERTICAL) {//RecyclerView  为垂直滚动的时候
            int left = parent.getPaddingLeft();  //分割线左边位置： RecyclerView的paddingLeft 加上 自定义的 分割线左边距离
            int right = parent.getWidth() - parent.getPaddingRight() - mRightOrBottomPadding;  //分割线右边边位置： RecyclerView的宽度减去paddingRight 再减去 自定义的 分割线右边距离
            for (int i = 0; i < childCount; i++) {
                if (i == 0 && firstVisiblePosition == 0) {
                    continue;
                }
                View childView = parent.getChildAt(i);
                int rLeft = left;
                //is parent
                RcvAdapterItem childViewHolder = (RcvAdapterItem) parent.getChildViewHolder(childView);
                AbstractAdapterItem<Object> item = childViewHolder.getItem();
                if (!(item instanceof AbstractParentAdapterItem))
                    rLeft += mLeftOrTopPadding;
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();
                int bottom = childView.getTop() - params.topMargin;
                int top = bottom - mHeight;
                dividerDrawable.setBounds(rLeft, top, right, bottom);
                dividerDrawable.draw(c);
            }
        } else if (orientation == LinearLayoutManager.HORIZONTAL) {
            for (int i = 0; i < childCount; i++) {
                if (i == 0 && firstVisiblePosition == 0) {
                    continue;
                }
                View childView = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();
                int top = parent.getPaddingTop() + mLeftOrTopPadding;
                int bottom = childView.getBottom() - mRightOrBottomPadding;
                int right = childView.getLeft() - params.leftMargin;
                int left = right - mHeight;
                dividerDrawable.setBounds(left, top, right, bottom);
                dividerDrawable.draw(c);
            }
        }
    }

    private LinearLayoutManager getLinearLayoutManger(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            return (LinearLayoutManager) layoutManager;
        }
        return null;
    }

    private int getOrientation(RecyclerView parent) {
        LinearLayoutManager layoutManager = getLinearLayoutManger(parent);
        if (layoutManager != null) {
            return layoutManager.getOrientation();
        }
        return -1;
    }

    private int getOrientation(LinearLayoutManager layoutManager) {
        if (layoutManager != null) {
            return layoutManager.getOrientation();
        }
        return -1;
    }

}