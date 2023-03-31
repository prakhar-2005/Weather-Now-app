package com.example.weathernowapp.geocodemodels

data class GeocodingModelItem(
    val country: String,
    val lat: Double,
    val local_names: LocalNames,
    val lon: Double,
    val name: String,
    val state: String?
)