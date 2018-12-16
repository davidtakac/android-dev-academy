package ada.osc.taskie.ui.tasks.all;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import ada.osc.taskie.model.Task;
import ada.osc.taskie.ui.tasks.adapter.TaskAdapter;

public interface AllTasksContract {

    interface View {

        void showTasks(List<Task> tasks);

        void showMoreTasks(List<Task> tasks);

        void onTaskRemoved(String taskId);

        void showNetworkError();
    }

    interface Presenter {

        void setView(View allTasksView);

        void getTasks();

        void deleteTask(Task task);

        void setTaskFavorite(Task task);

        void loadPage(int pageIndex);
    }
}
