package ada.osc.myfirstweatherapp.model.city;

import java.io.Serializable;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class City extends RealmObject implements Serializable {
    @PrimaryKey
    private String id = UUID.randomUUID().toString();

    private String cityName;

    public City(){
        cityName = "";
    }

    public City(String name){
        cityName = name;
    }

    public String getName() {
        return cityName;
    }

    public String getId(){
        return id;
    }
}
