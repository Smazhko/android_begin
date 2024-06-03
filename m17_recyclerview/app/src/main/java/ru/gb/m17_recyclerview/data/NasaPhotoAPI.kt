package ru.gb.m17_recyclerview.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

private const val BASE_URL =  "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/"

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val getNasaPhotoAPI: NasaPhotoAPI by lazy {
        retrofit.create(NasaPhotoAPI::class.java)
    }
}


interface NasaPhotoAPI {
    @Headers(
        "Accept: application/json",
        "Content-type: application/json"
    )
    @GET("photos")
    suspend fun getPhotoList(
        @Query("sol") sol: String = "48",
//        @Query("earth_date") earthDate: String = "2022-1-1",
        @Query("api_key") apiKey: String = "EOhdooJnykCUVcHiph2xoa9FHbIT9ZIOMk7Ad7C5"
    ): ListPhotoDTO
}