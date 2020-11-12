package com.gonzoapps.asteroidradar.network

import com.gonzoapps.asteroidradar.util.Constants.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface NasaApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getNEoWsListAsync(
        @Query("start_date")
        start: String,
        @Query("end_date")
        end: String,
        @Query("api_key")
        key: String,
    ) : String
}



private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


object NasaApi {

    private var loggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BASIC)

    private var okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(okHttpClient)
        .build()


    val retrofitService: NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }
}

