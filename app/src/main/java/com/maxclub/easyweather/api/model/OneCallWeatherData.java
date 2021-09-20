package com.maxclub.easyweather.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OneCallWeatherData implements Parcelable {
    @SerializedName("lat")
    public double lat;

    @SerializedName("lon")
    public double lon;

    @SerializedName("timezone")
    public String timezone;

    @SerializedName("timezone_offset")
    public int timezoneOffset;

    @SerializedName("current")
    public Current current;

    @SerializedName("hourly")
    public List<Hourly> hourly;

    @SerializedName("daily")
    public List<Daily> daily;

    protected OneCallWeatherData(Parcel in) {
        lat = in.readDouble();
        lon = in.readDouble();
        timezone = in.readString();
        timezoneOffset = in.readInt();
        current = in.readParcelable(Current.class.getClassLoader());
        hourly = in.createTypedArrayList(Hourly.CREATOR);
        daily = in.createTypedArrayList(Daily.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeString(timezone);
        dest.writeInt(timezoneOffset);
        dest.writeParcelable(current, flags);
        dest.writeTypedList(hourly);
        dest.writeTypedList(daily);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OneCallWeatherData> CREATOR = new Creator<OneCallWeatherData>() {
        @Override
        public OneCallWeatherData createFromParcel(Parcel in) {
            return new OneCallWeatherData(in);
        }

        @Override
        public OneCallWeatherData[] newArray(int size) {
            return new OneCallWeatherData[size];
        }
    };

    public static class Current implements Parcelable {
        @SerializedName("dt")
        public long dt;

        @SerializedName("sunrise")
        public long sunrise;

        @SerializedName("sunset")
        public long sunset;

        @SerializedName("temp")
        public float temp;

        @SerializedName("feels_like")
        public float feelsLike;

        @SerializedName("pressure")
        public int pressure;

        @SerializedName("humidity")
        public int humidity;

        @SerializedName("dew_point")
        public float dewPoint;

        @SerializedName("uvi")
        public float uvi;

        @SerializedName("clouds")
        public int clouds;

        @SerializedName("visibility")
        public int visibility;

        @SerializedName("wind_speed")
        public float windSpeed;

        @SerializedName("wind_deg")
        public int windDeg;

        @SerializedName("weather")
        public List<Weather> weather;

        protected Current(Parcel in) {
            dt = in.readLong();
            sunrise = in.readLong();
            sunset = in.readLong();
            temp = in.readFloat();
            feelsLike = in.readFloat();
            pressure = in.readInt();
            humidity = in.readInt();
            dewPoint = in.readFloat();
            uvi = in.readFloat();
            clouds = in.readInt();
            visibility = in.readInt();
            windSpeed = in.readFloat();
            windDeg = in.readInt();
            weather = in.createTypedArrayList(Weather.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(dt);
            dest.writeLong(sunrise);
            dest.writeLong(sunset);
            dest.writeFloat(temp);
            dest.writeFloat(feelsLike);
            dest.writeInt(pressure);
            dest.writeInt(humidity);
            dest.writeFloat(dewPoint);
            dest.writeFloat(uvi);
            dest.writeInt(clouds);
            dest.writeInt(visibility);
            dest.writeFloat(windSpeed);
            dest.writeInt(windDeg);
            dest.writeTypedList(weather);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Current> CREATOR = new Creator<Current>() {
            @Override
            public Current createFromParcel(Parcel in) {
                return new Current(in);
            }

            @Override
            public Current[] newArray(int size) {
                return new Current[size];
            }
        };
    }

    public static class Hourly implements Parcelable {
        @SerializedName("dt")
        public long dt;

        @SerializedName("temp")
        public float temp;

        @SerializedName("feels_like")
        public float feelsLike;

        @SerializedName("pressure")
        public int pressure;

        @SerializedName("humidity")
        public int humidity;

        @SerializedName("dew_point")
        public float dewPoint;

        @SerializedName("uvi")
        public float uvi;

        @SerializedName("clouds")
        public int clouds;

        @SerializedName("visibility")
        public int visibility;

        @SerializedName("wind_speed")
        public float windSpeed;

        @SerializedName("wind_deg")
        public int windDeg;

        @SerializedName("weather")
        public List<Weather> weather;

        @SerializedName("pop")
        public float pop;

        protected Hourly(Parcel in) {
            dt = in.readLong();
            temp = in.readFloat();
            feelsLike = in.readFloat();
            pressure = in.readInt();
            humidity = in.readInt();
            dewPoint = in.readFloat();
            uvi = in.readFloat();
            clouds = in.readInt();
            visibility = in.readInt();
            windSpeed = in.readFloat();
            windDeg = in.readInt();
            weather = in.createTypedArrayList(Weather.CREATOR);
            pop = in.readFloat();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(dt);
            dest.writeFloat(temp);
            dest.writeFloat(feelsLike);
            dest.writeInt(pressure);
            dest.writeInt(humidity);
            dest.writeFloat(dewPoint);
            dest.writeFloat(uvi);
            dest.writeInt(clouds);
            dest.writeInt(visibility);
            dest.writeFloat(windSpeed);
            dest.writeInt(windDeg);
            dest.writeTypedList(weather);
            dest.writeFloat(pop);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Hourly> CREATOR = new Creator<Hourly>() {
            @Override
            public Hourly createFromParcel(Parcel in) {
                return new Hourly(in);
            }

            @Override
            public Hourly[] newArray(int size) {
                return new Hourly[size];
            }
        };
    }

    public static class Daily implements Parcelable {
        @SerializedName("dt")
        public long dt;

        @SerializedName("sunrise")
        public long sunrise;

        @SerializedName("sunset")
        public long sunset;

        @SerializedName("moonrise")
        public long moonrise;

        @SerializedName("moonset")
        public long moonset;

        @SerializedName("moon_phase")
        public float moonPhase;

        @SerializedName("temp")
        public Temp temp;

        @SerializedName("feels_like")
        public FeelsLike feelsLike;

        @SerializedName("pressure")
        public int pressure;

        @SerializedName("humidity")
        public int humidity;

        @SerializedName("dew_point")
        public float dewPoint;

        @SerializedName("wind_speed")
        public float windSpeed;

        @SerializedName("wind_deg")
        public int windDeg;

        @SerializedName("weather")
        public List<Weather> weather;

        @SerializedName("clouds")
        public int clouds;

        @SerializedName("pop")
        public float pop;

        @SerializedName("uvi")
        public float uvi;

        protected Daily(Parcel in) {
            dt = in.readLong();
            sunrise = in.readLong();
            sunset = in.readLong();
            moonrise = in.readLong();
            moonset = in.readLong();
            moonPhase = in.readFloat();
            temp = in.readParcelable(Temp.class.getClassLoader());
            feelsLike = in.readParcelable(FeelsLike.class.getClassLoader());
            pressure = in.readInt();
            humidity = in.readInt();
            dewPoint = in.readFloat();
            windSpeed = in.readFloat();
            windDeg = in.readInt();
            weather = in.createTypedArrayList(Weather.CREATOR);
            clouds = in.readInt();
            pop = in.readFloat();
            uvi = in.readFloat();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(dt);
            dest.writeLong(sunrise);
            dest.writeLong(sunset);
            dest.writeLong(moonrise);
            dest.writeLong(moonset);
            dest.writeFloat(moonPhase);
            dest.writeParcelable(temp, flags);
            dest.writeParcelable(feelsLike, flags);
            dest.writeInt(pressure);
            dest.writeInt(humidity);
            dest.writeFloat(dewPoint);
            dest.writeFloat(windSpeed);
            dest.writeInt(windDeg);
            dest.writeTypedList(weather);
            dest.writeInt(clouds);
            dest.writeFloat(pop);
            dest.writeFloat(uvi);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Daily> CREATOR = new Creator<Daily>() {
            @Override
            public Daily createFromParcel(Parcel in) {
                return new Daily(in);
            }

            @Override
            public Daily[] newArray(int size) {
                return new Daily[size];
            }
        };

        public static class Temp implements Parcelable {
            @SerializedName("day")
            public float day;

            @SerializedName("min")
            public float min;

            @SerializedName("max")
            public float max;

            @SerializedName("night")
            public float night;

            @SerializedName("eve")
            public float eve;

            @SerializedName("morn")
            public float morn;

            protected Temp(Parcel in) {
                day = in.readFloat();
                min = in.readFloat();
                max = in.readFloat();
                night = in.readFloat();
                eve = in.readFloat();
                morn = in.readFloat();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeFloat(day);
                dest.writeFloat(min);
                dest.writeFloat(max);
                dest.writeFloat(night);
                dest.writeFloat(eve);
                dest.writeFloat(morn);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<Temp> CREATOR = new Creator<Temp>() {
                @Override
                public Temp createFromParcel(Parcel in) {
                    return new Temp(in);
                }

                @Override
                public Temp[] newArray(int size) {
                    return new Temp[size];
                }
            };
        }

        public static class FeelsLike implements Parcelable {
            @SerializedName("day")
            public float day;

            @SerializedName("night")
            public float night;

            @SerializedName("eve")
            public float eve;

            @SerializedName("morn")
            public float morn;

            protected FeelsLike(Parcel in) {
                day = in.readFloat();
                night = in.readFloat();
                eve = in.readFloat();
                morn = in.readFloat();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeFloat(day);
                dest.writeFloat(night);
                dest.writeFloat(eve);
                dest.writeFloat(morn);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<FeelsLike> CREATOR = new Creator<FeelsLike>() {
                @Override
                public FeelsLike createFromParcel(Parcel in) {
                    return new FeelsLike(in);
                }

                @Override
                public FeelsLike[] newArray(int size) {
                    return new FeelsLike[size];
                }
            };
        }
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
}
