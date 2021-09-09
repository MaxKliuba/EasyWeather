package com.maxclub.easyweather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.maxclub.easyweather.api.WeatherApi;
import com.maxclub.easyweather.api.model.WeatherData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class LocationWeatherFragment extends Fragment {

    private static final String TAG = "LocationWeatherFragment";

    private static final String[] LOCATION_PERMISSION = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };
    private static final int REQUEST_LOCATION_PERMISSION = 0;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private Location mLocation;
    private boolean mIsLocationUpdatesRegistered = false;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private WeatherApi mWeatherApi = WeatherApi.Instance.getApi();
    private WeatherData mWeatherData;

    private View mGooglePlayServicesNotFoundViewContainer;
    private View mPermissionViewContainer;
    private View mLocationEnablingContainer;
    private View mConnectionErrorContainer;
    private View mWaitingForDataViewContainer;
    private View mMainContentContainer;
    private List<View> mViewContainers = new ArrayList<>();

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTextView;

    public static LocationWeatherFragment newInstance() {
        return new LocationWeatherFragment();
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
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        mGooglePlayServicesNotFoundViewContainer =
                (View) view.findViewById(R.id.google_play_services_not_found_view_container);
        mGooglePlayServicesNotFoundViewContainer.setVisibility(View.GONE);
        ImageView googlePlayServicesNotFoundImageView =
                (ImageView) view.findViewById(R.id.google_play_services_not_found_image_view);
        Glide.with(getActivity())
                .asGif()
                .load(R.raw.gif_google_play_services_not_found)
                .into(googlePlayServicesNotFoundImageView);

        mPermissionViewContainer = (View) view.findViewById(R.id.permission_view_container);
        mPermissionViewContainer.setVisibility(View.GONE);
        ImageView permissionImageView = (ImageView) view.findViewById(R.id.permission_image_view);
        Glide.with(getActivity())
                .asGif()
                .load(R.raw.gif_permission)
                .into(permissionImageView);
        Button settingsButton = (Button) view.findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        mLocationEnablingContainer = (View) view.findViewById(R.id.location_enabling_view_container);
        mLocationEnablingContainer.setVisibility(View.GONE);
        ImageView locationEnablingImageView = (ImageView) view.findViewById(R.id.location_enabling_image_view);
        Glide.with(getActivity())
                .asGif()
                .load(R.raw.gif_permission)
                .into(locationEnablingImageView);
        Button locationEnablingButton = (Button) view.findViewById(R.id.location_enabling_button);
        locationEnablingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        mConnectionErrorContainer = (View) view.findViewById(R.id.connection_error_view_container);
        mConnectionErrorContainer.setVisibility(View.GONE);
        ImageView connectingErrorImageView = (ImageView) view.findViewById(R.id.connection_error_image_view);
        Glide.with(getActivity())
                .asGif()
                .load(R.raw.gif_connection_error)
                .into(connectingErrorImageView);
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

        mViewContainers.add(mGooglePlayServicesNotFoundViewContainer);
        mViewContainers.add(mPermissionViewContainer);
        mViewContainers.add(mLocationEnablingContainer);
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

        if (isGooglePlayServicesAvailable()) {
            if (hasLocationPermission()) {
                if (isLocationEnabled()) {
                    createLocationClient();
                } else {
                    setViewContainerVisible(mLocationEnablingContainer);
                }
            } else {
                setViewContainerVisible(mPermissionViewContainer);
                requestPermissions(LOCATION_PERMISSION, REQUEST_LOCATION_PERMISSION);
            }
        } else {
            setViewContainerVisible(mGooglePlayServicesNotFoundViewContainer);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mIsLocationUpdatesRegistered) {
            stopLocationUpdates();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mCompositeDisposable.dispose();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.location_weather_fragment, menu);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (mWeatherData != null) {
            activity.getSupportActionBar().setSubtitle(String.format("%s, %s",
                    mWeatherData.getCity().getName(), mWeatherData.getCity().getCountry()));
        } else {
            activity.getSupportActionBar().setSubtitle(null);
        }
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int errorCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());

        return errorCode == ConnectionResult.SUCCESS;
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
                    if (isLocationEnabled()) {
                        createLocationClient();
                    } else {
                        setViewContainerVisible(mLocationEnablingContainer);
                    }
                } else {
                    setViewContainerVisible(mPermissionViewContainer);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressLint("MissingPermission")
    private void createLocationClient() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.i(TAG, "getLastLocation() -> " + location.getLatitude() + ", " + location.getLongitude());
                            mLocation = location;
                            if (mWeatherData == null) {
                                updateWeather();
                            }
                        } else {
                            Log.i(TAG, "getLastLocation() -> null");
                        }
                    }
                });

        startLocationUpdates();

        if (mWeatherData == null) {
            setViewContainerVisible(mWaitingForDataViewContainer);
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.i(TAG, "onLocationResult() -> null");

                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Log.i(TAG, "onLocationChanged() -> " + location.getLatitude() + ", " + location.getLongitude());
                    mLocation = location;

                    if (mWeatherData == null) {
                        fetchWeatherByLocation(mLocation);
                    }
                }
            }
        };

        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.getMainLooper());
        mIsLocationUpdatesRegistered = true;
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        mIsLocationUpdatesRegistered = false;
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressLint("CheckResult")
    private void fetchWeatherByLocation(Location location) {
        mSwipeRefreshLayout.setRefreshing(true);
        mCompositeDisposable.clear();
        mCompositeDisposable.add(mWeatherApi.getWeatherData(location.getLatitude(), location.getLongitude(), 40, "metric", "en")
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
                            setViewContainerVisible(mConnectionErrorContainer);
                        }
                    }
                }));
    }

    private void updateWeather() {
        if (isGooglePlayServicesAvailable()) {
            if (hasLocationPermission()) {
                if (isLocationEnabled()) {
                    if (mWeatherData == null) {
                        setViewContainerVisible(mWaitingForDataViewContainer);
                    }

                    if (mLocation != null) {
                        fetchWeatherByLocation(mLocation);
                    } else {
                        if (!mIsLocationUpdatesRegistered) {
                            createLocationClient();
                        } else {
                            mSwipeRefreshLayout.setRefreshing(false);
                            setViewContainerVisible(mConnectionErrorContainer);
                        }
                    }
                } else {
                    setViewContainerVisible(mLocationEnablingContainer);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            } else {
                setViewContainerVisible(mPermissionViewContainer);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        } else {
            setViewContainerVisible(mGooglePlayServicesNotFoundViewContainer);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void updateUserInterface() {
        if (mWeatherData != null) {
            mTextView.setText(String.format("%s %s %s",
                    mWeatherData.getCity().getName(),
                    mWeatherData.getList().get(0).getMain().getTemp(),
                    mWeatherData.getList().get(0).getWeather().get(0).getMain()));

            setViewContainerVisible(mMainContentContainer);
        }

        getActivity().invalidateOptionsMenu();
    }

    private void setViewContainerVisible(View viewContainer) {
        for (View container : mViewContainers) {
            if (container.equals(viewContainer)) {
                container.setVisibility(View.VISIBLE);
            } else {
                container.setVisibility(View.GONE);
            }
        }
    }
}