package com.wreckingball.whatsitlikeoutside.data

import android.content.Context
import com.wreckingball.whatsitlikeoutside.R
import com.wreckingball.whatsitlikeoutside.listeners.CityRepositoryListener
import com.wreckingball.whatsitlikeoutside.utils.getJsonObjects
import kotlin.random.Random

class CityRepository(private val cityDao: CityDao) {
    private lateinit var cities: MutableList<City>
    private lateinit var listener: CityRepositoryListener

    fun setCallback(cityRepositoryListener: CityRepositoryListener) {
        listener = cityRepositoryListener
    }

    fun getCities() : List<City> {
        cities.shuffle(Random(System.currentTimeMillis()))
        return cities
    }

    fun fetchCities(context: Context) {
        cityDao.deleteAll()
        loadCities(context)
        val cityList = cityDao.getCities()
        val mutableCities = cityList.toMutableList()
        cities = mutableCities
        listener.onGotCities()
    }

    fun deleteCities() {
        cityDao.deleteAll()
    }

    private fun loadCities(context: Context) {
        //get cities from raw data -- later it could be from a server
        val cityArray: Cities? = getJsonObjects(context, R.raw.cities)
        val rawCities = cityArray?.cities?.toList()
        rawCities?.let {
            putCities(rawCities)
        }
    }

    private fun putCities(cities: List<City>) {
        if (cities.isNotEmpty()) {
            cityDao.insertAll(cities)
        }
    }
}