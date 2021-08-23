package com.maxclub.android.easyweather;

import androidx.fragment.app.Fragment;

public class EasyWeatherActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return SearchWeatherFragment.newInstance();
    }
}