package ada.osc.taskie.ui.tasks.all;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ada.osc.taskie.App;
import ada.osc.taskie.R;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskList;
import ada.osc.taskie.networking.ApiService;
import ada.osc.taskie.networking.RetrofitUtil;
import ada.osc.taskie.presentation.AllTasksPresenter;
import ada.osc.taskie.ui.addTask.NewTaskActivity;
import ada.osc.taskie.ui.tasks.TasksActivity;
import ada.osc.taskie.util.Constants;
import ada.osc.taskie.util.InternetUtil;
import ada.osc.taskie.util.SharedPrefsUtil;
import ada.osc.taskie.ui.tasks.adapter.TaskAdapter;
import ada.osc.taskie.listener.TaskClickListener;
import ada.osc.taskie.util.ToastUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AllTasksFragment extends Fragment implements AllTasksContract.View, TaskClickListener {

    @BindView(R.id.tasks)
    RecyclerView tasks;

    private TaskAdapter taskAdapter;
    private AllTasksContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        presenter = new AllTasksPresenter(App.getApiInteractor(), App.getPreferences());
        presenter.setView(this);

        taskAdapter = new TaskAdapter(this);
        taskAdapter.setLoadMoreListener(new TaskAdapter.LoadMoreListener() {
            @Override
            public void loadMoreTasks() {
                //because pagination starts from 1
                int pageIndex = 1 + taskAdapter.getItemCount() / Constants.PAGE_SIZE;
                Log.d("david", "page index: " + pageIndex);
                presenter.loadPage(pageIndex);
            }
        });

        tasks.setLayoutManager(new LinearLayoutManager(getActivity()));
        tasks.setItemAnimator(new DefaultItemAnimator());
        tasks.setAdapter(taskAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        //presenter.getFavoriteTasks();
        Log.d("david", "all fragment loading page 1");
        if (InternetUtil.isConnectedToInternet(getActivity())) {
            presenter.loadPage(1);
        } else {
            showNoConnectionError();
        }
    }

    @Override
    public void showTasks(List<Task> tasks) {
        taskAdapter.updateTasks(tasks);
        taskAdapter.setMoreDataAvailable(tasks.size() >= Constants.PAGE_SIZE);
    }

    @Override
    public void showMoreTasks(List<Task> tasks) {
        taskAdapter.addTasks(tasks);
        taskAdapter.setMoreDataAvailable(tasks.size() >= Constants.PAGE_SIZE);
    }

    @Override
    public void onTaskRemoved(String taskId) {
        taskAdapter.removeTask(taskId);
    }

    @Override
    public void onClick(Task task) {
        if (InternetUtil.isConnectedToInternet(getActivity())) {
            startEditActivity(task);
        } else {
            showNoConnectionError();
        }
    }

    @Override
    public void onLongClick(Task task) {
        if (InternetUtil.isConnectedToInternet(getActivity())) {
            presenter.deleteTask(task);
        } else {
            showNoConnectionError();
        }
    }

    @Override
    public void onFavoriteStateChanged(Task task) {
        if (InternetUtil.isConnectedToInternet(getActivity())) {
            presenter.setTaskFavorite(task);
        } else {
            showNoConnectionError();
        }
    }

    private void startEditActivity(Task task) {
        Intent intent = new Intent(getActivity(), NewTaskActivity.class);
        intent.putExtra(TasksActivity.EXTRA_TASKTOEDIT, task.getmId());
        startActivity(intent);
    }

    @Override
    public void showNetworkError() {
        ToastUtil.shortToastWithString(getActivity(), getString(R.string.errormsg_networkError));
    }

    private void showNoConnectionError() {
        ToastUtil.shortToastWithString(getActivity(), getString(R.string.errormsg_noConnection));
    }
}
