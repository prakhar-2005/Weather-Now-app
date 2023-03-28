package com.example.weathernowapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.weathernowapp.R
import com.example.weathernowapp.airpollution2.AirModel

class AqiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aqi)
    }

//    private fun setAqiData(body: AirModel){
//
//        binding.apply {
//
//            aqibutton.text = "AQI: " + body.list[0].main.aqi.toString()
//
//            Log.e("body", "$body")
//        }
//    }
}