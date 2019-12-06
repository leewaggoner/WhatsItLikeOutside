package com.wreckingball.whatsitlikeoutside.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.wreckingball.whatsitlikeoutside.networking.RetrofitService
import com.wreckingball.whatsitlikeoutside.networking.WeatherAPI
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response


private const val TAG = "Networking"

object TemperatureRepository {
    private var temperatureApi: WeatherAPI =
        RetrofitService.createService(
            WeatherAPI::class.java
        )

    fun getTemperature(location: String) : MutableLiveData<TemperatureResponse> {
        val temperatureResponse = MutableLiveData<TemperatureResponse>()
        Log.d(TAG, "Network call openweathermap")
        val call = temperatureApi.getTemperature(location, "imperial", "bb1a3462fe01930352f5a8eacfb873b4")
        call.enqueue(object : retrofit2.Callback<TemperatureResponse> {
            override fun onResponse(call: Call<TemperatureResponse>, response: Response<TemperatureResponse>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Network response successful")
                    temperatureResponse.value = response.body()
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        val errMessage = jObjError.getJSONObject("error").getString("message")
                        Log.d(TAG, "Network response unsuccessful. Code: " +
                                response.code() + "\n" +
                                errMessage + "\n" +
                                response.errorBody()?.string())
                    } catch (e: Exception) {
                    }
                    temperatureResponse.value = null
                }
            }
            override fun onFailure(call: Call<TemperatureResponse>, t: Throwable) {
                Log.d(TAG, "Network response failed", t)
                temperatureResponse.value = null
            }
        })
        return temperatureResponse
    }
}