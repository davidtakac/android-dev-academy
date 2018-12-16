package ada.osc.myfirstweatherapp.contracts;

import ada.osc.myfirstweatherapp.model.city.City;

public interface WeatherContract {
    interface Presenter {
        void setView(View weatherView);
        void setWeatherForCity(City city);
        City getCityById(String cityId);
    }

    interface View {
        void setCurrentTemperatureValues(double temperatureValues);
        void setPressureValues(double pressureValues);
        void setDescriptionValues(String descriptionValues);
        void setMaxTemperatureValues(double maxTemperatureValues);
        void setWeatherIcon(String iconPath);
        void setMinTemperatureValues(double minTemperatureValues);
        void setWindValues(double windValues);

        void showNetworkError();
    }
}
