package com.example.weathernowapp.utilities

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AirUtilities {

    private var retrofit: Retrofit?=null

    var BASE_URL_AIR = "http://api.openweathermap.org/data/2.5/"

    fun getAirInterface():AqiInterface? {

        if (retrofit == null) {

            retrofit =Retrofit.Builder()
                .baseUrl(BASE_URL_AIR)
                .addConverterFactory(GsonConverterFactory.create()).build()

        }

        return retrofit?.create(AqiInterface::class.java)
    }
}