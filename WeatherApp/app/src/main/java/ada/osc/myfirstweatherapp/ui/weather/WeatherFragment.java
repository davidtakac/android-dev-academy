package ada.osc.myfirstweatherapp.ui.weather;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import ada.osc.myfirstweatherapp.App;
import ada.osc.myfirstweatherapp.contracts.WeatherContract;
import ada.osc.myfirstweatherapp.model.city.City;
import ada.osc.myfirstweatherapp.util.Constants;
import ada.osc.myfirstweatherapp.presentation.weather.WeatherPresenter;
import ada.osc.myfirstweatherapp.util.NetworkUtils;
import ada.osc.myfirstweatherapp.R;
import ada.osc.myfirstweatherapp.util.ToastUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Filip on 26/03/2016.
 */
public class WeatherFragment extends Fragment implements WeatherContract.View, Target {

    @BindView(R.id.weather_display_current_temperature_text_view)
    TextView currentTemperature;
    @BindView(R.id.weather_fragment_min_temperature_text_view)
    TextView minTemperature;
    @BindView(R.id.weather_fragment_max_temperature_text_view)
    TextView maxTemperature;
    @BindView(R.id.weather_display_pressure_text_view)
    TextView pressure;
    @BindView(R.id.weather_display_wind_text_view)
    TextView wind;
    @BindView(R.id.weather_display_detailed_description_text_view)
    TextView description;
    @BindView(R.id.weather_display_weather_icon_image_view)
    ImageView weatherIcon;

    private WeatherContract.Presenter presenter;

    public static WeatherFragment newInstance(City city) {
        Bundle data = new Bundle();
        data.putString(Constants.CITYID_BUNDLE_KEY, city.getId());
        WeatherFragment f = new WeatherFragment();
        f.setArguments(data);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        presenter = new WeatherPresenter(App.getApiInteractor(), App.getCityDaoInteractor());
        presenter.setView(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.setWeatherForCity(getAssociatedCity());
    }

    @Override
    public void setCurrentTemperatureValues(double temperatureValues) {
        currentTemperature.setText(getString(R.string.current_temperature_message, temperatureValues));
    }

    @Override
    public void setMinTemperatureValues(double minTemperatureValues) {
        minTemperature.setText(getString(R.string.minimum_temperature_message, minTemperatureValues));
    }

    @Override
    public void setMaxTemperatureValues(double maxTemperatureValues) {
        maxTemperature.setText(getString(R.string.maximum_temperature_message, maxTemperatureValues));
    }

    @Override
    public void setPressureValues(double pressureValues) {
        pressure.setText(getString(R.string.pressure_message, pressureValues));

    }

    @Override
    public void setWindValues(double windValues) {
        wind.setText(getString(R.string.wind_speed_message, windValues));
    }

    @Override
    public void setWeatherIcon(String iconPath) {
        //Glide.with(getContext()).load(Constants.IMAGE_BASE_URL + iconPath).into(weatherIcon);
        Picasso.get().load(Constants.IMAGE_BASE_URL + iconPath).into(weatherIcon);
    }

    @Override
    public void setDescriptionValues(String descriptionValues) {
        description.setText(descriptionValues);
    }

    @Override
    public void showNetworkError() {
        ToastUtil.showToastWithString(getContext(), getString(R.string.weather_fragment_loading_failure));
    }

    private void refreshCurrentData() {
        if (NetworkUtils.isConnectedToInternet(getActivity())) {
            presenter.setWeatherForCity(getAssociatedCity());
        }
    }

    private City getAssociatedCity(){
        return presenter.getCityById(getArguments().getString(Constants.CITYID_BUNDLE_KEY));
    }

    //for picasso
    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        weatherIcon.setImageDrawable(new BitmapDrawable(getContext().getResources(), bitmap));
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
        weatherIcon.setImageDrawable(errorDrawable);
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        weatherIcon.setImageDrawable(placeHolderDrawable);
    }
}
