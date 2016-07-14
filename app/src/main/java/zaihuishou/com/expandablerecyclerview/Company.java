package zaihuishou.com.expandablerecyclerview;


import com.zaihuishou.expandablerecycleradapter.model.ExpandableListItem;

import java.util.List;

public class Company implements ExpandableListItem {

    public boolean mExpanded = false;
    public String name;
    public List<Department> mDepartments;

    @Override
    public List<?> getChildItemList() {
        return mDepartments;
    }

    @Override
    public boolean isExpanded() {
        return mExpanded;
    }

    @Override
    public void setExpanded(boolean isExpanded) {
        mExpanded = isExpanded;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                '}';
    }
}
