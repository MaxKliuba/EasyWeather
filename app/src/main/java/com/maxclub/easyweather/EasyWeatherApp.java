package com.maxclub.easyweather;

import android.app.Application;

import androidx.room.Room;

import com.maxclub.easyweather.database.CityDao;
import com.maxclub.easyweather.database.CityDatabase;

public class EasyWeatherApp extends Application {

    private static EasyWeatherApp sEasyWeatherApp;

    private CityDatabase mCityDatabase;
    private CityDao mCityDao;

    @Override
    public void onCreate() {
        super.onCreate();

        sEasyWeatherApp = this;

        mCityDatabase = Room.databaseBuilder(getApplicationContext(), CityDatabase.class, "city-db")
                .allowMainThreadQueries()
                .build();

        mCityDao = mCityDatabase.cityDao();
    }

    public static EasyWeatherApp getInstance() {
        return sEasyWeatherApp;
    }

    public CityDatabase getCityDatabase() {
        return mCityDatabase;
    }

    public void setCityDatabase(CityDatabase cityDatabase) {
        mCityDatabase = cityDatabase;
    }

    public CityDao getCityDao() {
        return mCityDao;
    }

    public void setCityDao(CityDao cityDao) {
        mCityDao = cityDao;
    }
}
