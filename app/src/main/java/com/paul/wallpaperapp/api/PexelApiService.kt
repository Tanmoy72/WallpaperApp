package com.paul.wallpaperapp.api



import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PexelsApiService {
    //curated/?page=20&per_page=100
    @GET("curated")
    fun getCuratedPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<ResponseBody> // Returns raw ResponseBody using Call



    ///search?query=tree
    @GET("search?")
    fun getSearchPhotos(@Query("query")searchTxt: String): Call<ResponseBody>
}