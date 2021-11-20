package com.example.pracainzynierska.network

import android.content.Context
import android.util.Log
import com.example.pracainzynierska.datastore.PrefsStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptorOkHttpClient @Inject constructor(
    @ApplicationContext private val appContext: Context,
): Interceptor  {

    private val prefsStore = PrefsStore(appContext)

    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val httpUrl: HttpUrl = original.url
        var tokenString = ""
        runBlocking {
            tokenString = prefsStore.getToken.first()
            Log.d("debuglog", "interceptor $tokenString")

        }
        val url: HttpUrl = httpUrl.newBuilder()
            //.addQueryParameter("Authorization", "$tokenString")
            .build()
        val request: Request = original.newBuilder()
            .header("Authorization", "${tokenString}")
            .url(url)
            .build()
        return chain.proceed(request)
    }
}