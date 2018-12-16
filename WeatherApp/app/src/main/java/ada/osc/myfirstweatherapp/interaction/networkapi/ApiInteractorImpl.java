package ada.osc.myfirstweatherapp.interaction.networkapi;

import ada.osc.myfirstweatherapp.model.serverresponse.WeatherResponse;
import ada.osc.myfirstweatherapp.networking.ApiService;
import retrofit2.Callback;

public class ApiInteractorImpl implements ApiInteractor {

    private final ApiService apiService;

    public ApiInteractorImpl(ApiService apiService){
        this.apiService = apiService;
    }

    @Override
    public void getWeatherByCityName(String cityName, Callback<WeatherResponse> weatherResponseCallback, String appId) {
        apiService.getWeatherByCityName(cityName, appId).enqueue(weatherResponseCallback);
    }
}
