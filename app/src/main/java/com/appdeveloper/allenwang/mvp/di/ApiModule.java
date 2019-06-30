package com.appdeveloper.allenwang.mvp.di;

import com.appdeveloper.allenwang.mvp.data.RetrofitApi;
import com.appdeveloper.allenwang.mvp.data.ServiceApi;
import com.appdeveloper.allenwang.mvp.data.ServiceApiImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    private static final String BASE_URL = "http://www.omdbapi.com/";

    @Singleton
    @Provides
    ServiceApi provideServiceApi(RetrofitApi retrofitApi) {
        return new ServiceApiImpl(retrofitApi);
    }

    @Singleton
    @Provides
    RetrofitApi provideRetrofitApi(OkHttpClient client, GsonConverterFactory gson, RxJava2CallAdapterFactory rxAdapter)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(gson)
                .addCallAdapterFactory(rxAdapter)
                .build();

        return  retrofit.create(RetrofitApi.class);
    }
}
