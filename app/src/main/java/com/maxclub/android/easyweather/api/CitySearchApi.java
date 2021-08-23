package com.maxclub.android.easyweather.api;

import com.maxclub.android.easyweather.data.SearchData;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CitySearchApi {
    String BASE_URL = "https://dataservice.accuweather.com/";
    String API_KEY = "mitmhqwJTnlJgCSoGbQaW7RYbugFoQLG";

    @GET("locations/v1/cities/search")
    Observable<List<SearchData>> getWeatherDataByCity(@Query("apikey") String apiKey,
                                                      @Query("q") String city,
                                                      @Query("language") String language,
                                                      @Query("details") boolean details);

    class Instance {
        public static CitySearchApi getApi() {
            return getRetrofit().create(CitySearchApi.class);
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
