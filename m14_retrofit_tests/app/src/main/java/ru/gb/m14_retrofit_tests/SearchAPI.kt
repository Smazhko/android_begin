package ru.gb.m14_retrofit_tests

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

private const val BASE_URL =  "https://randomuser.me/api/"

object RetrofitInstance {
    //val gson = GsonBuilder().setLenient().create()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val searchApi: SearchApi = retrofit.create(SearchApi::class.java)
}

interface SearchApi {
    @Headers(
        "Accept: application/json",
        "Content-type: application/json"
    )
    @GET("?")
    fun getUser(): Call<UserResponse>
}