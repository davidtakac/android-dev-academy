package ada.osc.taskie.contracts;

import android.view.MenuItem;

import java.util.List;

import ada.osc.taskie.model.Task;

public interface TasksContract {
    interface View{
        void displayTasks(List<Task> tasks);
    }

    interface Presenter{
        void setView(TasksContract.View view);
        void toggleCompleted(Task task);
        void cyclePriority(Task task);
        void updateTasksDisplay();
        void deleteTask(Task task);
        void handleMenuItemSelected(MenuItem item);
    }
}
