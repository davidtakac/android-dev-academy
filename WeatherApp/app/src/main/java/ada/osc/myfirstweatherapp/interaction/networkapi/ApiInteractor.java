package ada.osc.myfirstweatherapp.interaction.networkapi;

import ada.osc.myfirstweatherapp.model.serverresponse.WeatherResponse;
import retrofit2.Callback;

public interface ApiInteractor {
    void getWeatherByCityName(String cityName, Callback<WeatherResponse> weatherResponseCallback, String appId);
}
