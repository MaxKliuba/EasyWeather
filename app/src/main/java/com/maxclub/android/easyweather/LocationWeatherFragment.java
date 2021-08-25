package com.maxclub.android.easyweather;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.maxclub.android.easyweather.api.WeatherApi;
import com.maxclub.android.easyweather.data.WeatherData;

import org.jetbrains.annotations.NotNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LocationWeatherFragment extends Fragment {

    private static final String TAG = "LocationWeatherFragment";

    private static final String[] LOCATION_PERMISSION = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };
    private static final int REQUEST_LOCATION_PERMISSION = 0;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLocation;
    private WeatherApi mWeatherApi = WeatherApi.Instance.getApi();
    private WeatherData mWeatherData;
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
            // error message
            Toast.makeText(getActivity(), "Google Play Services are not available", Toast.LENGTH_LONG).show();
        }
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_weather_fragment, container, false);

        mTextView = (TextView) view.findViewById(R.id.weather_textview);

        updateUserInterface();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        disconnectFromGoogleApiClient();
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
                    // go to settings button
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressLint("MissingPermission")
    private void registerLocationRequestListener() {
        if (mLocationRequest == null) {
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(10000);
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull @NotNull Location location) {
                Log.i(TAG, "onLocationChanged: " + location.getLatitude() + ", " + location.getLongitude());
                mLocation = location;
                updateWeather();
            }
        });
    }

    @SuppressLint("CheckResult")
    private void getWeatherByLocation(Location location) {
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
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, e.getMessage(), e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updateWeather() {
        if (mLocation != null) {
            getWeatherByLocation(mLocation);
        }
    }

    private void updateUserInterface() {
        if (mWeatherData != null) {
            mTextView.setText(mWeatherData.getCity().getName()
                    + " " + mWeatherData.getList().get(0).getMain().getTemp()
                    + " " + mWeatherData.getList().get(0).getWeather().get(0).getMain());
        }
    }
}