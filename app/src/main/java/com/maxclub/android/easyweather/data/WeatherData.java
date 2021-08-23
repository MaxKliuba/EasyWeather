package com.maxclub.android.easyweather.data;

import com.google.gson.annotations.SerializedName;

public class WeatherData {
    @SerializedName("main")
    private Main mMain;

    @SerializedName("name")
    private String mName;

    public Main getMain() {
        return mMain;
    }

    public void setMain(Main main) {
        mMain = main;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public class Main {
        @SerializedName("temp")
        private float mTemp;

        public float getTemp() {
            return mTemp;
        }

        public void setTemp(float temp) {
            mTemp = temp;
        }
    }
}
