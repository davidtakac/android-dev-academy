package ada.osc.taskie.ui.tasks.favorite;

import java.util.List;

import ada.osc.taskie.model.Task;
import ada.osc.taskie.ui.tasks.all.AllTasksContract;

public interface FavoriteTasksContract {

    interface View {

        void showTasks(List<Task> tasks);

        void showMoreTasks(List<Task> tasks);

        void onTaskRemoved(String taskId);

        void showNetworkError();
    }

    interface Presenter {

        void setView(FavoriteTasksContract.View favoriteTasksView);

        void getFavoriteTasks();

        void deleteTask(Task task);

        void setTaskFavorite(Task task);

        void loadPage(int pageIndex);
    }
}
