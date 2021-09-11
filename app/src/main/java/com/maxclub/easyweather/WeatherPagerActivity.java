package com.maxclub.easyweather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.SortedList;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.maxclub.easyweather.database.model.City;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WeatherPagerActivity extends AppCompatActivity {

    private static final String EXTRA_CITY = "com.maxclub.easyweather.extra_city";

    private FragmentAdapter mFragmentAdapter;
    private ViewPager2 mViewPager;
    private boolean mFirstUpdate = true;

    public static Intent newIntent(Context context, City city) {
        Intent intent = new Intent(context, WeatherPagerActivity.class);
        intent.putExtra(EXTRA_CITY, city);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_pager);

        City city = (City) getIntent().getParcelableExtra(EXTRA_CITY);

        mViewPager = (ViewPager2) findViewById(R.id.weather_view_pager);
        mFragmentAdapter = new FragmentAdapter(this);
        mViewPager.setAdapter(mFragmentAdapter);

        CityViewModel cityViewModel = new ViewModelProvider(this).get(CityViewModel.class);
        cityViewModel.getCityLiveData().observe(this, new Observer<List<City>>() {
            @Override
            public void onChanged(List<City> cityList) {
                mFragmentAdapter.setItems(cityList);
                mFragmentAdapter.notifyDataSetChanged();

                if (mFirstUpdate) {
                    for (int i = 0; i < mFragmentAdapter.getItemCount(); i++) {
                        if (mFragmentAdapter.getItems().get(i).equals(city)) {
                            mViewPager.setCurrentItem(i, false);
                            break;
                        }
                    }
                    mFirstUpdate = false;
                }
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, mViewPager, true,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {

                    }
                });
        tabLayoutMediator.attach();
    }

    private class FragmentAdapter extends FragmentStateAdapter {
        private SortedList<City> mItems;

        public FragmentAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);

            mItems = new SortedList<>(City.class, new SortedList.Callback<City>() {
                @Override
                public int compare(City o1, City o2) {
                    return o1.order - o2.order;
                }

                @Override
                public void onChanged(int position, int count) {
                    mViewPager.getAdapter().notifyItemRangeChanged(position, count);
                }

                @Override
                public boolean areContentsTheSame(City oldItem, City newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areItemsTheSame(City item1, City item2) {
                    return item1.id == item2.id;
                }

                @Override
                public void onInserted(int position, int count) {
                    mViewPager.getAdapter().notifyItemRangeInserted(position, count);
                    mViewPager.setCurrentItem(position, false);
                }

                @Override
                public void onRemoved(int position, int count) {
                    mViewPager.getAdapter().notifyItemRangeRemoved(position, count);
                }

                @Override
                public void onMoved(int fromPosition, int toPosition) {
                    mViewPager.getAdapter().notifyItemMoved(fromPosition, toPosition);
                }
            });
        }

        public SortedList<City> getItems() {
            return mItems;
        }

        public void setItems(List<City> items) {
            mItems.replaceAll(items);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        @NonNull
        @NotNull
        @Override
        public Fragment createFragment(int position) {
            City city = mItems.get(position);

            if (city.id == 0) {
                return LocationWeatherFragment.newInstance();
            } else {
                return CityWeatherFragment.newInstance(city);
            }
        }

        @Override
        public long getItemId(int position) {
            return mItems.get(position).id;
        }

        @Override
        public boolean containsItem(long itemId) {
            for (int i = 0; i < mItems.size(); i++) {
                if (mItems.get(i).id == itemId) {
                    return true;
                }
            }

            return false;
        }
    }
}