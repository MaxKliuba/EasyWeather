package com.maxclub.android.easyweather.data;

import com.google.gson.annotations.SerializedName;

public class WeatherData2 {
    @SerializedName("Headline")
    private Headline mHeadline;

    public Headline getHeadline() {
        return mHeadline;
    }

    public void setHeadline(Headline headline) {
        mHeadline = headline;
    }

    public class Headline {
        @SerializedName("Text")
        private String mText;

        public String getText() {
            return mText;
        }

        public void setText(String text) {
            mText = text;
        }
    }
}
