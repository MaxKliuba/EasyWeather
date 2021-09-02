package com.maxclub.easyweather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.maxclub.easyweather.database.model.City;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WeatherPagerActivity extends AppCompatActivity {

    private static final String EXTRA_CITY = "com.maxclub.easyweather.extra_city";

    private City mCurrentLocationCity;
    private List<City> mCities = new ArrayList<>();
    private ViewPager mViewPager;

    public static Intent newIntent(Context context, City city) {
        Intent intent = new Intent(context, WeatherPagerActivity.class);
        intent.putExtra(EXTRA_CITY, city);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_pager);

        City city = (City) getIntent().getParcelableExtra(EXTRA_CITY);

        CityViewModel cityViewModel = new ViewModelProvider(this).get(CityViewModel.class);
        mCurrentLocationCity = cityViewModel.getCurrentLocationCity();
        cityViewModel.getCityLiveData().observe(this, new Observer<List<City>>() {
            @Override
            public void onChanged(List<City> cityList) {
                mCities = new ArrayList<>(cityList);
                mCities.add(0, mCurrentLocationCity);
                mViewPager.getAdapter().notifyDataSetChanged();

                for (int i = 0; i < mCities.size(); i++) {
                    if (mCities.get(i).equals(city)) {
                        mViewPager.setCurrentItem(i);
                        break;
                    }
                }
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();

        mViewPager = (ViewPager) findViewById(R.id.weather_view_pager);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @NonNull
            @NotNull
            @Override
            public Fragment getItem(int position) {
                City city = mCities.get(position);

                if (city.equals(mCurrentLocationCity)) {
                    return LocationWeatherFragment.newInstance();
                } else {
                    return CityWeatherFragment.newInstance(city);
                }
            }

            @Override
            public int getCount() {
                return mCities.size();
            }
        });
    }
}