package com.maxclub.easyweather.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "City", indices = {@Index(value = {"id"}, unique = true)})
public class City implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "order")
    public int order;

    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "country")
    public String country;

    @ColumnInfo(name = "lat")
    public double lat;

    @ColumnInfo(name = "lon")
    public double lon;

    public City() {

    }

    protected City(Parcel in) {
        order = in.readInt();
        id = in.readInt();
        name = in.readString();
        country = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        if (order != city.order) return false;
        if (id != city.id) return false;
        if (Double.compare(city.lat, lat) != 0) return false;
        if (Double.compare(city.lon, lon) != 0) return false;
        if (name != null ? !name.equals(city.name) : city.name != null) return false;
        return country != null ? country.equals(city.country) : city.country == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = order;
        result = 31 * result + id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lon);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(order);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(country);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };
}
