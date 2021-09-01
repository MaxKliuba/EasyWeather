package com.maxclub.easyweather;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.maxclub.easyweather.api.WeatherApi;
import com.maxclub.easyweather.api.model.WeatherData;
import com.maxclub.easyweather.database.model.City;

import org.jetbrains.annotations.NotNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class SearchWeatherFragment extends Fragment {

    private static final String TAG = "SearchWeatherFragment";

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private WeatherApi mWeatherApi = WeatherApi.Instance.getApi();
    private WeatherData mWeatherData;
    private String mQuery = "";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SearchView mSearchView;
    private TextView mTextView;

    public static SearchWeatherFragment newInstance() {
        return new SearchWeatherFragment();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        setHasOptionsMenu(true);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_refresh_layout_color);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.swipe_refresh_layout_background_color);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateWeather();
            }
        });

        mTextView = (TextView) view.findViewById(R.id.weather_textview);

        updateUserInterface();

        return view;
    }

    @Override
    public void onDestroy() {
        mCompositeDisposable.dispose();

        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.search_weather_fragment, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search_view);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setQueryHint(getString(R.string.search_city_query_hint));
        mSearchView.setQuery(mQuery, false);
        mSearchView.setIconified(mQuery == null);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "QueryTextSubmit: " + query);
                mQuery = query;
                mSearchView.clearFocus();
                fetchWeatherByCityName(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "QueryTextChange: " + newText);
                mQuery = newText;

                return false;
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });

        MenuItem addCityMenuItem = menu.findItem(R.id.action_add);
        addCityMenuItem.setVisible(mWeatherData != null);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.action_add:
                City city = new City(mWeatherData.getCity().getId(),
                        mWeatherData.getCity().getName(),
                        mWeatherData.getCity().getCountry());
                EasyWeatherApp.getInstance().getCityDao().insert(city);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("CheckResult")
    private void fetchWeatherByCityName(String city) {
        mSwipeRefreshLayout.setRefreshing(true);
        mCompositeDisposable.add(mWeatherApi.getWeatherData(city, 40, "metric", "en")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<WeatherData, Throwable>() {
                    @Override
                    public void accept(WeatherData weatherData, Throwable throwable) throws Exception {
                        mSwipeRefreshLayout.setRefreshing(false);

                        if (throwable != null) {
                            Log.e(TAG, throwable.getMessage(), throwable);
                            // вивід екрану із повідомленням, що щось пішло не так
                        }

                        mWeatherData = weatherData;
                        updateUserInterface();

                        if (mSearchView != null) {
                            mSearchView.onActionViewCollapsed();
                            mQuery = null;
                        }
                    }
                }));
    }

    private void updateWeather() {
        if (mQuery != null) {
            fetchWeatherByCityName(mQuery);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void updateUserInterface() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (mWeatherData != null) {
            activity.getSupportActionBar().setSubtitle(String.format("%s, %s",
                    mWeatherData.getCity().getName(), mWeatherData.getCity().getCountry()));
            mTextView.setText(String.format("%s %s %s",
                    mWeatherData.getCity().getName(),
                    mWeatherData.getList().get(0).getMain().getTemp(),
                    mWeatherData.getList().get(0).getWeather().get(0).getMain()));
        } else {
            activity.getSupportActionBar().setSubtitle(null);
            mTextView.setText(null);
        }

        getActivity().invalidateOptionsMenu();
    }
}