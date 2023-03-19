package com.example.weathernowapp.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.weathernowapp.models.WeatherModel
import com.example.weathernowapp.R
import com.example.weathernowapp.utilities.ApiUtilities
import com.example.weathernowapp.databinding.ActivityMain2Binding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Response
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class MainActivity2 : AppCompatActivity() {

    lateinit var binding:ActivityMain2Binding

    private lateinit var currentLocation: Location

    private lateinit var fusedLocationProvider:FusedLocationProviderClient

    private val LOCATION_REQUEST_CODE=101

    private val apiKey="7df093c596143ab7b6dd15e0be85bee0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main2)

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)

        getCurrentLocation()

        binding.searchcity.setOnEditorActionListener { textView, i, keyEvent ->

            if (i==EditorInfo.IME_ACTION_SEARCH){

                getCityWeather(binding.searchcity.text.toString())

                val view = this.currentFocus

                if (view!=null){

                    val imm:InputMethodManager = getSystemService(INPUT_METHOD_SERVICE)
                            as InputMethodManager

                    imm.hideSoftInputFromWindow(view.windowToken, 0)

                    binding.searchcity.clearFocus()
                }

                return@setOnEditorActionListener true
            }

            else{
                return@setOnEditorActionListener false
            }
        }

        binding.currentlocation.setOnClickListener{

            getCurrentLocation()

        }
    }

    private fun getCityWeather(city:String){

        binding.progressbar.visibility = View.VISIBLE

        ApiUtilities.getApiInterface()?.getCityWeatherData(city, apiKey)
            ?.enqueue(object :retrofit2.Callback<WeatherModel>{
                override fun onResponse(
                    call: Call<WeatherModel>,
                    response: Response<WeatherModel>
                ) {

                    if (response.isSuccessful){

                        binding.progressbar.visibility = View.GONE

                        response.body()?.let {

                            setData(it)
                        }
                    }
                    else{

                        Toast.makeText(this@MainActivity2, "No city found", Toast.LENGTH_SHORT).show()

                        binding.progressbar.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<WeatherModel>, t:Throwable){

                }
            }
        )
    }

    private fun fetchCurrentLocationWeather(latitude:String, longitude:String){

        ApiUtilities.getApiInterface()?.getCurrentWeatherData(latitude,longitude,apiKey)
            ?.enqueue(object :retrofit2.Callback<WeatherModel>{
                override fun onResponse(
                    call: Call<WeatherModel>,
                    response: Response<WeatherModel>
                ){

                    if (response.isSuccessful){

                        binding.progressbar.visibility = View.GONE

                        response.body()?.let {

                            setData(it)
                        }
                    }
                    else{

                        Toast.makeText(this@MainActivity2, "No city found", Toast.LENGTH_SHORT).show()

                        binding.progressbar.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<WeatherModel>, t:Throwable){

                }
            })
    }

    private fun getCurrentLocation(){

        if (checkPermissions()){

            if (isLocationEnabled()){

                if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    )!=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    )!=PackageManager.PERMISSION_GRANTED
                ){

                    requestPermission()

                    return
                }

                fusedLocationProvider.lastLocation
                    .addOnSuccessListener { location->

                        if (location!=null){

                            currentLocation = location

                            binding.progressbar.visibility = View.VISIBLE

                            fetchCurrentLocationWeather(
                                location.latitude.toString(),
                                location.longitude.toString()
                            )
                        }
                    }
            }

            else{

                val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)

                startActivity(intent)
            }
        }

        else{

            requestPermission()
        }
    }

    private fun requestPermission(){

        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_REQUEST_CODE
        )
    }

    private fun isLocationEnabled():Boolean {

        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE)
                as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                ||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions():Boolean{

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )==PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )==PackageManager.PERMISSION_GRANTED){

            return true
        }

        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode==LOCATION_REQUEST_CODE){

            if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                getCurrentLocation()

            }

            else{

            }
        }
    }

    private fun  setData(body:WeatherModel){

        binding.apply {

            val currentDate = SimpleDateFormat("dd/MM/yyyy hh:mm").format(Date())

            time.text=currentDate.toString()

            maxtemp.text = "Max " + k2c(body?.main?.temp_max!!) + "°"

            mintemp.text = "Min " + k2c(body?.main?.temp_min!!) + "°"

            temp.text = "" + k2c(body?.main?.temp!!) + "°"

            weathertitle.text = body.weather[0].main

            pressuretext.text = body.main.pressure.toString() + " mbar"

            humiditytext.text = body.main.humidity.toString() + "%"

            tempinftext.text = "" + k2c(body.main.temp).times(1.8).plus(32)
                .roundToInt() + "℉"

            searchcity.setText(body.name)

            feelslike.text = "Feels like " + k2c(body?.main?.feels_like!!) + "°"

            windtext.text = body.wind.speed.toString() + " m/s"
        }

        updateUI(body.weather[0].id)
    }

    private fun updateUI(id: Int) {


        binding.apply {

            when(id){

                in 200..232->{

                    weatherimg.setImageResource(R.drawable.ic_storm_weather)

                    mainlayout.background=ContextCompat
                        .getDrawable(this@MainActivity2, R.drawable.thunderstorm_bg)

                    optionslayout.background=ContextCompat
                        .getDrawable(this@MainActivity2, R.drawable.thunderstorm_bg)

                }

                in 300..321->{

                    weatherimg.setImageResource(R.drawable.ic_few_clouds)

                    mainlayout.background=ContextCompat
                        .getDrawable(this@MainActivity2, R.drawable.drizzle_bg)

                    optionslayout.background=ContextCompat
                        .getDrawable(this@MainActivity2, R.drawable.drizzle_bg)

                }

                in 500..531->{

                    weatherimg.setImageResource(R.drawable.ic_rainy_weather)

                    mainlayout.background=ContextCompat
                        .getDrawable(this@MainActivity2, R.drawable.rain_bg)

                    optionslayout.background=ContextCompat
                        .getDrawable(this@MainActivity2, R.drawable.rain_bg)

                }

                in 600..622->{

                    weatherimg.setImageResource(R.drawable.ic_snow_weather)

                    mainlayout.background=ContextCompat
                        .getDrawable(this@MainActivity2, R.drawable.snow_bg)

                    optionslayout.background=ContextCompat
                        .getDrawable(this@MainActivity2, R.drawable.snow_bg)

                }

                in 701..781->{

                    weatherimg.setImageResource(R.drawable.ic_broken_clouds)

                    mainlayout.background=ContextCompat
                        .getDrawable(this@MainActivity2, R.drawable.atmosphere_bg)

                    optionslayout.background=ContextCompat
                        .getDrawable(this@MainActivity2, R.drawable.atmosphere_bg)

                }

                800->{

                    weatherimg.setImageResource(R.drawable.ic_clear_day)

                    mainlayout.background=ContextCompat
                        .getDrawable(this@MainActivity2, R.drawable.clear_bg)

                    optionslayout.background=ContextCompat
                        .getDrawable(this@MainActivity2, R.drawable.clear_bg)

                }

                in 801..804->{

                    weatherimg.setImageResource(R.drawable.ic_cloudy_weather)

                    mainlayout.background=ContextCompat
                        .getDrawable(this@MainActivity2, R.drawable.clouds_bg)

                    optionslayout.background=ContextCompat
                        .getDrawable(this@MainActivity2, R.drawable.clouds_bg)


                }

                else->{

                    weatherimg.setImageResource(R.drawable.ic_unknown)

                    mainlayout.background=ContextCompat
                        .getDrawable(this@MainActivity2, R.drawable.unknown_bg)

                    optionslayout.background=ContextCompat
                        .getDrawable(this@MainActivity2, R.drawable.unknown_bg)


                }
            }
        }
    }

    private fun k2c(kelvinTemp: Double): Double {

        val celsiusTemp = kelvinTemp.minus(273)

        return celsiusTemp.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()

    }
}