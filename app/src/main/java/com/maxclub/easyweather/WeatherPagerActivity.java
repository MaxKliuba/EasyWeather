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
import androidx.recyclerview.widget.SortedList;
import androidx.viewpager.widget.ViewPager;

import com.maxclub.easyweather.database.model.City;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WeatherPagerActivity extends AppCompatActivity {

    private static final String EXTRA_CITY = "com.maxclub.easyweather.extra_city";

    private SortedList<City> mCities;
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

        mCities = new SortedList<>(City.class, new SortedList.Callback<City>() {
            @Override
            public int compare(City o1, City o2) {
                return o1.order - o2.order;
            }

            @Override
            public void onChanged(int position, int count) {
                mViewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public boolean areContentsTheSame(City oldItem, City newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(City item1, City item2) {
                return item1.id == item2.id;
            }

            @Override
            public void onInserted(int position, int count) {
                mViewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onRemoved(int position, int count) {
                mViewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                mViewPager.getAdapter().notifyDataSetChanged();
            }
        });

        City city = (City) getIntent().getParcelableExtra(EXTRA_CITY);

        CityViewModel cityViewModel = new ViewModelProvider(this).get(CityViewModel.class);
        cityViewModel.getCityLiveData().observe(this, new Observer<List<City>>() {
            @Override
            public void onChanged(List<City> cityList) {
                mCities.replaceAll(cityList);
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

                if (city.id == 0) {
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