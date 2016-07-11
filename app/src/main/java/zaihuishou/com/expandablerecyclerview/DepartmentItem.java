package zaihuishou.com.expandablerecyclerview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaihuishou.expandablerecycleradapter.Model.ParentListItem;
import com.zaihuishou.expandablerecycleradapter.ViewHolder.AbstractParentAdapterItem;

import java.util.List;

/**
 * 创建者: zhiqiang(谭志强)
 * 创建时间 16-7-11.
 * 作者邮箱 tanzhiqiang@todayoffice.cn
 * 描述:
 */

public class DepartmentItem extends AbstractParentAdapterItem {

    private TextView mName;
    private ImageView mChild;

    @Override
    public int getLayoutResId() {
        return R.layout.item_department;
    }

    @Override
    public void onBindViews(View root) {
        super.onBindViews(root);
        mName = (TextView) root.findViewById(R.id.tv_name);
        mChild = (ImageView) root.findViewById(R.id.iv_child);
    }

    @Override
    public void onSetViews() {
        mChild.setVisibility(View.GONE);
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
            mChild.setVisibility(View.VISIBLE);
    }
}
