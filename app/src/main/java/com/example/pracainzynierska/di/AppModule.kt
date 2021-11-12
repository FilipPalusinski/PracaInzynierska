package com.example.pracainzynierska.di

import android.content.Context
import com.example.pracainzynierska.datastore.PrefsStore
import com.example.pracainzynierska.network.ApiService
import com.example.pracainzynierska.network.AuthInterceptorOkHttpClient
import com.example.pracainzynierska.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
            level = HttpLoggingInterceptor.Level.HEADERS
        }


    @Singleton
    @Provides
    fun providesAuthInterceptor(@ApplicationContext appContext: Context): Interceptor = AuthInterceptorOkHttpClient(appContext)

    @Singleton
    @Provides
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        AuthInterceptorOkHttpClient : Interceptor
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(AuthInterceptorOkHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideApi(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }


}