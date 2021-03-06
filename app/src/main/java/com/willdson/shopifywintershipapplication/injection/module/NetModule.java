package com.willdson.shopifywintershipapplication.injection.module;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.willdson.shopifywintershipapplication.data.ShopifyAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dwson on 9/24/16.
 */

@Module
public class NetModule {

    String mBaseUrl;

    public NetModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    @Provides
    @Singleton
    Gson providesGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    StethoInterceptor providesStethoInterceptor() {
        return new StethoInterceptor();
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient(StethoInterceptor stethoInterceptor) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(stethoInterceptor)
                .build();
        return client;
    }

    @Provides
    @Singleton
    RxJavaCallAdapterFactory providesRxAdapter() {
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.create();
        return rxAdapter;
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(Gson gson, OkHttpClient okHttpClient, RxJavaCallAdapterFactory rxAdapter) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxAdapter)
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    ShopifyAPI providesShopifyAPI(Retrofit retrofit) {
        return retrofit.create(ShopifyAPI.class);
    }
}
