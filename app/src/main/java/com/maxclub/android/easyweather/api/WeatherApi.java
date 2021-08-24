package com.maxclub.android.easyweather.api;

import com.maxclub.android.easyweather.data.WeatherData;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    String BASE_URL = "https://api.openweathermap.org/";
    String API_KEY = "3488770e3e941c4894196cdea0e06a57";

    @GET("data/2.5/forecast")
    Observable<WeatherData> getWeatherDataByCity(@Query("q") String city,
                                                 @Query("appid") String apiKey,
                                                 @Query("cnt") int cnt,
                                                 @Query("units") String units,
                                                 @Query("lang") String lang);

    class Instance {
        public static WeatherApi getApi() {
            return getRetrofit().create(WeatherApi.class);
        }

        private static Retrofit getRetrofit() {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

            Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
            retrofitBuilder.baseUrl(BASE_URL);
            retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
            retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            retrofitBuilder.client(okHttpClientBuilder.build());

            return retrofitBuilder.build();
        }
    }
}
