package com.maxclub.easyweather;

import android.graphics.drawable.Drawable;

public class WeatherDrawable {
    private String mAssetPath;
    private String mName;
    private Drawable mDrawable;

    public WeatherDrawable(String assetPath) {
        mAssetPath = assetPath;
        String[] components = assetPath.split("/");
        String filename = components[components.length - 1];
        mName = filename.replace(".png", "");
    }

    public String getAssetPath() {
        return mAssetPath;
    }

    public String getName() {
        return mName;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }
}
