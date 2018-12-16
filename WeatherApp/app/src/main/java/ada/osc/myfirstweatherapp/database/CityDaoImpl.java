package ada.osc.myfirstweatherapp.database;

import java.util.List;

import ada.osc.myfirstweatherapp.model.city.City;
import io.realm.Realm;

public class CityDaoImpl implements CityDao {

    private Realm db;

    public CityDaoImpl(Realm database){
        db = database;
    }

    public void saveCity(City cityToSave) {
        db.beginTransaction();
        db.copyToRealm(cityToSave);
        db.commitTransaction();
    }


    public void deleteCity(City cityToDelete) {
        cityToDelete.deleteFromRealm();
    }


    public List<City> getAllCities() {
        return db.where(City.class).findAll();
    }


    public City getCityById(String id) {
        return db.where(City.class).equalTo("id", id).findFirst();
    }

    public boolean containsCity(City city){
        City c = db.where(City.class).equalTo("cityName", city.getName()).findFirst();
        return c != null;
    }
}
