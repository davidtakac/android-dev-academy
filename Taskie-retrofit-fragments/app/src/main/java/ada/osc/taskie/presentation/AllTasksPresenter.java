package ada.osc.taskie.presentation;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;

import ada.osc.taskie.interaction.ApiInteractor;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskList;
import ada.osc.taskie.ui.tasks.all.AllTasksContract;
import ada.osc.taskie.util.SharedPrefsUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllTasksPresenter implements AllTasksContract.Presenter {

    private final ApiInteractor apiInteractor;
    private final SharedPreferences preferences;

    private AllTasksContract.View allTasksView;

    public AllTasksPresenter(ApiInteractor apiInteractor, SharedPreferences preferences) {
        this.apiInteractor = apiInteractor;
        this.preferences = preferences;
    }

    @Override
    public void setView(AllTasksContract.View allTasksView) {
        this.allTasksView = allTasksView;
    }

    @Override
    public void getTasks() {
        apiInteractor.getTasks(allTasksCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
    }

    private Callback<TaskList> allTasksCallback() {
        return new Callback<TaskList>() {
            @Override
            public void onResponse(Call<TaskList> call, Response<TaskList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Task> tasks = response.body().mTaskList;

                    allTasksView.showTasks(tasks);
                }
            }

            @Override
            public void onFailure(Call<TaskList> call, Throwable t) {
                allTasksView.showNetworkError();
            }
        };
    }

    public void setTasksDisplayToFirstPage(){
        apiInteractor.getPageOfTasks(1, firstPageCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
    }

    private Callback<TaskList> firstPageCallback() {
        return new Callback<TaskList>() {
            @Override
            public void onResponse(Call<TaskList> call, Response<TaskList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().mTaskList.size() > 0) {
                        Log.d("david", "mTaskList size: " + response.body().mTaskList.size());

                        allTasksView.showTasks(response.body().mTaskList);
                    }
                }
            }

            @Override
            public void onFailure(Call<TaskList> call, Throwable t) {
                allTasksView.showNetworkError();
            }
        };
    }

    @Override
    public void deleteTask(Task task) {
        apiInteractor.deleteTask(task.getmId(), deleteTaskCallback(task.getmId()), preferences.getString(SharedPrefsUtil.TOKEN, ""));
    }

    private Callback<Task> deleteTaskCallback(final String taskToDeleteId) {
        return new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful()) {
                    allTasksView.onTaskRemoved(taskToDeleteId);
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                allTasksView.showNetworkError();
            }
        };
    }

    @Override
    public void setTaskFavorite(Task task) {
        apiInteractor.toggleTaskFavorite(task.getmId(), toggleTaskFavoriteCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
    }

    private Callback<Task> toggleTaskFavoriteCallback() {
        return new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setTasksDisplayToFirstPage();
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                allTasksView.showNetworkError();
            }
        };
    }

    @Override
    public void loadPage(int pageIndex) {
        apiInteractor.getPageOfTasks(pageIndex, pageOfTasksCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
    }

    private Callback<TaskList> pageOfTasksCallback() {
        return new Callback<TaskList>() {
            @Override
            public void onResponse(Call<TaskList> call, Response<TaskList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().mTaskList.size() > 0) {
                        Log.d("david", "mTaskList size: " + response.body().mTaskList.size());

                        allTasksView.showMoreTasks(response.body().mTaskList);
                    }
                }
            }

            @Override
            public void onFailure(Call<TaskList> call, Throwable t) {
                allTasksView.showNetworkError();
            }
        };
    }
}
