package com.maxclub.easyweather.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastWeatherData implements Parcelable {
    @SerializedName("cnt")
    public int cnt;

    @SerializedName("list")
    public List<ListItem> list;

    @SerializedName("city")
    public City city;

    protected ForecastWeatherData(Parcel in) {
        cnt = in.readInt();
        list = in.createTypedArrayList(ListItem.CREATOR);
        city = in.readParcelable(City.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cnt);
        dest.writeTypedList(list);
        dest.writeParcelable(city, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ForecastWeatherData> CREATOR = new Creator<ForecastWeatherData>() {
        @Override
        public ForecastWeatherData createFromParcel(Parcel in) {
            return new ForecastWeatherData(in);
        }

        @Override
        public ForecastWeatherData[] newArray(int size) {
            return new ForecastWeatherData[size];
        }
    };

    public static class ListItem implements Parcelable {
        @SerializedName("dt")
        public long dt;

        @SerializedName("main")
        public Main main;

        @SerializedName("weather")
        public List<Weather> weather;

        @SerializedName("wind")
        public Wind wind;

        @SerializedName("visibility")
        public int visibility;

        @SerializedName("pop")
        public float pop;

        protected ListItem(Parcel in) {
            dt = in.readLong();
            main = in.readParcelable(Main.class.getClassLoader());
            weather = in.createTypedArrayList(Weather.CREATOR);
            wind = in.readParcelable(Wind.class.getClassLoader());
            visibility = in.readInt();
            pop = in.readFloat();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(dt);
            dest.writeParcelable(main, flags);
            dest.writeTypedList(weather);
            dest.writeParcelable(wind, flags);
            dest.writeInt(visibility);
            dest.writeFloat(pop);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ListItem> CREATOR = new Creator<ListItem>() {
            @Override
            public ListItem createFromParcel(Parcel in) {
                return new ListItem(in);
            }

            @Override
            public ListItem[] newArray(int size) {
                return new ListItem[size];
            }
        };

        public static class Main implements Parcelable {
            @SerializedName("temp")
            public float temp;

            @SerializedName("feels_like")
            public float feelsLike;

            @SerializedName("temp_min")
            public float tempMin;

            @SerializedName("temp_max")
            public float tempMax;

            @SerializedName("pressure")
            public int pressure;

            @SerializedName("sea_level")
            public int sealevel;

            @SerializedName("grnd_level")
            public int groundLevel;

            @SerializedName("humidity")
            public int humidity;

            @SerializedName("temp_kf")
            public float tempKf;

            protected Main(Parcel in) {
                temp = in.readFloat();
                feelsLike = in.readFloat();
                tempMin = in.readFloat();
                tempMax = in.readFloat();
                pressure = in.readInt();
                sealevel = in.readInt();
                groundLevel = in.readInt();
                humidity = in.readInt();
                tempKf = in.readFloat();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeFloat(temp);
                dest.writeFloat(feelsLike);
                dest.writeFloat(tempMin);
                dest.writeFloat(tempMax);
                dest.writeInt(pressure);
                dest.writeInt(sealevel);
                dest.writeInt(groundLevel);
                dest.writeInt(humidity);
                dest.writeFloat(tempKf);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<Main> CREATOR = new Creator<Main>() {
                @Override
                public Main createFromParcel(Parcel in) {
                    return new Main(in);
                }

                @Override
                public Main[] newArray(int size) {
                    return new Main[size];
                }
            };
        }

        public static class Weather implements Parcelable {
            @SerializedName("id")
            public int id;

            @SerializedName("main")
            public String main;

            @SerializedName("description")
            public String description;

            @SerializedName("icon")
            public String icon;

            protected Weather(Parcel in) {
                id = in.readInt();
                main = in.readString();
                description = in.readString();
                icon = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(id);
                dest.writeString(main);
                dest.writeString(description);
                dest.writeString(icon);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<Weather> CREATOR = new Creator<Weather>() {
                @Override
                public Weather createFromParcel(Parcel in) {
                    return new Weather(in);
                }

                @Override
                public Weather[] newArray(int size) {
                    return new Weather[size];
                }
            };
        }

        public static class Wind implements Parcelable {
            @SerializedName("speed")
            public float speed;

            @SerializedName("deg")
            public int deg;

            protected Wind(Parcel in) {
                speed = in.readFloat();
                deg = in.readInt();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeFloat(speed);
                dest.writeInt(deg);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<Wind> CREATOR = new Creator<Wind>() {
                @Override
                public Wind createFromParcel(Parcel in) {
                    return new Wind(in);
                }

                @Override
                public Wind[] newArray(int size) {
                    return new Wind[size];
                }
            };
        }
    }

    public static class City implements Parcelable {
        @SerializedName("id")
        public int id;

        @SerializedName("name")
        public String name;

        @SerializedName("coord")
        public Coord coord;

        @SerializedName("country")
        public String country;

        @SerializedName("population")
        public long population;

        @SerializedName("timezone")
        public int timezone;

        @SerializedName("sunrise")
        public long sunrise;

        @SerializedName("sunset")
        public long sunset;

        protected City(Parcel in) {
            id = in.readInt();
            name = in.readString();
            coord = in.readParcelable(Coord.class.getClassLoader());
            country = in.readString();
            population = in.readLong();
            timezone = in.readInt();
            sunrise = in.readLong();
            sunset = in.readLong();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeParcelable(coord, flags);
            dest.writeString(country);
            dest.writeLong(population);
            dest.writeInt(timezone);
            dest.writeLong(sunrise);
            dest.writeLong(sunset);
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

        public static class Coord implements Parcelable {
            @SerializedName("lat")
            public float lat;

            @SerializedName("lon")
            public float lon;

            protected Coord(Parcel in) {
                lat = in.readFloat();
                lon = in.readFloat();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeFloat(lat);
                dest.writeFloat(lon);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<Coord> CREATOR = new Creator<Coord>() {
                @Override
                public Coord createFromParcel(Parcel in) {
                    return new Coord(in);
                }

                @Override
                public Coord[] newArray(int size) {
                    return new Coord[size];
                }
            };
        }
    }
}