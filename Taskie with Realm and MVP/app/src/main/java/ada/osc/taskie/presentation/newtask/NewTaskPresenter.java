package ada.osc.taskie.presentation.newtask;

import android.content.Context;
import android.util.Log;

import java.util.Date;

import ada.osc.taskie.App;
import ada.osc.taskie.R;
import ada.osc.taskie.contracts.NewTaskContract;
import ada.osc.taskie.database.category.CategoryDao;
import ada.osc.taskie.database.task.TaskDao;
import ada.osc.taskie.model.Category;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskPriority;
import ada.osc.taskie.util.Constants;
import ada.osc.taskie.util.DateUtil;
import ada.osc.taskie.util.SharedPrefsUtil;

public class NewTaskPresenter implements NewTaskContract.Presenter {

    private NewTaskContract.View view;
    private TaskDao taskDao;
    private CategoryDao categoryDao;

    private Task taskToEdit = null;
    private Category selectedCategory;

    private Context context;

    public NewTaskPresenter(TaskDao taskDao, CategoryDao categoryDao, Context context) {
        this.taskDao = taskDao;
        this.categoryDao = categoryDao;
        this.context = context;

        selectedCategory = this.categoryDao.getNoneCategory();
    }

    @Override
    public void setView(NewTaskContract.View newTaskView) {
        view = newTaskView;
    }

    @Override
    public void initializeToValuesOf(String taskToEditId) {
        taskToEdit = taskDao.queryForId(taskToEditId);
        selectedCategory = taskToEdit.getCategory();
        view.setViewsToValuesOf(taskToEdit);
    }

    @Override
    public void saveTask() {
        String title = view.getTitleEntry();
        String description = view.getDescriptionEntry();

        if (title.isEmpty()) {
            view.setTitleError(getStringFromRes(R.string.errormsg_edittext_blankInput));
            return;
        }
        if (description.isEmpty()) {
            view.setDescriptionError(getStringFromRes(R.string.errormsg_edittext_blankInput));
            return;
        }

        Date date = view.getDateEntry();
        if (DateUtil.isDateBeforeToday(date)) {
            view.showToastWithString(getStringFromRes(R.string.errormsg_calendarview_invalidDeadline));
            return;
        }

        TaskPriority priority = view.getPriorityEntry();
        savePriorityToSharedPrefs(priority);

        if (taskToEdit == null) {
            Task newTask = new Task();

            newTask.setTitle(title);
            newTask.setDescription(description);
            newTask.setPriority(priority);
            newTask.setDeadline(date);
            newTask.setCategory(selectedCategory);

            taskDao.copyToRealm(newTask);
        } else {
            Log.d(Constants.LOG_TAG, "editing task");
            taskDao.editTaskTitle(taskToEdit, title);
            taskDao.editTaskDescription(taskToEdit, description);
            taskDao.editTaskPriority(taskToEdit, priority);
            taskDao.editTaskDeadline(taskToEdit, date);
            taskDao.editTaskCategory(taskToEdit, selectedCategory);

            taskDao.copyToRealmOrUpdate(taskToEdit);
        }
        view.onTaskSaved();
    }

    @Override
    public void setSelectedCategory(String categoryId) {
        selectedCategory = categoryDao.getCategoryById(categoryId);
    }

    @Override
    public Category getSelectedCategory() {
        return selectedCategory;
    }

    private void savePriorityToSharedPrefs(TaskPriority priority) {
        SharedPrefsUtil.saveToPrefs(context, Constants.PRIORITY_TASK_FIELD_NAME, priority.toString());
    }

    private String getStringFromRes(int stringId) {
        return App.getContext().getResources().getString(stringId);
    }
}
