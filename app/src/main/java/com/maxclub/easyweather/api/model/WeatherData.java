package com.maxclub.easyweather.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherData implements Parcelable {
    @SerializedName("cnt")
    private int cnt;

    @SerializedName("list")
    List<ListItem> list;

    @SerializedName("city")
    private City city;

    protected WeatherData(Parcel in) {
        cnt = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cnt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WeatherData> CREATOR = new Creator<WeatherData>() {
        @Override
        public WeatherData createFromParcel(Parcel in) {
            return new WeatherData(in);
        }

        @Override
        public WeatherData[] newArray(int size) {
            return new WeatherData[size];
        }
    };

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<ListItem> getList() {
        return list;
    }

    public void setList(List<ListItem> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public static class ListItem implements Parcelable {
        @SerializedName("dt")
        private long dt;

        @SerializedName("main")
        private Main main;

        @SerializedName("weather")
        private List<Weather> weather;

        @SerializedName("wind")
        private Wind wind;

        @SerializedName("visibility")
        private int visibility;

        protected ListItem(Parcel in) {
            dt = in.readLong();
            visibility = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(dt);
            dest.writeInt(visibility);
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

        public long getDt() {
            return dt;
        }

        public void setDt(long dt) {
            this.dt = dt;
        }

        public Main getMain() {
            return main;
        }

        public void setMain(Main main) {
            this.main = main;
        }

        public List<Weather> getWeather() {
            return weather;
        }

        public void setWeather(List<Weather> weather) {
            this.weather = weather;
        }

        public Wind getWind() {
            return wind;
        }

        public void setWind(Wind wind) {
            this.wind = wind;
        }

        public int getVisibility() {
            return visibility;
        }

        public void setVisibility(int visibility) {
            this.visibility = visibility;
        }

        public static class Main implements Parcelable {
            @SerializedName("temp")
            private float temp;

            @SerializedName("feels_like")
            private float feelsLike;

            @SerializedName("temp_min")
            private float tempMin;

            @SerializedName("temp_max")
            private float tempMax;

            @SerializedName("pressure")
            private int pressure;

            @SerializedName("sea_level")
            private int sealevel;

            @SerializedName("grnd_level")
            private int groundLevel;

            @SerializedName("humidity")
            private int humidity;

            protected Main(Parcel in) {
                temp = in.readFloat();
                feelsLike = in.readFloat();
                tempMin = in.readFloat();
                tempMax = in.readFloat();
                pressure = in.readInt();
                sealevel = in.readInt();
                groundLevel = in.readInt();
                humidity = in.readInt();
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

            public float getTemp() {
                return temp;
            }

            public void setTemp(float temp) {
                this.temp = temp;
            }

            public float getFeelsLike() {
                return feelsLike;
            }

            public void setFeelsLike(float feelsLike) {
                this.feelsLike = feelsLike;
            }

            public float getTempMin() {
                return tempMin;
            }

            public void setTempMin(float tempMin) {
                this.tempMin = tempMin;
            }

            public float getTempMax() {
                return tempMax;
            }

            public void setTempMax(float tempMax) {
                this.tempMax = tempMax;
            }

            public int getPressure() {
                return pressure;
            }

            public void setPressure(int pressure) {
                this.pressure = pressure;
            }

            public int getSealevel() {
                return sealevel;
            }

            public void setSealevel(int sealevel) {
                this.sealevel = sealevel;
            }

            public int getGroundLevel() {
                return groundLevel;
            }

            public void setGroundLevel(int groundLevel) {
                this.groundLevel = groundLevel;
            }

            public int getHumidity() {
                return humidity;
            }

            public void setHumidity(int humidity) {
                this.humidity = humidity;
            }
        }

        public static class Weather implements Parcelable {
            @SerializedName("id")
            private int id;

            @SerializedName("main")
            private String main;

            @SerializedName("description")
            private String description;

            @SerializedName("icon")
            private String icon;

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

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getMain() {
                return main;
            }

            public void setMain(String main) {
                this.main = main;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }

        public static class Wind implements Parcelable {
            @SerializedName("speed")
            private float speed;

            @SerializedName("deg")
            private float deg;

            @SerializedName("gust")
            private float gust;

            protected Wind(Parcel in) {
                speed = in.readFloat();
                deg = in.readFloat();
                gust = in.readFloat();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeFloat(speed);
                dest.writeFloat(deg);
                dest.writeFloat(gust);
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

            public float getSpeed() {
                return speed;
            }

            public void setSpeed(float speed) {
                this.speed = speed;
            }

            public float getDeg() {
                return deg;
            }

            public void setDeg(float deg) {
                this.deg = deg;
            }

            public float getGust() {
                return gust;
            }

            public void setGust(float gust) {
                this.gust = gust;
            }
        }
    }

    public static class City implements Parcelable {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        @SerializedName("coord")
        private Coord coord;

        @SerializedName("country")
        private String country;

        @SerializedName("timezone")
        private int timezone;

        @SerializedName("sunrise")
        private long sunrise;

        @SerializedName("sunset")
        private long sunset;

        protected City(Parcel in) {
            id = in.readInt();
            name = in.readString();
            country = in.readString();
            timezone = in.readInt();
            sunrise = in.readLong();
            sunset = in.readLong();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeString(country);
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Coord getCoord() {
            return coord;
        }

        public void setCoord(Coord coord) {
            this.coord = coord;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public int getTimezone() {
            return timezone;
        }

        public void setTimezone(int timezone) {
            this.timezone = timezone;
        }

        public long getSunrise() {
            return sunrise;
        }

        public void setSunrise(long sunrise) {
            this.sunrise = sunrise;
        }

        public long getSunset() {
            return sunset;
        }

        public void setSunset(long sunset) {
            this.sunset = sunset;
        }

        public static class Coord implements Parcelable {
            @SerializedName("lat")
            private float lat;

            @SerializedName("lon")
            private float lon;

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

            public float getLat() {
                return lat;
            }

            public void setLat(float lat) {
                this.lat = lat;
            }

            public float getLon() {
                return lon;
            }

            public void setLon(float lon) {
                this.lon = lon;
            }
        }
    }
}