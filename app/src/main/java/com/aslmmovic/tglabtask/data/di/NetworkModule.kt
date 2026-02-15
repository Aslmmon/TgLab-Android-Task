package com.aslmmovic.tglabtask.data.di

import android.content.Context
import com.aslmmovic.tglabtask.BuildConfig
import com.aslmmovic.tglabtask.data.remote.api.ApiConstants
import com.aslmmovic.tglabtask.data.remote.api.ApiHeaders
import com.aslmmovic.tglabtask.data.remote.api.NbaApi
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true // super important for real APIs
        isLenient = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient {
        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(ApiHeaders.AUTHORIZATION, BuildConfig.BALLDONTLIE_API_KEY)
                .build()
            chain.proceed(request)
        }
        val builder = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)

        if (BuildConfig.DEBUG) {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
                redactHeader(ApiHeaders.AUTHORIZATION)
            }
            val chuckerInterceptor = ChuckerInterceptor.Builder(context)
                .alwaysReadResponseBody(true)
                .build()

            require(!BuildConfig.BALLDONTLIE_API_KEY.isBlank()) {
                ApiHeaders.ErrorMsgMissingApiKey
            }

            builder.addInterceptor(logger)
                .addInterceptor(chuckerInterceptor)   // UI Inspector

        }
        return builder.build()

    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideNbaApi(retrofit: Retrofit): NbaApi =
        retrofit.create(NbaApi::class.java)
}
