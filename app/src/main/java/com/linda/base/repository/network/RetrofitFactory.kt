package com.linda.base.repository.network

import com.linda.base.utils.AppConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitFactory {

    private val serviceMap = hashMapOf<String, Retrofit>()

    fun getRetrofitService(baseUrl: String): Retrofit {
        serviceMap[baseUrl]?.let {
            return it
        }
        val client = OkHttpClient.Builder()
            .connectTimeout(AppConfig.NET_TIME_OUT_MS, TimeUnit.MILLISECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        serviceMap[baseUrl] = retrofit
        return retrofit
    }

}