package com.maxclub.easyweather;

import androidx.fragment.app.Fragment;

public class LocationWeatherActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return LocationWeatherFragment.newInstance();
    }
}