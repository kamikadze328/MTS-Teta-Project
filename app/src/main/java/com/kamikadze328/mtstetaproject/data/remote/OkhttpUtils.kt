package com.kamikadze328.mtstetaproject.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


fun Retrofit.Builder.setClient(vararg defaultParameters: Pair<String, String>) = apply {
    val builder = OkHttpClient.Builder()

    if (defaultParameters.isNotEmpty()) {
        builder.addInterceptor {
            val request = it.request().newBuilder()
            val originalHttpUrl = it.request().url
            val url = originalHttpUrl.newBuilder()
            for (param in defaultParameters) {
                url.addQueryParameter(param.first, param.second)
            }
            request.url(url.build())

            return@addInterceptor it.proceed(request.build())
        }
    }

    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
    builder.addInterceptor(logging)

    builder.connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)

    this.client(builder.build())
}
