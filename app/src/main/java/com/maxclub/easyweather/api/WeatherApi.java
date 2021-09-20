package com.maxclub.easyweather.api;

import com.maxclub.easyweather.api.model.ForecastWeatherData;
import com.maxclub.easyweather.api.model.OneCallWeatherData;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import io.reactivex.Single;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("data/2.5/forecast")
    Single<ForecastWeatherData> getWeatherData(@Query("q") String city,
                                               @Query("cnt") int cnt,
                                               @Query("units") String units,
                                               @Query("lang") String lang);

    @GET("data/2.5/forecast")
    Single<ForecastWeatherData> getWeatherData(@Query("lat") double lat,
                                               @Query("lon") double lon,
                                               @Query("cnt") int cnt,
                                               @Query("units") String units,
                                               @Query("lang") String lang);

    @GET("data/2.5/onecall")
    Single<OneCallWeatherData> getOneCallWeatherData(@Query("lat") double lat,
                                                     @Query("lon") double lon,
                                                     @Query("exclude") String exclude,
                                                     @Query("units") String units,
                                                     @Query("lang") String lang);

    class Instance {
        private static final String BASE_URL = "https://api.openweathermap.org/";
        private static final String API_KEY = "3488770e3e941c4894196cdea0e06a57";

        public static WeatherApi getApi() {
            return getRetrofit().create(WeatherApi.class);
        }

        private static OkHttpClient getOkHttpClient() {
            final OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            okHttpClientBuilder.addInterceptor(new Interceptor() {
                @Override
                public @NotNull Response intercept(@NotNull Chain chain) throws IOException {
                    final Request original = chain.request();
                    final HttpUrl originalHttpUrl = original.url();
                    final HttpUrl url = originalHttpUrl.newBuilder()
                            .addQueryParameter("appid", API_KEY)
                            .build();
                    final Request.Builder requestBuilder = original.newBuilder()
                            .url(url);
                    final Request request = requestBuilder.build();

                    return chain.proceed(request);
                }
            });

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(loggingInterceptor);

            return okHttpClientBuilder.build();
        }

        private static Retrofit getRetrofit() {
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build();
        }
    }
}
