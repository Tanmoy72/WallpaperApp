package com.paul.wallpaperapp.api

import okhttp3.Interceptor
import okhttp3.Response

// Create an Interceptor for adding the Authorization header
class AuthInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", apiKey)
            .build()
        return chain.proceed(request)
    }
}