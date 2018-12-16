package ada.osc.taskie.ui.addTask;

import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskPriority;

public interface NewTaskContract {

    interface View {

        void onTaskCreated();

        void showNetworkError();

        void showTaskError();

        void setTitle(String title);

        void setContent(String content);

        void setPriority(int priority);
    }

    interface Presenter {

        void setView(View newTaskView);

        void createTask(Task task);

        void setEntriesToValuesOf(String taskId);

        void updateTaskWithValues(String taskId, String title, String content, TaskPriority priority);
    }
}
