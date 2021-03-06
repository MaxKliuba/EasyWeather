package com.maxclub.easyweather;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class SearchWeatherActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return SearchWeatherFragment.newInstance();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SearchWeatherActivity.class);

        return intent;
    }
}