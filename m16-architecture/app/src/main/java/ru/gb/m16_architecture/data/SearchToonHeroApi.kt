package ru.gb.m16_architecture.data

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

private const val BASE_URL =  "https://rickandmortyapi.com/api/"


object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val getToonHeroAPI: SearchToonHeroApi by lazy {
        retrofit.create(SearchToonHeroApi::class.java)
    }
}


interface SearchToonHeroApi {
    @Headers(
        "Accept: application/json",
        "Content-type: application/json"
    )
    @GET("character/{idArr}")
    suspend fun getTooHero(@Path("idArr") idArr: String = "1"): ToonHeroDTO
}