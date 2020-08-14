package com.amamode.realestatemanager.di

import android.os.Build
import com.amamode.realestatemanager.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkModule {
    companion object {
        fun getRetrofit(): Retrofit {
            val builder = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                builder.client(getOkHttpClient())
            }

            return builder.build()
        }

        private fun getOkHttpClient(): OkHttpClient {
            val okHttpClient = OkHttpClient.Builder()
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)

            if (BuildConfig.DEBUG) {
                okHttpClient.addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
            return okHttpClient.build()
        }
    }
}
