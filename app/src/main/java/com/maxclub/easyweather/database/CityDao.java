package com.maxclub.easyweather.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.maxclub.easyweather.database.model.City;

import java.util.List;

@Dao
public interface CityDao {
    @Query("SELECT * FROM City")
    List<City> getAll();

    @Query("SELECT * FROM City")
    LiveData<List<City>> getAllLiveData();

    @Query("SELECT * FROM City WHERE id = :id LIMIT 1")
    City findById(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(City city);

    @Update
    void update(City city);

    @Delete
    void delete(City city);
}
