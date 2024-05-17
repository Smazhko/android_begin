package ru.gb.m14_retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

private const val BASE_URL =  "https://randomuser.me/api/"

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val getUserAPI: GetUserAPI by lazy {
        retrofit.create(GetUserAPI::class.java)
    }
}

interface GetUserAPI {
    @Headers(
        "Accept: application/json",
        "Content-type: application/json"
    )
    @GET("?")
    suspend fun getUser(): UserResponse
}