package zaihuishou.com.expandablerecyclerview;

import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractParentAdapterItem;

/**
 * 创建者: zhiqiang(谭志强)
 * 创建时间 16-7-9.
 * 作者邮箱 tanzhiqiang@todayoffice.cn
 * 描述:
 */

public class CompanyItem extends AbstractParentAdapterItem {

    private TextView mName;
    private ImageView mArrow;

    @Override
    public int getLayoutResId() {
        return R.layout.item_company;
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
        }else {
            start = 90f;
            target = 0f;
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mArrow, View.ROTATION, start, target);
        objectAnimator.setDuration(300);
        objectAnimator.start();
    }

    @Override
    public void onSetViews() {
        mArrow.setImageResource(0);
        mArrow.setImageResource(R.mipmap.arrow_down);
    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        onSetViews();
        Company company = (Company) model;
        mName.setText(company.name);
        if (position == 0) {
            Log.i("BaseExpandableAdapter", "company:" + company.name);
        }
    }
}
