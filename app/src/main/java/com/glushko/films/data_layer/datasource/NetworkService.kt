package com.glushko.films.data_layer.datasource

import com.glushko.films.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

object NetworkService {
    private const val BASE_URL = "https://kinopoiskapiunofficial.tech/api/v2.2/"
    private const val API_KEY = "68171039-847a-4fb3-ae25-b084f1c6e7e"

    fun makeNetworkService(): ApiService {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
                .header("Content-Type", "application/json")
                .header("X-API-KEY", "68171039-847a-4fb3-ae25-b084f1c6e7eb")
                .method(original.method(), original.body())
                .build()

            chain.proceed(request)
        }
        val client = httpClient.addInterceptor(HttpLoggingInterceptor()
            .apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            }).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build().create(ApiService::class.java)
    }
}