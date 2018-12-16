package ada.osc.taskie.presentation;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;

import ada.osc.taskie.interaction.ApiInteractor;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskList;
import ada.osc.taskie.ui.tasks.favorite.FavoriteTasksContract;
import ada.osc.taskie.util.SharedPrefsUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteTaskPresenter implements FavoriteTasksContract.Presenter {

    private final SharedPreferences preferences;
    private final ApiInteractor apiInteractor;

    private FavoriteTasksContract.View favoriteTasksView;

    public FavoriteTaskPresenter(SharedPreferences preferences, ApiInteractor apiInteractor) {
        this.preferences = preferences;
        this.apiInteractor = apiInteractor;
    }

    @Override
    public void setView(FavoriteTasksContract.View favoriteTasksView) {
        this.favoriteTasksView = favoriteTasksView;
    }

    @Override
    public void getFavoriteTasks() {
        apiInteractor.getFavoriteTasks(favoriteTasksCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
    }

    private Callback<TaskList> favoriteTasksCallback() {
        return new Callback<TaskList>() {
            @Override
            public void onResponse(Call<TaskList> call, Response<TaskList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Task> tasks = response.body().mTaskList;

                    favoriteTasksView.showTasks(tasks);
                }
            }

            @Override
            public void onFailure(Call<TaskList> call, Throwable t) {
                favoriteTasksView.showNetworkError();
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
                if(response.isSuccessful()){
                    favoriteTasksView.onTaskRemoved(taskToDeleteId);
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                favoriteTasksView.showNetworkError();
            }
        };
    }

    @Override
    public void setTaskFavorite(Task task) {
        apiInteractor.toggleTaskFavorite(task.getmId(), toggleFavoriteCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
    }

    private Callback<Task> toggleFavoriteCallback() {
        return new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if(response.isSuccessful() && response.body() != null){
                    reloadTasksDisplay();
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                favoriteTasksView.showNetworkError();
            }
        };
    }

    @Override
    public void loadPage(int pageIndex) {
        apiInteractor.getPageOfFavoriteTasks(pageIndex, pageOfTasksCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
    }

    private Callback<TaskList> pageOfTasksCallback() {
        return new Callback<TaskList>() {
            @Override
            public void onResponse(Call<TaskList> call, Response<TaskList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().mTaskList.size() > 0) {
                        Log.d("david", "mTaskList size: " + response.body().mTaskList.size());

                        favoriteTasksView.showMoreTasks(response.body().mTaskList);
                    }
                }
            }

            @Override
            public void onFailure(Call<TaskList> call, Throwable t) {
                favoriteTasksView.showNetworkError();
            }
        };
    }

    public void reloadTasksDisplay(){
        apiInteractor.getPageOfFavoriteTasks(1, getFirstPageCallback(), preferences.getString(SharedPrefsUtil.TOKEN, ""));
    }

    private Callback<TaskList> getFirstPageCallback() {
        return new Callback<TaskList>() {
            @Override
            public void onResponse(Call<TaskList> call, Response<TaskList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().mTaskList.size() > 0) {
                        Log.d("david", "mTaskList size: " + response.body().mTaskList.size());

                        favoriteTasksView.showTasks(response.body().mTaskList);
                    }
                }
            }

            @Override
            public void onFailure(Call<TaskList> call, Throwable t) {
                favoriteTasksView.showNetworkError();
            }
        };
    }
}
