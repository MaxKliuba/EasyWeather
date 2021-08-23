package com.maxclub.android.easyweather.api;

import com.maxclub.android.easyweather.data.WeatherData2;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi2 {
    String BASE_URL = "https://dataservice.accuweather.com/";
    String API_KEY = "mitmhqwJTnlJgCSoGbQaW7RYbugFoQLG";

    @GET("forecasts/v1/daily/5day/324505")
    Observable<WeatherData2> getWeatherDataByCity(@Query("apikey") String apiKey,
                                                  @Query("language") String language,
                                                  @Query("details") boolean details,
                                                  @Query("metric") boolean isMetric);

    class Instance {
        public static WeatherApi2 getApi() {
            return getRetrofit().create(WeatherApi2.class);
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
