package com.maxclub.easyweather;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class WeatherDrawableManager {

    private static final String TAG = "WeatherDrawableManager";

    private static final String WEATHER_DRAWABLES_FOLDER = "weather_drawables";

    private AssetManager mAssets;
    private Map<String, Drawable> mDrawables = new LinkedHashMap<>();

    public WeatherDrawableManager(Context context) {
        mAssets = context.getAssets();
        loadWeatherDrawables();
    }

    public Map<String, Drawable> getDrawables() {
        return mDrawables;
    }

    public Drawable getDrawableByName(String name) {
        return mDrawables.get(name);
    }

    private void loadWeatherDrawables() {
        String[] weatherDrawableNames;
        try {
            weatherDrawableNames = mAssets.list(WEATHER_DRAWABLES_FOLDER);
            Log.i(TAG, "Found " + weatherDrawableNames.length + " drawables");
        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
            return;
        }

        for (String filename : weatherDrawableNames) {
            String assetPath = WEATHER_DRAWABLES_FOLDER + "/" + filename;
            WeatherDrawable weatherDrawable = new WeatherDrawable(assetPath);

            try {
                load(weatherDrawable);
            } catch (IOException ioe) {
                Log.e(TAG, "Could not load drawable " + filename, ioe);
            }

            mDrawables.put(weatherDrawable.getName(), weatherDrawable.getDrawable());
        }
    }

    private void load(WeatherDrawable weatherDrawable) throws IOException {
        InputStream inputStream = mAssets.open(weatherDrawable.getAssetPath());
        Drawable drawable = Drawable.createFromStream(inputStream, null);
        weatherDrawable.setDrawable(drawable);
    }
}
