package com.maxclub.easyweather;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class CityListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return CityListFragment.newInstance();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, CityListActivity.class);

        return intent;
    }
}
