package ada.osc.taskie.interaction;

import ada.osc.taskie.model.LoginResponse;
import ada.osc.taskie.model.RegistrationToken;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskList;
import ada.osc.taskie.networking.ApiService;
import retrofit2.Callback;

public class ApiInteractorImpl implements ApiInteractor {

    private final ApiService apiService;

    public ApiInteractorImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void registerUser(RegistrationToken registerRequest, Callback<RegistrationToken> callback) {
        apiService.registerUser(registerRequest).enqueue(callback);
    }

    @Override
    public void loginUser(RegistrationToken loginRequest, Callback<LoginResponse> callback) {
        apiService.loginUser(loginRequest).enqueue(callback);
    }

    @Override
    public void getTasks(Callback<TaskList> callback, String token) {
        apiService.getTasks(token).enqueue(callback);
    }

    @Override
    public void getFavoriteTasks(Callback<TaskList> callback, String token) {
        apiService.getFavoriteTasks(token).enqueue(callback);
    }

    @Override
    public void addNewTask(Task task, Callback<Task> callback, String token) {
        apiService.postNewTask(token, task).enqueue(callback);
    }

    @Override
    public void getTask(String taskId, Callback<Task> callback, String token) {
        apiService.getTask(token, taskId).enqueue(callback);
    }

    @Override
    public void updateTask(Task task, Callback<Task> callback, String token) {
        apiService.updateTask(token, task).enqueue(callback);
    }

    @Override
    public void toggleTaskFavorite(String taskId, Callback<Task> callback, String token) {
        apiService.toggleTaskFavorite(token, taskId).enqueue(callback);
    }

    @Override
    public void deleteTask(String taskId, Callback<Task> callback, String token) {
        apiService.deleteTask(token, taskId).enqueue(callback);
    }

    @Override
    public void getPageOfTasks(int pageIndex, Callback<TaskList> callback, String token) {
        apiService.getPageOfTasks(token, pageIndex).enqueue(callback);
    }

    @Override
    public void getPageOfFavoriteTasks(int pageIndex, Callback<TaskList> callback, String token) {
        apiService.getPageOfFavoriteTasks(token, pageIndex).enqueue(callback);
    }
}
