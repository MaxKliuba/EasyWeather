package com.maxclub.easyweather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.maxclub.easyweather.api.WeatherApi;
import com.maxclub.easyweather.api.model.WeatherData;
import com.maxclub.easyweather.database.model.City;
import com.maxclub.easyweather.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class CityWeatherFragment extends Fragment {

    private static final String TAG = "CityWeatherFragment";

    private static final String ARG_CITY = "city";

    private static final String KEY_WEATHER_DATA = "mWeatherData";

    private static final int REQUEST_REMOVE = 0;
    private static final String DIALOG_REMOVE = "DialogRemoveItem";

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final WeatherApi mWeatherApi = WeatherApi.Instance.getApi();
    private WeatherData mWeatherData;
    private City mCity;

    private View mConnectionErrorContainer;
    private TextView mMessageTextView;
    private View mWaitingForDataViewContainer;
    private View mMainContentContainer;
    private final List<View> mViewContainers = new ArrayList<>();

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTextView;

    public static CityWeatherFragment newInstance(City city) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_CITY, city);

        CityWeatherFragment fragment = new CityWeatherFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCity = (City) getArguments().getParcelable(ARG_CITY);

        if (savedInstanceState != null) {
            mWeatherData = (WeatherData) savedInstanceState.getParcelable(KEY_WEATHER_DATA);
        }

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
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

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

        mTextView = (TextView) view.findViewById(R.id.weather_textview);

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

        outState.putParcelable(KEY_WEATHER_DATA, mWeatherData);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.city_weather_fragment, menu);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(String.format("%s, %s", mCity.name, mCity.country));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent CityListActivityIntent = CityListActivity.newIntent(getContext());
                startActivity(CityListActivityIntent);
                return true;
            case R.id.action_search:
                Intent SearchWeatherActivityIntent = SearchWeatherActivity.newIntent(getActivity());
                startActivity(SearchWeatherActivityIntent);
                return true;
            case R.id.action_delete:
                FragmentManager manager = getActivity().getSupportFragmentManager();
                RemoveItemDialogFragment dialog = RemoveItemDialogFragment.newInstance();
                dialog.setTargetFragment(CityWeatherFragment.this, REQUEST_REMOVE);
                dialog.show(manager, DIALOG_REMOVE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_REMOVE:
                if (resultCode == Activity.RESULT_OK) {
                    App.getInstance().getCityDao().delete(mCity);
                }
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void fetchWeatherByCityName(String city) {
        mSwipeRefreshLayout.setRefreshing(true);
        mCompositeDisposable.clear();
        mCompositeDisposable.add(mWeatherApi.getWeatherData(city, 40, "metric", "en")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<WeatherData, Throwable>() {
                    @Override
                    public void accept(WeatherData weatherData, Throwable throwable) throws Exception {
                        mSwipeRefreshLayout.setRefreshing(false);

                        mWeatherData = weatherData;
                        updateUserInterface();

                        if (throwable != null) {
                            Log.e(TAG, throwable.getMessage(), throwable);
                            setConnectionErrorContainerVisible(throwable.getMessage());
                        }
                    }
                }));
    }

    private void updateWeather() {
        if (mWeatherData == null) {
            Utils.switchView(mWaitingForDataViewContainer, mViewContainers);
        }

        fetchWeatherByCityName(mCity.name);
    }

    private void updateUserInterface() {
        if (mWeatherData != null) {
            mTextView.setText(String.format("%s %s %s",
                    mWeatherData.getCity().getName(),
                    mWeatherData.getList().get(0).getMain().getTemp(),
                    mWeatherData.getList().get(0).getWeather().get(0).getMain()));

            Utils.switchView(mMainContentContainer, mViewContainers);
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
        Utils.switchView(mConnectionErrorContainer, mViewContainers);
    }
}