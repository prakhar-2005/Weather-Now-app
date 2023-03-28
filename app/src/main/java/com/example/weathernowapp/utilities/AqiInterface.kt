package com.example.weathernowapp.utilities

import com.example.weathernowapp.airpollution2.AirModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AqiInterface {

    @GET("air_pollution")
    fun getAirPollutionData(
        @Query("lat") lat:String,
        @Query("lon") lon:String,
        @Query("APPID") appid:String
    ): Call<AirModel>
}