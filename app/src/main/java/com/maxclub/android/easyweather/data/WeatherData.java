package com.maxclub.android.easyweather.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherData {
    @SerializedName("cnt")
    private int cnt;

    @SerializedName("list")
    List<ListItem> list;

    @SerializedName("city")
    private City city;

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

    public static class ListItem {
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

        public static class Main {
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

        public static class Weather {
            @SerializedName("id")
            private int id;

            @SerializedName("main")
            private String main;

            @SerializedName("description")
            private String description;

            @SerializedName("icon")
            private String icon;

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

        public static class Wind {
            @SerializedName("speed")
            private float speed;

            @SerializedName("deg")
            private float deg;

            @SerializedName("gust")
            private float gust;

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

    public static class City {
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

        public static class Coord {
            @SerializedName("lat")
            private float lat;

            @SerializedName("lon")
            private float lon;

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