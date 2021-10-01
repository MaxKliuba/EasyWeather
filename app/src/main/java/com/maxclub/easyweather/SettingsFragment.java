package com.maxclub.easyweather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButtonToggleGroup;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends Fragment {

    private MaterialButtonToggleGroup mThemeButtonToggleGroup;
    private MaterialButtonToggleGroup mUnitsButtonToggleGroup;

    private final Map<Integer, Integer> mThemeButtonToggleGroupMap = new HashMap<>();
    private final Map<String, Integer> mUnitsButtonToggleGroupMap = new HashMap<>();

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mThemeButtonToggleGroupMap.put(SettingsPreferences.LIGHT_THEME, R.id.button_light_theme);
        mThemeButtonToggleGroupMap.put(SettingsPreferences.AUTO_THEME, R.id.button_auto_theme);
        mThemeButtonToggleGroupMap.put(SettingsPreferences.NIGHT_THEME, R.id.button_night_theme);

        mUnitsButtonToggleGroupMap.put(SettingsPreferences.METRIC, R.id.button_celsius);
        mUnitsButtonToggleGroupMap.put(SettingsPreferences.IMPERIAL, R.id.button_fahrenheit);
        mUnitsButtonToggleGroupMap.put(SettingsPreferences.STANDARD, R.id.button_kelvin);

        setHasOptionsMenu(true);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle(R.string.settings_title);

        mThemeButtonToggleGroup = (MaterialButtonToggleGroup) view.findViewById(R.id.theme_button_toggle_group);
        mThemeButtonToggleGroup.check(mThemeButtonToggleGroupMap.get(SettingsPreferences.getTheme(getActivity())));
        mThemeButtonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                for (int key : mThemeButtonToggleGroupMap.keySet()) {
                    if (checkedId == mThemeButtonToggleGroupMap.get(key)) {
                        SettingsPreferences.setTheme(getActivity(), key);
                        break;
                    }
                }
            }
        });
        mUnitsButtonToggleGroup = (MaterialButtonToggleGroup) view.findViewById(R.id.units_button_toggle_group);
        mUnitsButtonToggleGroup.check(mUnitsButtonToggleGroupMap.get(SettingsPreferences.getUnits(getActivity())));
        mUnitsButtonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                for (String key : mUnitsButtonToggleGroupMap.keySet()) {
                    if (checkedId == mUnitsButtonToggleGroupMap.get(key)) {
                        SettingsPreferences.setUnits(getActivity(), key);
                        break;
                    }
                }
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
