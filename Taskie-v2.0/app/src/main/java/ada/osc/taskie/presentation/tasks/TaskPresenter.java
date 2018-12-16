package ada.osc.taskie.presentation.tasks;

import android.view.MenuItem;

import java.util.List;

import ada.osc.taskie.R;
import ada.osc.taskie.contracts.TasksContract;
import ada.osc.taskie.database.task.TaskDao;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskPriority;

public class TaskPresenter implements TasksContract.Presenter {

    private TaskDao taskDao;
    private TasksContract.View view;

    private boolean sortByPriority = false;
    private TaskPriority prioritiesToDisplay = null;
    private boolean displayCompletedTasks = true;

    public TaskPresenter(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public void setView(TasksContract.View tasksView) {
        view = tasksView;
    }

    @Override
    public void toggleCompleted(Task task) {
        taskDao.toggleTaskCompleted(task);
    }

    @Override
    public void cyclePriority(Task task) {
        taskDao.cycleTaskPriority(task);
    }

    @Override
    public void updateTasksDisplay() {
        List<Task> tasksToDisplay = taskDao.queryFor(displayCompletedTasks, prioritiesToDisplay, sortByPriority);
        view.displayTasks(tasksToDisplay);
    }

    @Override
    public void deleteTask(Task task) {
        taskDao.deleteFromRealm(task);
    }

    @Override
    public void handleMenuItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_menu_sortByPriority:
                if (item.isChecked()) {
                    item.setChecked(false);
                    sortByPriority = false;
                } else {
                    item.setChecked(true);
                    sortByPriority = true;
                }
                break;
            case R.id.itemcheckable_menu_showNotCompleted:
                if (item.isChecked()) {
                    item.setChecked(false);
                    displayCompletedTasks = false;
                } else {
                    item.setChecked(true);
                    displayCompletedTasks = true;
                }
                break;
            case R.id.item_priorityFilterMenu_all:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    prioritiesToDisplay = null;
                }
                break;
            case R.id.item_priorityFilterMenu_low:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    prioritiesToDisplay = TaskPriority.LOW;
                }
                break;
            case R.id.item_priorityFilterMenu_med:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    prioritiesToDisplay = TaskPriority.MEDIUM;
                }
                break;
            case R.id.item_priorityFilterMenu_high:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    prioritiesToDisplay = TaskPriority.HIGH;
                }
                break;
        }
    }

}
