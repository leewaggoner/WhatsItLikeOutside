package com.wreckingball.whatsitlikeoutside.models

import android.app.Application
import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.wreckingball.whatsitlikeoutside.data.CityRepository
import com.wreckingball.whatsitlikeoutside.listeners.CityRepositoryListener
import com.wreckingball.whatsitlikeoutside.utils.PreferencesWrapper
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject

private const val HIGH_SCORE_KEY = "highScoreKey"

class EndViewModel(private val cityRepository: CityRepository, application: Application) :
        AndroidViewModel(application), KoinComponent {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val preferencesWrapper: PreferencesWrapper by inject()
    var score = ObservableField("")
    var highScore = ObservableField("")

    fun calcHighScore(score: Int) {
        var currentHighScore = preferencesWrapper.getInt(HIGH_SCORE_KEY, 0)
        if (score > currentHighScore) {
            preferencesWrapper.putInt(HIGH_SCORE_KEY, score)
            currentHighScore = score
        }

        this.score.set(score.toString())
        this.highScore.set(currentHighScore.toString())
    }

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