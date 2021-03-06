package com.maxclub.easyweather;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.maxclub.easyweather.api.WeatherApi;
import com.maxclub.easyweather.api.model.ForecastWeatherData;
import com.maxclub.easyweather.api.model.OneCallWeatherData;
import com.maxclub.easyweather.database.model.City;
import com.maxclub.easyweather.utils.LocaleHelper;
import com.maxclub.easyweather.utils.StringHelper;
import com.maxclub.easyweather.utils.ViewHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class SearchWeatherFragment extends Fragment {

    private static final String TAG = "SearchWeatherFragment";

    private static final String KEY_CITY_DATA = "mCityData";
    private static final String KEY_ONE_CALL_WEATHER_DATA = "mOneCallWeatherData";
    private static final String KEY_EDITABLE_QUERY = "mEditableQuery";
    private static final String KEY_QUERY = "mQuery";
    private static final String KEY_IS_SEARCH_VIEW_ICONIFIED = "mIsSearchViewIconified";

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final WeatherApi mWeatherApi = WeatherApi.Instance.getApi();
    private ForecastWeatherData.City mCityData;
    private OneCallWeatherData mOneCallWeatherData;
    private String mEditableQuery;
    private String mQuery;
    private boolean mIsSearchViewIconified = false;
    private boolean mIsSearchViewOnActionViewCollapsed = false;

    private View mConnectionErrorContainer;
    private TextView mMessageTextView;
    private View mWaitingForDataViewContainer;
    private View mMainContentContainer;
    private final List<View> mViewContainers = new ArrayList<>();
    private WeatherDrawableManager mWeatherDrawableManager;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SearchView mSearchView;
    private ImageView mMainIconImageView;
    private TextView mMainDescriptionTextView;
    private TextView mMainTempTextView;
    private TextView mMainFeelsLikeTextView;
    private TextView mWindTextView;
    private TextView mPopTextView;
    private TextView mUviTextView;
    private TextView mVisibilityTextView;
    private TextView mHumidityTextView;
    private TextView mPressureTextView;

    private RecyclerView mHourlyWeatherRecyclerView;
    private HourlyWeatherAdapter mHourlyWeatherAdapter;
    private RecyclerView mDailyWeatherRecyclerView;
    private DailyWeatherAdapter mDailyWeatherAdapter;

    public static SearchWeatherFragment newInstance() {
        return new SearchWeatherFragment();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCityData = (ForecastWeatherData.City) savedInstanceState.getParcelable(KEY_CITY_DATA);
            mOneCallWeatherData = (OneCallWeatherData) savedInstanceState.getParcelable(KEY_ONE_CALL_WEATHER_DATA);
            mEditableQuery = (String) savedInstanceState.getString(KEY_EDITABLE_QUERY);
            mQuery = (String) savedInstanceState.getString(KEY_QUERY);
            mIsSearchViewIconified = (boolean) savedInstanceState.getBoolean(KEY_IS_SEARCH_VIEW_ICONIFIED);
        }

        mWeatherDrawableManager = new WeatherDrawableManager(getActivity());

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

        mConnectionErrorContainer = (View) view.findViewById(R.id.connection_error_view_container);
        mConnectionErrorContainer.setVisibility(View.GONE);
        ImageView connectingErrorImageView = (ImageView) view.findViewById(R.id.connection_error_image_view);
        Glide.with(getActivity())
                .asGif()
                .load(R.raw.gif_connection_error)
                .into(connectingErrorImageView);
        mMessageTextView = (TextView) view.findViewById(R.id.message_text_view);
        Button retryButton = (Button) view.findViewById(R.id.retry_button);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateWeather();
            }
        });

        mWaitingForDataViewContainer = (View) view.findViewById(R.id.waiting_for_data_view_container);
        mWaitingForDataViewContainer.setVisibility(View.GONE);
        ImageView loadingViewImageView = (ImageView) view.findViewById(R.id.waiting_for_data_image_view);
        Glide.with(getActivity())
                .asGif()
                .load(R.raw.gif_waiting_for_data)
                .into(loadingViewImageView);

        mMainContentContainer = (View) view.findViewById(R.id.main_content_container);
        mMainContentContainer.setVisibility(View.GONE);

        mViewContainers.add(mConnectionErrorContainer);
        mViewContainers.add(mWaitingForDataViewContainer);
        mViewContainers.add(mMainContentContainer);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_refresh_layout_color);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.swipe_refresh_layout_background_color);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateWeather();
            }
        });

        mMainIconImageView = (ImageView) view.findViewById(R.id.main_icon_image_view);
        mMainDescriptionTextView = (TextView) view.findViewById(R.id.main_description_text_view);
        mMainTempTextView = (TextView) view.findViewById(R.id.main_temp_text_view);
        mMainFeelsLikeTextView = (TextView) view.findViewById(R.id.main_feels_like_text_view);
        mWindTextView = (TextView) view.findViewById(R.id.wind_text_view);
        mPopTextView = (TextView) view.findViewById(R.id.pop_text_view);
        mUviTextView = (TextView) view.findViewById(R.id.uvi_text_view);
        mVisibilityTextView = (TextView) view.findViewById(R.id.visibility_text_view);
        mHumidityTextView = (TextView) view.findViewById(R.id.humidity_text_view);
        mPressureTextView = (TextView) view.findViewById(R.id.pressure_text_view);

        mHourlyWeatherRecyclerView = (RecyclerView) view.findViewById(R.id.hourly_weather_recycler_view);
        mHourlyWeatherRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mHourlyWeatherRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull @NotNull RecyclerView rv, @NonNull @NotNull MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    mMainContentContainer.getParent().requestDisallowInterceptTouchEvent(true);
                }

                return false;
            }

            @Override
            public void onTouchEvent(@NonNull @NotNull RecyclerView rv, @NonNull @NotNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

        mDailyWeatherRecyclerView = (RecyclerView) view.findViewById(R.id.daily_weather_recycler_view);
        mDailyWeatherRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mDailyWeatherRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull @NotNull RecyclerView rv, @NonNull @NotNull MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    mMainContentContainer.getParent().requestDisallowInterceptTouchEvent(true);
                }

                return false;
            }

            @Override
            public void onTouchEvent(@NonNull @NotNull RecyclerView rv, @NonNull @NotNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

        updateUserInterface();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        updateWeather();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mCompositeDisposable.dispose();
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_CITY_DATA, mCityData);
        outState.putParcelable(KEY_ONE_CALL_WEATHER_DATA, mOneCallWeatherData);
        outState.putString(KEY_EDITABLE_QUERY, mEditableQuery);
        outState.putString(KEY_QUERY, mQuery);
        outState.putBoolean(KEY_IS_SEARCH_VIEW_ICONIFIED, mIsSearchViewIconified);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.search_weather_fragment, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search_view);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setQueryHint(getString(R.string.search_city_query_hint));
        mSearchView.setQuery(mEditableQuery, false);
        mSearchView.setIconified(mIsSearchViewIconified);
        if (mIsSearchViewIconified) {
            mSearchView.clearFocus();
        }

        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "SearchView.setOnSearchClickListener() -> onClick()");
                mIsSearchViewIconified = false;
                mSearchView.setQuery(mEditableQuery, false);
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit() -> " + query);
                mQuery = query;
                mEditableQuery = query;
                mSearchView.clearFocus();
                mIsSearchViewIconified = true;
                fetchWeatherByCityName(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange() -> " + newText);
                if (!mIsSearchViewOnActionViewCollapsed) {
                    mEditableQuery = newText;
                    mIsSearchViewIconified = false;
                }

                return false;
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d(TAG, "SearchView.setOnCloseListener() -> onClose()");
                mIsSearchViewIconified = true;
                mEditableQuery = mQuery;

                return false;
            }
        });

        MenuItem saveCityMenuItem = menu.findItem(R.id.action_add);
        saveCityMenuItem.setVisible(mCityData != null);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.action_add:
                City city = new City();
                city.id = mCityData.id;
                city.name = mCityData.name;
                city.country = mCityData.country;
                city.lat = mCityData.coord.lat;
                city.lon = mCityData.coord.lon;

                App.getInstance().getCityDao().insert(city);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("CheckResult")
    private void fetchWeatherByCityName(String cityName) {
        mSwipeRefreshLayout.setRefreshing(true);
        mCompositeDisposable.clear();
        mCompositeDisposable.add(mWeatherApi.getWeatherData(cityName, 1,
                SettingsPreferences.getUnits(getActivity()), LocaleHelper.getLanguage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<ForecastWeatherData, Throwable>() {
                    @Override
                    public void accept(ForecastWeatherData forecastWeatherData, Throwable throwable) throws Exception {
                        if (forecastWeatherData != null) {
                            mCityData = forecastWeatherData.city;
                            fetchWeatherByCityData(mCityData);
                        }

                        if (throwable != null) {
                            Log.e(TAG, throwable.getMessage(), throwable);
                            mSwipeRefreshLayout.setRefreshing(false);
                            setConnectionErrorContainerVisible(throwable.getMessage());
                        }
                    }
                }));
    }

    @SuppressLint("CheckResult")
    private void fetchWeatherByCityData(ForecastWeatherData.City city) {
        mSwipeRefreshLayout.setRefreshing(true);
        mCompositeDisposable.clear();
        mCompositeDisposable.add(mWeatherApi.getOneCallWeatherData(city.coord.lat, city.coord.lon,
                "minutely", SettingsPreferences.getUnits(getActivity()), LocaleHelper.getLanguage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<OneCallWeatherData, Throwable>() {
                    @Override
                    public void accept(OneCallWeatherData oneCallWeatherData, Throwable throwable) throws Exception {
                        mSwipeRefreshLayout.setRefreshing(false);

                        mOneCallWeatherData = oneCallWeatherData;
                        updateUserInterface();

                        if (mSearchView != null && mIsSearchViewIconified) {
                            mIsSearchViewOnActionViewCollapsed = true;
                            mSearchView.onActionViewCollapsed();
                            mIsSearchViewOnActionViewCollapsed = false;
                        }
                        getActivity().invalidateOptionsMenu();

                        if (throwable != null) {
                            Log.e(TAG, throwable.getMessage(), throwable);
                            setConnectionErrorContainerVisible(throwable.getMessage());
                        }
                    }
                }));
    }

    private void updateWeather() {
        if (mCityData == null || mOneCallWeatherData == null) {
            ViewHelper.switchView(mWaitingForDataViewContainer, mViewContainers);
        }

        if (mQuery != null) {
            fetchWeatherByCityName(mQuery);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void updateUserInterface() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (mCityData != null && mOneCallWeatherData != null) {
            activity.getSupportActionBar().setSubtitle(String.format("%s, %s",
                    mCityData.name, mCityData.country));

            mMainIconImageView.setImageDrawable(
                    mWeatherDrawableManager.getDrawableByName(mOneCallWeatherData.current.weather.get(0).icon)
            );
            mMainDescriptionTextView.setText(
                    StringHelper.capitalize(mOneCallWeatherData.current.weather.get(0).description)
            );

            String[] windDirections = getResources().getStringArray(R.array.wind_directions);
            int index = Math.round(mOneCallWeatherData.current.windDeg / 45.0f);
            String windDirection = windDirections[index >= windDirections.length ? 0 : index];

            switch (SettingsPreferences.getUnits(getActivity())) {
                case SettingsPreferences.IMPERIAL:
                    mMainTempTextView.setText(getString(R.string.temp_f_label,
                            Math.round(mOneCallWeatherData.current.temp)));
                    mMainFeelsLikeTextView.setText(getString(R.string.feels_like_temp_f_label,
                            Math.round(mOneCallWeatherData.current.feelsLike)));
                    mWindTextView.setText(getString(R.string.wind_mph_label,
                            mOneCallWeatherData.current.windSpeed, windDirection));
                    mVisibilityTextView.setText(getString(R.string.visibility_mi_label,
                            mOneCallWeatherData.current.visibility / 1609.344f));
                    break;
                case SettingsPreferences.STANDARD:
                    mMainTempTextView.setText(getString(R.string.temp_k_label,
                            Math.round(mOneCallWeatherData.current.temp)));
                    mMainFeelsLikeTextView.setText(getString(R.string.feels_like_temp_k_label,
                            Math.round(mOneCallWeatherData.current.feelsLike)));
                    mWindTextView.setText(getString(R.string.wind_m_s_label,
                            mOneCallWeatherData.current.windSpeed, windDirection));
                    mVisibilityTextView.setText(getString(R.string.visibility_km_label,
                            mOneCallWeatherData.current.visibility / 1000.0f));
                    break;
                default:
                    mMainTempTextView.setText(getString(R.string.temp_c_label,
                            Math.round(mOneCallWeatherData.current.temp)));
                    mMainFeelsLikeTextView.setText(getString(R.string.feels_like_temp_c_label,
                            Math.round(mOneCallWeatherData.current.feelsLike)));
                    mWindTextView.setText(getString(R.string.wind_m_s_label,
                            mOneCallWeatherData.current.windSpeed, windDirection));
                    mVisibilityTextView.setText(getString(R.string.visibility_km_label,
                            mOneCallWeatherData.current.visibility / 1000.0f));
                    break;
            }
            mPopTextView.setText(getString(R.string.pop_label,
                    (int) (mOneCallWeatherData.hourly.get(0).pop * 100)));
            mUviTextView.setText(getString(R.string.uvi_label,
                    mOneCallWeatherData.current.uvi));
            mVisibilityTextView.setText(getString(R.string.visibility_km_label,
                    mOneCallWeatherData.current.visibility / 1000.0f));
            mHumidityTextView.setText(getString(R.string.humidity_label,
                    mOneCallWeatherData.current.humidity));
            mPressureTextView.setText(getString(R.string.pressure_label,
                    mOneCallWeatherData.current.pressure));

            if (mHourlyWeatherAdapter == null) {
                mHourlyWeatherAdapter = new HourlyWeatherAdapter(getActivity());
                mHourlyWeatherRecyclerView.setAdapter(mHourlyWeatherAdapter);
            }

            mHourlyWeatherAdapter.setItems(mOneCallWeatherData);
            mHourlyWeatherAdapter.notifyDataSetChanged();

            if (mDailyWeatherAdapter == null) {
                mDailyWeatherAdapter = new DailyWeatherAdapter(getActivity());
                mDailyWeatherRecyclerView.setAdapter(mDailyWeatherAdapter);
            }

            mDailyWeatherAdapter.setItems(mOneCallWeatherData);
            mDailyWeatherAdapter.notifyDataSetChanged();

            ViewHelper.switchView(mMainContentContainer, mViewContainers);
        } else {
            activity.getSupportActionBar().setSubtitle(null);
        }
    }

    private void setConnectionErrorContainerVisible() {
        setConnectionErrorContainerVisible(null);
    }

    private void setConnectionErrorContainerVisible(String message) {
        if (message != null) {
            mMessageTextView.setText(message);
            mMessageTextView.setVisibility(View.VISIBLE);
        } else {
            mMessageTextView.setText(null);
            mMessageTextView.setVisibility(View.GONE);
        }
        ViewHelper.switchView(mConnectionErrorContainer, mViewContainers);
    }
}