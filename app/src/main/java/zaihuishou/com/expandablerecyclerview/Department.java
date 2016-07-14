package zaihuishou.com.expandablerecyclerview;


import com.zaihuishou.expandablerecycleradapter.model.ExpandableListItem;

import java.util.List;

public class Department implements ExpandableListItem {

    private boolean mExpand = false;
    public String name;
    public List<Employee> mEmployees;

    @Override
    public List<?> getChildItemList() {
        return mEmployees;
    }

    @Override
    public boolean isExpanded() {
        return mExpand;
    }

    @Override
    public void setExpanded(boolean isExpanded) {
        mExpand = isExpanded;
    }

    @Override
    public String toString() {
        return "Department{" +
                "mExpand=" + mExpand +
                ", name='" + name + '\'' +
                ", mEmployees=" + mEmployees +
                '}';
    }
}
