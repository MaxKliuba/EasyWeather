package com.maxclub.easyweather;

import android.app.Application;

import androidx.room.Room;

import com.maxclub.easyweather.database.CityDao;
import com.maxclub.easyweather.database.CityDatabase;

public class App extends Application {

    private static App sApp;

    private CityDatabase mCityDatabase;
    private CityDao mCityDao;

    @Override
    public void onCreate() {
        super.onCreate();

        sApp = this;

        mCityDatabase = Room.databaseBuilder(getApplicationContext(), CityDatabase.class, "city-db")
                .allowMainThreadQueries()
                .build();

        mCityDao = mCityDatabase.cityDao();
    }

    public static App getInstance() {
        return sApp;
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
