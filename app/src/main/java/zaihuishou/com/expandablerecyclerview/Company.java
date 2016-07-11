package zaihuishou.com.expandablerecyclerview;

import com.zaihuishou.expandablerecycleradapter.Model.ParentListItem;

import java.util.List;

/**
 * 创建者: zhiqiang(谭志强)
 * 创建时间 16-7-9.
 * 作者邮箱 tanzhiqiang@todayoffice.cn
 * 描述:
 */

public class Company implements ParentListItem {

    public String name;
    public List<Department> mDepartments;

    @Override
    public List<?> getChildItemList() {
        return mDepartments;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
