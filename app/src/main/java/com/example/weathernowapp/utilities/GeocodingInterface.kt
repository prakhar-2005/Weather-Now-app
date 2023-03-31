package com.example.weathernowapp.utilities

import com.example.weathernowapp.airpollution2.AirModel
import com.example.weathernowapp.geocodemodels.GeocodingModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingInterface {

    @GET("direct")
    fun getCoordForCityName(
        @Query("q") q:String,
        @Query("APPID") appid:String
    ): Call<GeocodingModel>
}