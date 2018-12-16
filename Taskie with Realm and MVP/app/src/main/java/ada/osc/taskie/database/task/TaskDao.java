package ada.osc.taskie.database.task;

import java.util.Date;
import java.util.List;

import ada.osc.taskie.model.Category;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskPriority;

public interface TaskDao {
    void copyToRealm(Task task);
    void copyToRealmIfNotExists(Task task);
    void copyToRealmOrUpdate(Task task);
    void deleteFromRealm(Task task);
    List<Task> queryForAll();
    Task queryForId(String taskId);

    List<Task> queryFor(boolean completed, TaskPriority priority, boolean sortByPriority);

    void editTaskTitle(Task toEdit, String newTitle);
    void editTaskDescription(Task toEdit, String newDescription);
    void editTaskPriority(Task toEdit, TaskPriority newPriority);
    void editTaskDeadline(Task toEdit, Date newDeadline);
    void editTaskCategory(Task toEdit, Category newCategory);
    void toggleTaskCompleted(Task toEdit);
    void cycleTaskPriority(Task toEdit);
}
