package zaihuishou.com.expandablerecyclerview;

import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractExpandableAdapterItem;


public class CompanyItem extends AbstractExpandableAdapterItem {

    private TextView mName;
    private ImageView mArrow;
    private Company mCompany;

    @Override
    public int getLayoutResId() {
        return R.layout.item_company;
    }

    @Override
    public void onBindViews(final View root) {
        /**
         * control item expand and unexpand
         */
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doExpandOrUnexpand();
                Toast.makeText(root.getContext(), "click company：" +mCompany.name,Toast.LENGTH_SHORT).show();
            }
        });
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
        mArrow.setImageResource(0);
        mArrow.setImageResource(R.mipmap.arrow_down);
    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        onSetViews();
        onExpansionToggled(getExpandableListItem().isExpanded());
        mCompany = (Company) model;
        mName.setText(mCompany.name);
    }


}
