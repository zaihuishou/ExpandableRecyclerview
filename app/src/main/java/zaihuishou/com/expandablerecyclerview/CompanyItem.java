package zaihuishou.com.expandablerecyclerview;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zaihuishou.expandablerecycleradapter.ViewHolder.AbstractParentAdapterItem;

/**
 * 创建者: zhiqiang(谭志强)
 * 创建时间 16-7-9.
 * 作者邮箱 tanzhiqiang@todayoffice.cn
 * 描述:
 */

public class CompanyItem extends AbstractParentAdapterItem {

    private TextView mName;

    @Override
    public int getLayoutResId() {
        return R.layout.item_company;
    }

    @Override
    public void onBindViews(View root) {
        super.onBindViews(root);
        mName = (TextView) root.findViewById(R.id.tv_name);
    }

    @Override
    public void onExpansionToggled(boolean expanded) {

    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        Company company = (Company) model;
        mName.setText(company.name);
        if (position == 0) {
            Log.i("BaseExpandableAdapter", "company:" + company.name);
        }
    }
}
