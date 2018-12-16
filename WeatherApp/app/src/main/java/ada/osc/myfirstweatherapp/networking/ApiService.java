package ada.osc.myfirstweatherapp.networking;

import ada.osc.myfirstweatherapp.util.Constants;
import ada.osc.myfirstweatherapp.model.serverresponse.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/data/2.5/weather")
    Call<WeatherResponse> getWeatherByCityName(@Query("q") String city, @Query(Constants.APP_ID_KEY) String apiKey);

    /*@GET("/data/2.5/weather")
    Call<WeatherResponse> getWeatherByCityName(@Query("appid") String apiKey, @Query("q") String city);*/
}
