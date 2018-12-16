package ada.osc.myfirstweatherapp.presentation.allcities;

import android.view.MenuItem;

import ada.osc.myfirstweatherapp.R;
import ada.osc.myfirstweatherapp.contracts.AllCitiesContract;
import ada.osc.myfirstweatherapp.database.CityDaoImpl;
import ada.osc.myfirstweatherapp.interaction.citydao.CityDaoInteractor;

public class AllCitiesPresenter implements AllCitiesContract.Presenter {

    private AllCitiesContract.View allCitiesView;
    private CityDaoInteractor cityDaoInteractor;

    public AllCitiesPresenter(CityDaoInteractor daoInteractor) {
        cityDaoInteractor = daoInteractor;
    }

    @Override
    public void setView(AllCitiesContract.View view) {
        allCitiesView = view;
    }

    @Override
    public void setCitiesWeather() {
        allCitiesView.setAdapterData(cityDaoInteractor.getAllCities());
    }

    @Override
    public void handleNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_new_location:
                allCitiesView.onAddNewLocationClick();
                break;
            case R.id.menu_refresh:
                allCitiesView.onRefreshClick();
                break;
        }
    }

    @Override
    public void handleOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (android.R.id.home):
                allCitiesView.onHomeClick();
                break;
        }
    }

    @Override
    public void updateWeather() {
        allCitiesView.setAdapterData(cityDaoInteractor.getAllCities());
        allCitiesView.showRefreshedToast();
    }
}
