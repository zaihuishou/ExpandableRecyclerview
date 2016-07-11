package zaihuishou.com.expandablerecyclerview;

import com.zaihuishou.expandablerecycleradapter.Model.ParentListItem;

import java.util.List;

/**
 * 创建者: zhiqiang(谭志强)
 * 创建时间 16-7-9.
 * 作者邮箱 tanzhiqiang@todayoffice.cn
 * 描述:
 */

public class Department implements ParentListItem {

    private boolean mExpand = false;
    public String name;
    public List<Employee> mEmployees;

    @Override
    public List<?> getChildItemList() {
        return mEmployees;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
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
