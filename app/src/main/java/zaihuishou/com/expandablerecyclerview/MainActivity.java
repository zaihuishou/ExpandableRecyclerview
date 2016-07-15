package zaihuishou.com.expandablerecyclerview;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zaihuishou.expandablerecycleradapter.adapter.BaseExpandableAdapter;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractAdapterItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int ITEM_TYPE_COMPANY = 1;
    private final int ITEM_TYPE_DEPARTMENT = 2;
    private final int ITEM_TYPE_EMPLOYEE = 3;

    private RecyclerView mRecyclerView;
    private BaseExpandableAdapter mBaseExpandableAdapter;
    private List mCompanylist;
    private boolean hasAdd = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mRecyclerView != null) {
                    mBaseExpandableAdapter.collaspeAll();
//                    if (!hasAdd) {
//                        Department department = new Department();
//                        department.name = "Add a department";
//                        mBaseExpandableAdapter.addItem(department);
//                        hasAdd = true;
//                        fab.setImageResource(android.R.drawable.ic_delete);
//                        Snackbar.make(view, "add item", Snackbar.LENGTH_SHORT).show();
//                    } else {
//                        mBaseExpandableAdapter.removedItem(mBaseExpandableAdapter.getItemCount() - 1);
//                        hasAdd = false;
//                        fab.setImageResource(android.R.drawable.ic_input_add);
//                        Snackbar.make(view, "delete item", Snackbar.LENGTH_SHORT).show();
//                    }
                }
            }
        });
        initData();
        mBaseExpandableAdapter = new BaseExpandableAdapter(mCompanylist) {
            @NonNull
            @Override
            public AbstractAdapterItem<Object> getItemView(Object type) {
                int itemType = (int) type;
                switch (itemType) {
                    case ITEM_TYPE_COMPANY:
                        return new CompanyItem();
                    case ITEM_TYPE_DEPARTMENT:
                        return new DepartmentItem();
                    case ITEM_TYPE_EMPLOYEE:
                        return new EmployeeItem();
                }
                return null;
            }

            @Override
            public Object getItemViewType(Object t) {
                if (t instanceof Company) {
                    return ITEM_TYPE_COMPANY;
                } else if (t instanceof Department)
                    return ITEM_TYPE_DEPARTMENT;
                else if (t instanceof Employee)
                    return ITEM_TYPE_EMPLOYEE;
                return -1;
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mBaseExpandableAdapter);
        /**
         * item divider
         */
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.line_bg, null), 50, 0, 1));
        /**
         * set ExpandCollapseListener
         */
        mBaseExpandableAdapter.setExpandCollapseListener(new BaseExpandableAdapter.ExpandCollapseListener() {
            @Override
            public void onListItemExpanded(int position) {

            }

            @Override
            public void onListItemCollapsed(int position) {

            }
        });
    }

    private void initData() {
        mCompanylist = new ArrayList<>();

        mCompanylist.add(createCompany("Google"));

        mCompanylist.add(createCompany("Apple"));
    }

    @NonNull
    private Company createCompany(String companyName) {
        Company firstCompany = new Company();
        firstCompany.name = companyName;

        List<Department> departments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Department department = new Department();
            department.name = "Department:" + i;
            if (i == 0) {
                List<Employee> employeeList = new ArrayList<>();
                for (int j = 0; j < 4; j++) {
                    Employee employee = new Employee();
                    employee.name = "Employee:" + j;
                    employeeList.add(employee);
                }
                department.mEmployees = employeeList;
            }
            departments.add(department);
        }
        firstCompany.mDepartments = departments;
        return firstCompany;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
