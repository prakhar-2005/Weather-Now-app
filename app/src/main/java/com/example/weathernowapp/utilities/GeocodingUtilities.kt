package com.example.weathernowapp.utilities

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object GeocodingUtilities {

    private var retrofit: Retrofit?=null

    var BASE_URL_GEOCODING = "http://api.openweathermap.org/geo/1.0/"

    fun getGeocodingInterface(): GeocodingInterface? {

        if (retrofit == null) {

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_GEOCODING)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }

        return retrofit?.create(GeocodingInterface::class.java)

    }
}