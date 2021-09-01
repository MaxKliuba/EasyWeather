package com.maxclub.easyweather.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.maxclub.easyweather.database.model.City;

@Database(entities = {City.class}, version = 1, exportSchema = false)
public abstract class CityDatabase extends RoomDatabase {
    public abstract CityDao cityDao();
}
