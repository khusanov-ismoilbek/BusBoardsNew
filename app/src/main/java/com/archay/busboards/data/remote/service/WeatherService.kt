package com.archay.busboards.data.remote.service

import com.archay.busboards.data.remote.response.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("forecast.json")
    fun getWeather(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("days") days: Int
    ): Call<WeatherResponse>
}