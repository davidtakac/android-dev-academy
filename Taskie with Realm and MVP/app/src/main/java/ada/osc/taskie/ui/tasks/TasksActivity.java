package ada.osc.taskie.ui.tasks;

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

import java.util.List;

import ada.osc.taskie.App;
import ada.osc.taskie.R;
import ada.osc.taskie.contracts.TasksContract;
import ada.osc.taskie.presentation.tasks.TaskPresenter;
import ada.osc.taskie.ui.newtask.NewTaskActivity;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.ui.tasks.adapter.TaskAdapter;
import ada.osc.taskie.ui.tasks.listener.TaskClickListener;
import ada.osc.taskie.util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TasksActivity extends AppCompatActivity implements TasksContract.View {

    private TasksContract.Presenter presenter;

    private TaskAdapter taskAdapter;

    @BindView(R.id.fab_tasks_addNew)
    FloatingActionButton fabNewTask;
    @BindView(R.id.recycler_tasks)
    RecyclerView rvTasks;

    TaskClickListener taskClickListener = getTaskClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        ButterKnife.bind(this);
        setUpRecyclerView();

        presenter = new TaskPresenter(App.getTaskDao());
        presenter.setView(this);
        presenter.updateTasksDisplay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.updateTasksDisplay();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.handleMenuItemSelected(item);
        presenter.updateTasksDisplay();
        return super.onOptionsItemSelected(item);
    }

    public void showAlertDialogToDelete(final Task task) {
        // TODO: 10-Jun-18 refactor
        AlertDialog.Builder builder = new AlertDialog.Builder(TasksActivity.this);

        builder.setMessage(R.string.string_deleteTaskFragment_deleteTask)
                .setPositiveButton(R.string.string_delTaskDialogFrag_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.deleteTask(task);
                        presenter.updateTasksDisplay();
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
        startActivity(new Intent(this, NewTaskActivity.class));
    }

    @Override
    public void displayTasks(List<Task> tasks) {
        taskAdapter.updateTasks(tasks);
    }

    private TaskClickListener getTaskClickListener(){
        return new TaskClickListener() {
            @Override
            public void onClick(Task task) {
                Intent editTask = new Intent(TasksActivity.this, NewTaskActivity.class);
                editTask.putExtra(Constants.EXTRA_EDITTASK, task.getId());

                startActivity(editTask);
            }

            @Override
            public void onLongClick(final Task task) {
                showAlertDialogToDelete(task);
            }

            @Override
            public void onToggleButtonClick(Task task) {
                presenter.toggleCompleted(task);
                presenter.updateTasksDisplay();
            }

            @Override
            public void onPriorityClick(Task task) {
                presenter.cyclePriority(task);
                presenter.updateTasksDisplay();
            }
        };
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

        taskAdapter = new TaskAdapter(taskClickListener);

        rvTasks.setLayoutManager(layoutManager);
        rvTasks.addItemDecoration(decoration);
        rvTasks.setItemAnimator(animator);
        rvTasks.setAdapter(taskAdapter);
    }
}
