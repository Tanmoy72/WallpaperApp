package com.paul.wallpaperapp.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory




object RetrofitInstance {
    private const val BASE_URL = "https://api.pexels.com/v1/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor("563492ad6f91700001000001f152288f93984c1996a39446aae0ab94")) // Replace with your API key
        .build()


    val api: PexelsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PexelsApiService::class.java)
    }
}