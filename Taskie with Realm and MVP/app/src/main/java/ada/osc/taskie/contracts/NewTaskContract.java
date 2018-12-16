package ada.osc.taskie.contracts;

import java.util.Date;

import ada.osc.taskie.model.Category;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskPriority;

public interface NewTaskContract {
    interface View{
        void showToastWithString(String str);
        void setViewsToValuesOf(Task task);

        String getTitleEntry();
        String getDescriptionEntry();
        TaskPriority getPriorityEntry();
        Date getDateEntry();

        void setTitleError(String errorMessage);
        void setDescriptionError(String errorMessage);

        void onTaskSaved();
    }

    interface Presenter{
        void setView(NewTaskContract.View newTaskView);
        void initializeToValuesOf(String taskToEditId);
        void saveTask();
        void setSelectedCategory(String categoryId);
        Category getSelectedCategory();
    }
}
