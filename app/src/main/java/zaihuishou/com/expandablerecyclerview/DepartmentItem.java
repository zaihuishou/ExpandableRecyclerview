package zaihuishou.com.expandablerecyclerview;

import com.zaihuishou.expandablerecycleradapter.model.ExpandableListItem;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractExpandableAdapterItem;

import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DepartmentItem extends AbstractExpandableAdapterItem implements View.OnClickListener {

    private TextView mName;
    private ImageView mArrow;
    private TextView mExpand;

    @Override
    public int getLayoutResId() {
        return R.layout.item_department;
    }

    @Override
    public void onBindViews(View root) {
        mName = (TextView) root.findViewById(R.id.tv_name);
        mArrow = (ImageView) root.findViewById(R.id.iv_arrow);
        mExpand = (TextView) root.findViewById(R.id.tv_expand);
        mExpand.setOnClickListener(this);
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        float start, target;
        if (expanded) {
            mExpand.setText("unexpand");
            start = 0f;
            target = 90f;
        } else {
            mExpand.setText("expand");
            start = 90f;
            target = 0f;
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mArrow, View.ROTATION, start, target);
        objectAnimator.setDuration(300);
        objectAnimator.start();
    }

    @Override
    public void onSetViews() {
        mArrow.setVisibility(View.GONE);
    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        onSetViews();
        Department department = (Department) model;
        mName.setText(department.name);
        ExpandableListItem parentListItem = (ExpandableListItem) model;
        List<?> childItemList = parentListItem.getChildItemList();
        if (childItemList != null && !childItemList.isEmpty()) {
            mArrow.setVisibility(View.VISIBLE);
            mExpand.setText(parentListItem.isExpanded() ? "unexpand" : "expand");
        } else mExpand.setText("expand");
    }

    @Override
    public void onClick(View view) {
        /**
         * control item expand and unexpand
         */
        if (getExpandableListItem() != null && getExpandableListItem().getChildItemList() != null) {
            if (getExpandableListItem().isExpanded()) {
                collapseView();
            } else {
                expandView();
            }
        }
    }
}
