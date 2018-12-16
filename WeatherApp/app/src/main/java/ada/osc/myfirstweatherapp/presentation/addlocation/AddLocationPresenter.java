package ada.osc.myfirstweatherapp.presentation.addlocation;

import ada.osc.myfirstweatherapp.contracts.AddLocationContract;
import ada.osc.myfirstweatherapp.interaction.citydao.CityDaoInteractor;
import ada.osc.myfirstweatherapp.model.city.City;

public class AddLocationPresenter implements AddLocationContract.Presenter {

    private AddLocationContract.View addLocationView;
    private CityDaoInteractor cityDaoInteractor;

    public AddLocationPresenter(CityDaoInteractor daoInteractor) {
        cityDaoInteractor = daoInteractor;
    }

    @Override
    public void saveCityToDatabase(String cityName) {
        if (cityName.isEmpty()) {
            addLocationView.onEmptyStringRequestError();
            return;
        }

        City city = new City(cityName);

        if (cityDaoInteractor.containsCity(city)) {
            addLocationView.onLocationAlreadyExistsError();
        } else {
            cityDaoInteractor.saveCity(city);
            addLocationView.onSuccess();
        }
    }

    @Override
    public void setView(AddLocationContract.View view) {
        addLocationView = view;
    }
}
