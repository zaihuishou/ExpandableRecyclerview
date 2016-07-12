package zaihuishou.com.expandablerecyclerview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zaihuishou.expandablerecycleradapter.Adapter.BaseExpandableAdapter;
import com.zaihuishou.expandablerecycleradapter.ViewHolder.AbstractAdapterItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private BaseExpandableAdapter mBaseRcvAdapter;
    private List mCompanylist;
    private boolean hasAdd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mRecyclerView != null) {
                    Department department = new Department();
                    if (!hasAdd) {
                        department.name = "Add a department";
                        mBaseRcvAdapter.addItem(department);
                        hasAdd = true;
                    } else {
                        department.name = "change department";
                        mBaseRcvAdapter.modifyItem(mBaseRcvAdapter.getItemCount() - 1,department);
                    }
                }
            }
        });
        mCompanylist = new ArrayList<>();

        mCompanylist.add(creaateCompany("Google"));

        mCompanylist.add(creaateCompany("Apple"));
        mBaseRcvAdapter = new BaseExpandableAdapter(mCompanylist) {
            @NonNull
            @Override
            public AbstractAdapterItem<Object> getItemView(Object type) {
                int type1 = (int) type;
                switch (type1) {
                    case 1:
                        return new CompanyItem();
                    case 2:
                        return new DepartmentItem();
                    case 3:
                        return new EmployeeItem();
                }
                return null;
            }

            @Override
            public Object getItemViewType(Object t) {
                if (t instanceof Company) {
                    return 1;
                } else if (t instanceof Department)
                    return 2;
                else if (t instanceof Employee)
                    return 3;
                return 0;
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mBaseRcvAdapter);
    }

    @NonNull
    private Company creaateCompany(String companyName) {
        Company firstCompany = new Company();
        firstCompany.name = companyName;

        List<Department> departments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Department department = new Department();
            department.name = " sub-department:" + i;
            if (i == 0) {
                List<Employee> employeeList = new ArrayList<>();
                for (int j = 0; j < 4; j++) {
                    Employee employee = new Employee();
                    employee.name = "employee:" + j;
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
