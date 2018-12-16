package ada.osc.myfirstweatherapp.interaction.citydao;

import java.util.List;

import ada.osc.myfirstweatherapp.model.city.City;

public interface CityDaoInteractor {
    void saveCity(City cityToSave);
    void deleteCity(City cityToDelete);
    List<City> getAllCities();
    City getCityById(String id);
    boolean containsCity(City city);
}
