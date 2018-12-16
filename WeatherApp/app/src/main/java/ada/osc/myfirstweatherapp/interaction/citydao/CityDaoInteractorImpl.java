package ada.osc.myfirstweatherapp.interaction.citydao;

import java.util.List;

import ada.osc.myfirstweatherapp.database.CityDao;
import ada.osc.myfirstweatherapp.model.city.City;

public class CityDaoInteractorImpl implements CityDaoInteractor {

    private CityDao cityDao;

    public CityDaoInteractorImpl(CityDao dao){
        cityDao = dao;
    }

    @Override
    public void saveCity(City cityToSave) {
        cityDao.saveCity(cityToSave);
    }

    @Override
    public void deleteCity(City cityToDelete) {
        cityDao.deleteCity(cityToDelete);
    }

    @Override
    public List<City> getAllCities() {
        return cityDao.getAllCities();
    }

    @Override
    public City getCityById(String cityId) {
        return cityDao.getCityById(cityId);
    }

    @Override
    public boolean containsCity(City city) {
        return cityDao.containsCity(city);
    }
}
