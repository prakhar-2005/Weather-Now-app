package com.example.weathernowapp.forecastModels

data class ForecastModel(
    val alert: Alert,
    val current: Current,
    val forecast: Forecast,
    val location: Location
)