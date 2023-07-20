package com.endava.parking.di

import com.endava.parking.data.api.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieHandler
import java.net.CookieManager
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Named("baseUrl")
    @Provides
    fun provideBaseUrl() = "https://backend.parking-lot1.app.mddinternship.com/"

    @Singleton
    @Provides
    fun provideApi(@Named("baseUrl") baseUrl: String): ApiService =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(createOkHttpClient(MainInterceptor()))
            .build()
            .create(ApiService::class.java)

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        httpClient.cookieJar(JavaNetCookieJar(CookieManager()))
        httpClient.connectTimeout(15, TimeUnit.SECONDS)
        httpClient.readTimeout(20, TimeUnit.SECONDS)
        httpClient.writeTimeout(20, TimeUnit.SECONDS)
        httpClient.retryOnConnectionFailure(true)
        return httpClient.build()
    }

    inner class MainInterceptor: Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            return chain.proceed(chain.request())
        }
    }
}
