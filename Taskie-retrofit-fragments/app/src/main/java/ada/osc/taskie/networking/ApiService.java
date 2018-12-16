package ada.osc.taskie.networking;

import ada.osc.taskie.model.LoginResponse;
import ada.osc.taskie.model.RegistrationToken;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/register/")
    Call<RegistrationToken> registerUser(@Body RegistrationToken registrationToken);

    @POST("api/login/")
    Call<LoginResponse> loginUser(@Body RegistrationToken registrationToken);

    @POST("api/note/")
    Call<Task> postNewTask(@Header("authorization") String header, @Body Task task);

    @GET("api/note/")
    Call<TaskList> getTasks(@Header("authorization") String header);

    @GET("api/note/favorite")
    Call<TaskList> getFavoriteTasks(@Header("authorization") String token);

    @GET("api/note/{id}")
    Call<Task> getTask(@Header("authorization") String header, @Path("id") String taskId);

    @POST("api/note/edit")
    Call<Task> updateTask(@Header("authorization") String header, @Body Task taskToEdit);

    @POST("api/note/favorite")
    Call<Task> toggleTaskFavorite(@Header("authorization") String header, @Query("id") String taskToFavoriteId);

    @POST("api/note/delete")
    Call<Task> deleteTask(@Header("authorization") String header, @Query("id") String taskToDeleteId);

    @GET("api/note")
    Call<TaskList> getPageOfTasks(@Header("authorization") String header, @Query("page") int pageIndex);

    @GET("api/note/favorite")
    Call<TaskList> getPageOfFavoriteTasks(@Header("authorization") String header, @Query("page") int pageIndex);
}
