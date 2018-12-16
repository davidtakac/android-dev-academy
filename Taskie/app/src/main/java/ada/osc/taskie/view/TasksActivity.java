package ada.osc.taskie.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.Collections;
import java.util.List;

import ada.osc.taskie.R;
import ada.osc.taskie.database.DatabaseHelper;
import ada.osc.taskie.model.TaskList;
import ada.osc.taskie.networking.ApiService;
import ada.osc.taskie.networking.RetrofitUtil;
import ada.osc.taskie.util.CompareByPriority;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskPriority;
import ada.osc.taskie.util.SharedPrefsUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TasksActivity extends AppCompatActivity {

    private static final String TAG = "David";
    public static final String EXTRA_EDITTASK = "edit task";

    private DatabaseHelper mDatabaseHelper = null;

    private TaskAdapter mTaskAdapter;

    private boolean mIncludeCompletedTasks = true;
    private boolean mSortByPriority = false;
    //Null represents "not specified", shows tasks of all priorities.
    private TaskPriority mPriorityFilter = null;

    @BindView(R.id.fab_tasks_addNew)
    FloatingActionButton mNewTask;
    @BindView(R.id.recycler_tasks)
    RecyclerView mTasksRecycler;

    TaskClickListener mTaskClickListener = new TaskClickListener() {
        @Override
        public void onClick(Task task) {
            Intent editTask = new Intent();
            editTask.setClass(TasksActivity.this, NewTaskActivity.class);
            editTask.putExtra(EXTRA_EDITTASK, task.getId());

            startActivity(editTask);
        }

        @Override
        public void onLongClick(final Task task) {
            showAlertDialogToDelete(task);
        }

        @Override
        public void onToggleButtonClick(Task task) {
            task.toggleCompleted();
            getHelper().getTaskDao().update(task);
            updateTasksDisplayFromDatabase();
        }

        @Override
        public void onPriorityClick(Task task) {
            task.cyclePriority();
            getHelper().getTaskDao().update(task);
            updateTasksDisplayFromDatabase();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        ButterKnife.bind(this);
        setUpRecyclerView();
        //updateTasksDisplayFromDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTasksDisplayFromDatabase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatabaseHelper != null) {
            OpenHelperManager.releaseHelper();
            mDatabaseHelper = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_menu_sortByPriority:
                if (item.isChecked()) {
                    item.setChecked(false);
                    mSortByPriority = false;
                } else {
                    item.setChecked(true);
                    mSortByPriority = true;
                }
                break;
            case R.id.itemcheckable_menu_showNotCompleted:
                if (item.isChecked()) {
                    item.setChecked(false);
                    mIncludeCompletedTasks = false;
                } else {
                    item.setChecked(true);
                    mIncludeCompletedTasks = true;
                }
                break;
            case R.id.item_priorityFilterMenu_all:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    mPriorityFilter = null;
                }
                break;
            case R.id.item_priorityFilterMenu_low:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    mPriorityFilter = TaskPriority.LOW;
                }
                break;
            case R.id.item_priorityFilterMenu_med:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    mPriorityFilter = TaskPriority.MEDIUM;
                }
                break;
            case R.id.item_priorityFilterMenu_high:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    mPriorityFilter = TaskPriority.HIGH;
                }
                break;
        }
        updateTasksDisplayFromDatabase();
        return super.onOptionsItemSelected(item);
    }

    private void setUpRecyclerView() {

        int orientation = LinearLayoutManager.VERTICAL;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                this,
                orientation,
                false
        );

        RecyclerView.ItemDecoration decoration =
                new DividerItemDecoration(this, orientation);

        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();

        mTaskAdapter = new TaskAdapter(mTaskClickListener);

        mTasksRecycler.setLayoutManager(layoutManager);
        mTasksRecycler.addItemDecoration(decoration);
        mTasksRecycler.setItemAnimator(animator);
        mTasksRecycler.setAdapter(mTaskAdapter);

        getTasksFromServer();
    }

    private void updateTasksDisplayFromDatabase() {
        List<Task> tasks = getHelper().getTaskDao().queryFor(mIncludeCompletedTasks, mPriorityFilter);

        if (mSortByPriority) {
            Collections.sort(tasks, new CompareByPriority());
            Collections.reverse(tasks);
        }

        mTaskAdapter.updateTasks(tasks);
    }

    private void updateTasksDisplay(List<Task> tasks){
        if (mSortByPriority) {
            Collections.sort(tasks, new CompareByPriority());
            Collections.reverse(tasks);
        }

        mTaskAdapter.updateTasks(tasks);
    }

    private DatabaseHelper getHelper() {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return mDatabaseHelper;
    }

    public void showAlertDialogToDelete(final Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TasksActivity.this);

        builder.setMessage(R.string.string_deleteTaskFragment_deleteTask)
                .setPositiveButton(R.string.string_delTaskDialogFrag_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getHelper().getTaskDao().delete(task);
                        updateTasksDisplayFromDatabase();
                    }
                })
                .setNegativeButton(R.string.string_delTaskDialogFrag_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cancels deletion, a.k.a does nothing.
                    }
                });
        builder.show();
    }

    @OnClick(R.id.fab_tasks_addNew)
    public void startNewTaskActivity() {
        Intent newTask = new Intent();
        newTask.setClass(this, NewTaskActivity.class);
        startActivity(newTask);
    }

    private void getTasksFromServer(){
        Retrofit retrofit = RetrofitUtil.createRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<TaskList> taskListCall = apiService
                .getTasks(SharedPrefsUtil.getFromPrefs(this
                        , SharedPrefsUtil.TOKEN, null));

        taskListCall.enqueue(new Callback<TaskList>() {
            @Override
            public void onResponse(Call<TaskList> call, Response<TaskList> response) {
                if(response.isSuccessful()){
                    updateTasksDisplay(response.body().mTaskList);
                }
            }

            @Override
            public void onFailure(Call<TaskList> call, Throwable t) {

            }
        });
    }
}
