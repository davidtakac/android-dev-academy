package ada.osc.myfirstweatherapp.ui.allcities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.List;

import ada.osc.myfirstweatherapp.App;
import ada.osc.myfirstweatherapp.contracts.AllCitiesContract;
import ada.osc.myfirstweatherapp.model.city.City;
import ada.osc.myfirstweatherapp.presentation.allcities.AllCitiesPresenter;
import ada.osc.myfirstweatherapp.ui.addlocation.AddLocationActivity;
import ada.osc.myfirstweatherapp.R;
import ada.osc.myfirstweatherapp.ui.allcities.adapter.CustomViewPagerFragmentAdapter;
import ada.osc.myfirstweatherapp.util.ToastUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AllCitiesActivity extends AppCompatActivity implements AllCitiesContract.View {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.main_activity_drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.main_activity_navigation_view) NavigationView navigationView;
    @BindView(R.id.main_activity_view_pager) ViewPager viewPager;
    private CustomViewPagerFragmentAdapter adapter;

    private AllCitiesContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allcities);

        ButterKnife.bind(this);
        initUI();
        initToolbar();
        adapter = new CustomViewPagerFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        presenter = new AllCitiesPresenter(App.getCityDaoInteractor());
        presenter.setView(this);

        presenter.setCitiesWeather();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initNavigationDrawer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setCitiesWeather();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.handleOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    public void setAdapterData(List<City> cities){
        adapter.setAdapterData(cities);
    }

    @Override
    public void onAddNewLocationClick() {
        startAddLocationActivity();
        closeDrawer();
    }

    @Override
    public void onHomeClick() {
        openDrawer();
    }

    @Override
    public void onRefreshClick() {
        presenter.updateWeather();
        closeDrawer();
    }


    private void initUI() {
        if (viewPager != null) {
            viewPager.setOffscreenPageLimit(3);
        }
    }

    private void openDrawer(){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void closeDrawer(){
        drawerLayout.closeDrawers();
    }

    private void initNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                presenter.handleNavigationItemSelected(item);
                return false;
            }
        });
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.main_activity_title);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    public void showRefreshedToast(){
        ToastUtil.showToastWithString(this, getString(R.string.string_toastnotif_refreshed));
    }

    private void startAddLocationActivity(){
        startActivity(new Intent(this, AddLocationActivity.class));
    }
}