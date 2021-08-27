package com.maxclub.easyweather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.maxclub.easyweather.api.WeatherApi;
import com.maxclub.easyweather.data.WeatherData;

import org.jetbrains.annotations.NotNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LocationWeatherFragment extends Fragment implements LocationListener {

    private static final String TAG = "LocationWeatherFragment";

    private static final String[] LOCATION_PERMISSION = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };
    private static final int REQUEST_LOCATION_PERMISSION = 0;

    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private WeatherApi mWeatherApi = WeatherApi.Instance.getApi();
    private WeatherData mWeatherData;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Toolbar mToolbar;
    private TextView mTextView;

    public static LocationWeatherFragment newInstance() {
        return new LocationWeatherFragment();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (isGooglePlayServicesAvailable()) {
            connectToGoogleApiClient();
        } else {
            Log.e(TAG, "Google Play services are not available");
            // вивід екрану із повідомленням про відсутність google play services
        }
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_fragment, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_refresh_layout_color);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.swipe_refresh_layout_background_color);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateWeather();
            }
        });
        mToolbar = (Toolbar) view.findViewById(R.id.weather_fragment_toolbar);
        mTextView = (TextView) view.findViewById(R.id.weather_textview);

        updateUserInterface();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            if (hasLocationPermission()) {
                registerLocationRequestListener();
            } else {
                requestPermissions(LOCATION_PERMISSION, REQUEST_LOCATION_PERMISSION);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        removeLocationRequestListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        disconnectFromGoogleApiClient();
    }

    @Override
    public void onLocationChanged(@NonNull @NotNull Location location) {
        Log.i(TAG, "onLocationChanged() -> " + location.getLatitude() + ", " + location.getLongitude());
        mLocation = location;
        fetchWeatherByLocation(location);
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int errorCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());

        return errorCode == ConnectionResult.SUCCESS;
    }

    private void connectToGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(@Nullable @org.jetbrains.annotations.Nullable Bundle bundle) {
                            Log.i(TAG, "GoogleApiClient.onConnected()");
                            if (hasLocationPermission()) {
                                registerLocationRequestListener();
                            } else {
                                requestPermissions(LOCATION_PERMISSION, REQUEST_LOCATION_PERMISSION);
                            }
                        }

                        @Override
                        public void onConnectionSuspended(int i) {

                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull @NotNull ConnectionResult connectionResult) {
                            Log.e(TAG, "GoogleApiClient connection error");
                        }
                    })
                    .build();

        }

        mGoogleApiClient.connect();
    }

    private void disconnectFromGoogleApiClient() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    private boolean hasLocationPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSION[0]);

        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull @NotNull String[] permissions,
                                           @NonNull @NotNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (hasLocationPermission()) {
                    registerLocationRequestListener();
                } else {
                    // вивід екрану із повідомленням про необхідність дозволів і кнопкою переходу у налаштування
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressLint("MissingPermission")
    private void registerLocationRequestListener() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation != null) {
            Log.i(TAG, "LastLocation -> " + mLocation.getLatitude() + ", " + mLocation.getLongitude());
            updateWeather();
        }
    }

    private void removeLocationRequestListener() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @SuppressLint("CheckResult")
    private void fetchWeatherByLocation(Location location) {
        mWeatherApi.getWeatherData(location.getLatitude(), location.getLongitude(), WeatherApi.API_KEY, 40, "metric", "en")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<WeatherData>() {
                    @Override
                    public void onNext(@NotNull WeatherData weatherData) {
                        mWeatherData = weatherData;
                        updateUserInterface();
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        if (!Utils.isNetworkAvailableAndConnected(getActivity())) {
                            Log.e(TAG, "No Internet connection", e);
                            // вивід екрану із повідомленням про відсутність інтернет з'єднання
                        } else {
                            Log.e(TAG, e.getMessage(), e);
                            // вивід екрану із повідомленням, що щось пішло не так
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void updateWeather() {
        if (mLocation != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            fetchWeatherByLocation(mLocation);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void updateUserInterface() {
        if (mWeatherData != null) {
            mToolbar.setSubtitle(mWeatherData.getCity().getName());
            mTextView.setText(mWeatherData.getList().get(0).getMain().getTemp()
                    + " " + mWeatherData.getList().get(0).getWeather().get(0).getMain());
        }
    }
}