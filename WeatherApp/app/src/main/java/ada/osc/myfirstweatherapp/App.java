package ada.osc.myfirstweatherapp;

import android.app.Application;

import ada.osc.myfirstweatherapp.database.CityDaoImpl;
import ada.osc.myfirstweatherapp.interaction.citydao.CityDaoInteractor;
import ada.osc.myfirstweatherapp.interaction.citydao.CityDaoInteractorImpl;
import ada.osc.myfirstweatherapp.interaction.networkapi.ApiInteractor;
import ada.osc.myfirstweatherapp.interaction.networkapi.ApiInteractorImpl;
import ada.osc.myfirstweatherapp.networking.ApiService;
import ada.osc.myfirstweatherapp.networking.RetrofitUtil;
import ada.osc.myfirstweatherapp.util.Constants;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Retrofit;

/**
 * Created by Filip on 01/04/2016.
 */
public class App extends Application {

    private static ApiInteractor apiInteractor;
    private static CityDaoInteractor cityDaoInteractor;

    @Override
    public void onCreate() {
        super.onCreate();

        final Retrofit retrofit = RetrofitUtil.createRetrofit();
        final ApiService apiService = retrofit.create(ApiService.class);
        initRealm();

        apiInteractor = new ApiInteractorImpl(apiService);
        cityDaoInteractor = new CityDaoInteractorImpl(new CityDaoImpl(Realm.getDefaultInstance()));
    }

    public static ApiInteractor getApiInteractor() {
        return apiInteractor;
    }

    public static CityDaoInteractor getCityDaoInteractor(){
        return cityDaoInteractor;
    }

    private void initRealm(){
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Constants.REALM_APP_NAME)
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
