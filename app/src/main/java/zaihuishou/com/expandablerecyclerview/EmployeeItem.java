package zaihuishou.com.expandablerecyclerview;

import android.view.View;
import android.widget.TextView;

import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractAdapterItem;


public class EmployeeItem extends AbstractAdapterItem {

    private TextView mName;

    @Override
    public int getLayoutResId() {
        return R.layout.item_employee;
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
        if (model instanceof Employee) {
            Employee employee = (Employee) model;
            mName.setText(employee.name);
        }
    }
}
