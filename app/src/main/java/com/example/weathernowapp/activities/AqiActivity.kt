package com.example.weathernowapp.activities

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.SubscriptSpan
import android.util.Log.e
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.weathernowapp.R
import com.example.weathernowapp.airpollution2.AirModel
import com.example.weathernowapp.databinding.ActivityAqiBinding
import com.example.weathernowapp.utilities.AirUtilities
import retrofit2.Call
import retrofit2.Response
import java.math.RoundingMode
import java.util.*

class AqiActivity : AppCompatActivity() {

    lateinit var binding: ActivityAqiBinding

    private val apiKey = "7df093c596143ab7b6dd15e0be85bee0"

    val so2string = "SO2"
    val so2stringspan = SpannableStringBuilder(so2string)
    val no2string = "NO2"
    val no2stringspan = SpannableStringBuilder(no2string)
    val pm10string = "PM10"
    val pm10stringspan = SpannableStringBuilder(pm10string)
    val pm25string = "PM2.5"
    val pm25stringspan = SpannableStringBuilder(pm25string)
    val o3string = "O3"
    val o3stringspan = SpannableStringBuilder(o3string)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aqi)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_aqi)

        val latval = intent.extras?.getString("latitude").toString()
        val lonval = intent.extras?.getString("longitude").toString()

        fetchCurrentAqiValues(latval, lonval)

        so2stringspan.setSpan(SubscriptSpan(), 2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        so2stringspan.setSpan(RelativeSizeSpan(0.8f), 2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        no2stringspan.setSpan(SubscriptSpan(), 2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        no2stringspan.setSpan(RelativeSizeSpan(0.8f), 2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        pm10stringspan.setSpan(SubscriptSpan(), 2, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        pm10stringspan.setSpan(RelativeSizeSpan(0.8f), 2, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        pm25stringspan.setSpan(SubscriptSpan(), 2, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        pm25stringspan.setSpan(RelativeSizeSpan(0.8f), 2, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        o3stringspan.setSpan(SubscriptSpan(), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        o3stringspan.setSpan(RelativeSizeSpan(0.8f), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    }

    private fun fetchCurrentAqiValues(latitude:String, longitude:String){

        binding.aqiprogressbar.visibility = View.VISIBLE

        AirUtilities.getAirInterface()?.getAirPollutionData(latitude,longitude,apiKey)
            ?.enqueue(object :retrofit2.Callback<AirModel>{
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(call: Call<AirModel>, response: Response<AirModel>) {

                    if (response.isSuccessful){

                        binding.aqiprogressbar.visibility = View.GONE

                        response.body()?.let {
                            e("body", "$it")
                            setAqidata(it)
                        }
                        if(response.body()==null) e("null", "null body")
                    }

                    else{

                        Toast.makeText(this@AqiActivity, "No AQI data found", Toast.LENGTH_SHORT).show()

                        binding.aqiprogressbar.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<AirModel>, t: Throwable) {
                    e("fail", t.message.toString())
                }
            })

    }

    @SuppressLint("SetTextI18n")
    private fun setAqidata(body: AirModel) {
        binding.apply {

            aqivalue.text = body.list[0].main.aqi.toString()
            so2text.text = so2stringspan
            so2value.text = body.list[0].components.so2.toBigDecimal().setScale(1, RoundingMode.UP).toString()
            no2text.text = no2stringspan
            no2value.text = body.list[0].components.no2.toBigDecimal().setScale(1, RoundingMode.UP).toString()
            pm10text.text = pm10stringspan
            pm10value.text = body.list[0].components.pm10.toBigDecimal().setScale(1, RoundingMode.UP).toString()
            pm25text.text = pm25stringspan
            pm25value.text = body.list[0].components.pm2_5.toBigDecimal().setScale(1, RoundingMode.UP).toString()
            o3text.text = o3stringspan
            o3value.text = body.list[0].components.o3.toBigDecimal().setScale(1, RoundingMode.UP).toString()
            covalue.text = body.list[0].components.co.toBigDecimal().setScale(1, RoundingMode.UP).toString()

            e("body", "$body")
        }

    }

}