package com.wreckingball.whatsitlikeoutside.networking

import com.wreckingball.whatsitlikeoutside.data.TemperatureResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("weather")
    fun getTemperature(
        @Query("q") location: String,
        @Query("units") units: String,
        @Query("APPID") appId: String
    ): Call<TemperatureResponse>
}