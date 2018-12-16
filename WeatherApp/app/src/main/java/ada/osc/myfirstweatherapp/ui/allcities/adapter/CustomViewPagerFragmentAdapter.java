package ada.osc.myfirstweatherapp.ui.allcities.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ada.osc.myfirstweatherapp.model.city.City;
import ada.osc.myfirstweatherapp.ui.weather.WeatherFragment;

public class CustomViewPagerFragmentAdapter extends FragmentPagerAdapter {
    private final List<City> mCitiesList = new ArrayList<>();

    public CustomViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return WeatherFragment.newInstance(mCitiesList.get(position));
    }

    @Override
    public int getCount() {
        return mCitiesList.size();
    }

    public void setAdapterData(List<City> dataSource) {
        this.mCitiesList.clear();
        this.mCitiesList.addAll(dataSource);
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mCitiesList.get(position).getName();
    }
}