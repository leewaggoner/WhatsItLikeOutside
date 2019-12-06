package com.wreckingball.whatsitlikeoutside.models

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.wreckingball.whatsitlikeoutside.data.CityRepository
import com.wreckingball.whatsitlikeoutside.listeners.CityRepositoryListener
import kotlinx.coroutines.*
import org.koin.core.KoinComponent

class RulesViewModel(private val cityRepository: CityRepository, application: Application) :
        AndroidViewModel(application), KoinComponent {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun getCities(context: Context) {
        cityRepository.setCallback(object : CityRepositoryListener {
            override fun onGotCities() {
                //hide loading dialogue
            }
        })

        uiScope.launch {
            getCitiesFromRepository(context)
        }
    }

    private suspend fun getCitiesFromRepository(context: Context) {
        return withContext(Dispatchers.IO) {
            cityRepository.fetchCities(context)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}