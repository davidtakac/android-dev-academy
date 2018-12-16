package ada.osc.myfirstweatherapp.contracts;

import ada.osc.myfirstweatherapp.model.city.City;

public interface AddLocationContract {
    interface View{
        void onSuccess();
        void onLocationAlreadyExistsError();
        void onEmptyStringRequestError();
    }

    interface Presenter{
        void saveCityToDatabase(String cityName);
        void setView(AddLocationContract.View view);
    }
}
