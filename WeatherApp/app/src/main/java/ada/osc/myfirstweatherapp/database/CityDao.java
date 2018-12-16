package ada.osc.myfirstweatherapp.database;

import java.util.List;

import ada.osc.myfirstweatherapp.model.city.City;

public interface CityDao {
    void saveCity(City cityToSave);
    void deleteCity(City cityToDelete);
    List<City> getAllCities();
    City getCityById(String id);
    boolean containsCity(City city);
}
