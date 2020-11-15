package com.gonzoapps.asteroidradar.network

import com.gonzoapps.asteroidradar.util.Constants.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.reflect.Type

interface NasaApiService {
    @Scalar
    @GET("neo/rest/v1/feed")
    suspend fun getNEoWsListAsync(
        @Query("start_date")
        start: String,
        @Query("end_date")
        end: String,
        @Query("api_key")
        key: String,
    ) : Response<String>

    @Json
    @GET("planetary/apod")
    suspend fun getPictureOfTheDayAsync(
        @Query("date")
        date: String,
        @Query("api_key")
        key: String,
    ) : Response<NetworkPictureOfDay>
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
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(okHttpClient)

    val retrofitService:NasaApiService by lazy {
        retrofit
            .addConverterFactory(HandleScalarAndJsonConverterFactory.create())
            .build().create(NasaApiService::class.java)
    }
}

class HandleScalarAndJsonConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        annotations.forEach { annotation ->
            return when (annotation) {
                is Scalar -> ScalarsConverterFactory.create().responseBodyConverter(type, annotations, retrofit)
                is Json -> MoshiConverterFactory.create(moshi).responseBodyConverter(type, annotations, retrofit)
                else -> null
            }
        }
        return null
    }
    companion object {
        fun create()  = HandleScalarAndJsonConverterFactory()
    }
}