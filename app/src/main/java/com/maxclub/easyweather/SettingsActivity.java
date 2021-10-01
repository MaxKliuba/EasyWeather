package com.maxclub.easyweather;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class SettingsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return SettingsFragment.newInstance();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);

        return intent;
    }
}
