package com.maxclub.easyweather;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class LocationWeatherActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return LocationWeatherFragment.newInstance();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LocationWeatherActivity.class);

        return intent;
    }
}