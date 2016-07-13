package zaihuishou.com.expandablerecyclerview;

import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaihuishou.expandablerecycleradapter.model.ParentListItem;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractParentAdapterItem;

import java.util.List;

/**
 * 创建者: zhiqiang(谭志强)
 * 创建时间 16-7-11.
 * 作者邮箱 tanzhiqiang@todayoffice.cn
 * 描述:
 */

public class DepartmentItem extends AbstractParentAdapterItem {

    private TextView mName;
    private ImageView mArrow;

    @Override
    public int getLayoutResId() {
        return R.layout.item_department;
    }

    @Override
    public void onBindViews(View root) {
        super.onBindViews(root);
        mName = (TextView) root.findViewById(R.id.tv_name);
        mArrow = (ImageView) root.findViewById(R.id.iv_arrow);
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        float start, target;
        if (expanded) {
            start = 0f;
            target = 90f;
        } else {
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
        ParentListItem parentListItem = (ParentListItem) model;
        List<?> childItemList = parentListItem.getChildItemList();
        if (childItemList != null && !childItemList.isEmpty())
            mArrow.setVisibility(View.VISIBLE);
    }
}
