package zaihuishou.com.expandablerecyclerview;

import android.view.View;
import android.widget.TextView;

import com.zaihuishou.expandablerecycleradapter.ViewHolder.AbstractAdapterItem;

/**
 * 创建者: zhiqiang(谭志强)
 * 创建时间 16-7-11.
 * 作者邮箱 tanzhiqiang@todayoffice.cn
 * 描述:
 */

public class DepartmentItem extends AbstractAdapterItem {

    private TextView mName;

    @Override
    public int getLayoutResId() {
        return R.layout.item_department;
    }

    @Override
    public void onBindViews(View root) {
        mName = (TextView) root.findViewById(R.id.tv_name);
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(Object model, int position) {
        Department department = (Department) model;
        mName.setText(department.name);
    }
}
