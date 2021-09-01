package com.maxclub.easyweather.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "City")
public class City {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "country")
    public String country;

    public City() {

    }

    public City(int id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }
}
