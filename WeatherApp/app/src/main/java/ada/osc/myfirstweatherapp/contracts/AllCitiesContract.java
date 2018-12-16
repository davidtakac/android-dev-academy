package ada.osc.myfirstweatherapp.contracts;

import android.view.MenuItem;

import java.util.List;

import ada.osc.myfirstweatherapp.model.city.City;

public interface AllCitiesContract {
    interface View{
        void setAdapterData(List<City> cities);
        void onAddNewLocationClick();
        void onHomeClick();
        void onRefreshClick();
        void showRefreshedToast();
    }

    interface Presenter{
        void setView(AllCitiesContract.View view);
        void setCitiesWeather();
        void handleNavigationItemSelected(MenuItem item);
        void handleOptionsItemSelected(MenuItem item);
        void updateWeather();
    }
}
