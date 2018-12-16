package ada.osc.myfirstweatherapp.presentation.weather;

import android.util.Log;

import ada.osc.myfirstweatherapp.interaction.citydao.CityDaoInteractor;
import ada.osc.myfirstweatherapp.model.city.City;
import ada.osc.myfirstweatherapp.util.Constants;
import ada.osc.myfirstweatherapp.model.serverresponse.WeatherResponse;
import ada.osc.myfirstweatherapp.interaction.networkapi.ApiInteractor;
import ada.osc.myfirstweatherapp.contracts.WeatherContract;
import ada.osc.myfirstweatherapp.util.ConversionUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherPresenter implements WeatherContract.Presenter {

    private final ApiInteractor apiInteractor;
    private final CityDaoInteractor cityDaoInteractor;

    private WeatherContract.View weatherView;

    public WeatherPresenter(ApiInteractor apiInteractor, CityDaoInteractor daoInteractor) {
        this.apiInteractor = apiInteractor;
        cityDaoInteractor = daoInteractor;
    }

    @Override
    public void setView(WeatherContract.View weatherView) {
        this.weatherView = weatherView;
    }

    @Override
    public void setWeatherForCity(City city) {
        apiInteractor.getWeatherByCityName(city.getName(), weatherInfoCallback(), Constants.APP_ID);
    }

    @Override
    public City getCityById(String cityId) {
        return cityDaoInteractor.getCityById(cityId);
    }

    private Callback<WeatherResponse> weatherInfoCallback() {
        return new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.body() != null) {
                    WeatherResponse data = response.body();

                    Log.d(Constants.LOG_TAG, "Temperature: " + data.getMain().getTemp());
                    setWeatherViewToValuesOf(data);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                weatherView.showNetworkError();
            }
        };
    }

    private void setWeatherViewToValuesOf(WeatherResponse data) {
        weatherView.setPressureValues(data.getMain().getPressure());
        weatherView.setDescriptionValues(data.getWeatherObject().getDescription());
        weatherView.setWindValues(data.getWind().getSpeed());

        weatherView.setCurrentTemperatureValues(
                ConversionUtil.kelvinToCelsius(data.getMain().getTemp())
        );

        weatherView.setMaxTemperatureValues(
                ConversionUtil.kelvinToCelsius(data.getMain().getMaxTemp())
        );
        weatherView.setMinTemperatureValues(
                ConversionUtil.kelvinToCelsius(data.getMain().getMinTemp())
        );

        weatherView.setWeatherIcon(
                getWeatherIconPath(data.getWeatherObject().getMain())
        );
    }

    private String getWeatherIconPath(String description) {
        String iconPath = "";
        if (description != null)
            switch (description) {
                case Constants.SNOW_CASE: {
                    iconPath = Constants.SNOW;
                    break;
                }
                case Constants.RAIN_CASE: {
                    iconPath = Constants.RAIN;
                    break;
                }
                case Constants.CLEAR_CASE: {
                    iconPath = Constants.SUN;
                    break;
                }
                case Constants.MIST_CASE: {
                    iconPath = Constants.FOG;
                    break;
                }
                case Constants.FOG_CASE: {
                    iconPath = Constants.FOG;
                    break;
                }
                case Constants.HAZE_CASE: {
                    iconPath = Constants.FOG;
                    break;
                }

                case Constants.CLOUD_CASE: {
                    iconPath = Constants.CLOUD;
                    break;
                }
            }
        return iconPath;
    }
}
