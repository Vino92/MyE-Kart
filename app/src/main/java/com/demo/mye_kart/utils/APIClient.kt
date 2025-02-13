package com.demo.mye_kart.utils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APIClient {
    fun getClient(): Retrofit {
        val client: OkHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)
            .callTimeout(2, TimeUnit.MINUTES)
            .build()
        return Retrofit.Builder().baseUrl(Constant.hostName)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    }

}