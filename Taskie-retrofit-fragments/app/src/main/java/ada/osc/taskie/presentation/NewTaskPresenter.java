package ada.osc.taskie.presentation;

import android.content.SharedPreferences;

import ada.osc.taskie.interaction.ApiInteractor;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskPriority;
import ada.osc.taskie.ui.addTask.NewTaskContract;
import ada.osc.taskie.util.SharedPrefsUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewTaskPresenter implements NewTaskContract.Presenter {

    private final SharedPreferences preferences;
    private final ApiInteractor apiInteractor;

    private NewTaskContract.View newTaskView;

    public NewTaskPresenter(SharedPreferences preferences, ApiInteractor apiInteractor) {
        this.preferences = preferences;
        this.apiInteractor = apiInteractor;
    }

    @Override
    public void setView(NewTaskContract.View newTaskView) {
        this.newTaskView = newTaskView;
    }

    @Override
    public void createTask(Task task) {
        if (task != null && !task.getDescription().isEmpty()
                && !task.getTitle().isEmpty()) {
            apiInteractor.addNewTask(task, getFinishActivityCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
        } else {
            newTaskView.showTaskError();
        }
    }

    //finishes activity on successful response, which happens either when
    //a task is successfully updated or created on the server.
    private Callback<Task> getFinishActivityCallback() {
        return new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful() && response.body() != null) {
                    newTaskView.onTaskCreated();
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                newTaskView.showNetworkError();
            }
        };
    }

    @Override
    public void setEntriesToValuesOf(String taskId){
        apiInteractor.getTask(taskId, getTaskFromServerCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
    }

    @Override
    public void updateTaskWithValues(String taskId, String title, String content, TaskPriority priority) {
        apiInteractor.getTask(taskId
                , getEditTaskCallback(title, content, priority)
                , preferences.getString(SharedPrefsUtil.TOKEN, "")
        );
    }

    private Callback<Task> getEditTaskCallback(final String title, final String content, final TaskPriority priority) {
        return new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if(response.isSuccessful() && response.body() != null){
                    Task taskToEdit = response.body();
                    taskToEdit.setTitle(title);
                    taskToEdit.setDescription(content);
                    taskToEdit.setPriority(priority.ordinal() + 1);

                    apiInteractor.updateTask(taskToEdit, getFinishActivityCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                // TODO: 5/22/2018 error handling
            }
        };
    }

    private void setViewToValuesOf(Task task) {
        newTaskView.setTitle(task.getTitle());
        newTaskView.setContent(task.getDescription());
        newTaskView.setPriority(task.getPriority() - 1);
    }

    //when the task is successfully retrieved, sets the associated view's
    //fields to the values of the retrieved task.
    private Callback<Task> getTaskFromServerCallback() {
        return new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if(response.isSuccessful() && response.body() != null){
                    setViewToValuesOf(response.body());
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                // TODO: 5/22/2018 oopsie woopsie
            }
        };
    }


}
