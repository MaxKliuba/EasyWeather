package com.maxclub.android.easyweather.data;

import com.google.gson.annotations.SerializedName;

public class SearchData {
    @SerializedName("Key")
    private String mKey;

    @SerializedName("LocalizedName")
    private String mLocalizedName;

    @SerializedName("EnglishName")
    private String mEnglishName;

    public String getKey() {
        return mKey;
    }

    public void setKey(final String key) {
        mKey = key;
    }

    public String getLocalizedName() {
        return mLocalizedName;
    }

    public void setLocalizedName(String localizedName) {
        mLocalizedName = localizedName;
    }

    public String getEnglishName() {
        return mEnglishName;
    }

    public void setEnglishName(String englishName) {
        mEnglishName = englishName;
    }
}
