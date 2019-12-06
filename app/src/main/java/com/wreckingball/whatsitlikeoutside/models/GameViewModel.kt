package com.wreckingball.whatsitlikeoutside.models

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.wreckingball.whatsitlikeoutside.R
import com.wreckingball.whatsitlikeoutside.data.CityRepository
import com.wreckingball.whatsitlikeoutside.data.GameTemperature
import com.wreckingball.whatsitlikeoutside.data.TemperatureRepository
import com.wreckingball.whatsitlikeoutside.data.TemperatureResponse
import com.wreckingball.whatsitlikeoutside.utils.LOSER
import com.wreckingball.whatsitlikeoutside.utils.Sounds
import com.wreckingball.whatsitlikeoutside.utils.VICTORY
import org.koin.core.KoinComponent
import org.koin.core.inject

enum class GameState {
    GAME_START,
    ROUND_START,
    ROUND_END,
    GAME_END
}

const val NO_CHOICE = "no_choice"
const val MAX_ROUNDS = 5

private const val CORRECT_SCORE = 1000
private const val TAG = "Networking"

class GameViewModel(val cityRepository: CityRepository, application: Application) :
        AndroidViewModel(application), KoinComponent {
    private var cities = cityRepository.getCities()
    private var gameState = MutableLiveData<GameState>()
    private var temperatureData = MutableLiveData<TemperatureResponse>()
    private lateinit var gameTemperature: GameTemperature
    private var roundScore = 0
    private var totalScore = 0
    private var currentRound = 0
    private val sounds: Sounds by inject()
    var roundText = ObservableField("")
    var totalText = ObservableField("")
    var curRound = ObservableField((currentRound + 1).toString())

    init {
        //set initial game state
        gameState.value = GameState.GAME_START
    }

    private fun restartGame() {
        cities = cityRepository.getCities()
        roundScore = 0
        totalScore = 0
        currentRound = 0
        roundText.set("")
        totalText.set("")
        curRound.set((currentRound + 1).toString())
    }

    fun initRound(activity: FragmentActivity) {
        if (currentRound >= MAX_ROUNDS) {
            restartGame()
        }
        curRound.set((currentRound + 1).toString())
        temperatureData = TemperatureRepository.getTemperature(cities[currentRound].weatherName)
        temperatureData.observe(activity, Observer {
            Log.d(TAG, "temperatureData change observed")
            roundScore = 0
            gameState.value = GameState.ROUND_START
        })
    }

    fun getGameState() : LiveData<GameState> {
        return gameState
    }

    fun getTotalScore() : Int {
        return totalScore
    }

    fun getRoundScore() : Int {
        return roundScore
    }

    fun getLocation() : String {
        return cities[currentRound].location
    }

    fun isGameOver() : Boolean {
        var value = false
        if (currentRound >= MAX_ROUNDS) {
            value = true
            gameState.value = GameState.GAME_END
        }
        return value
    }

    fun getGameTemperature() : GameTemperature {
        val name = temperatureData.value?.name
        val realTempD = temperatureData.value?.main?.temp?.toDouble()
        val realTemp = realTempD?.toInt()
        name?.let {
            realTemp?.let {
                gameTemperature =
                    GameTemperature(
                        name,
                        realTemp
                    )
                return gameTemperature
            }
        }
        gameTemperature =
            GameTemperature("Error", 0)
        return gameTemperature
    }

    fun endRound(chosenTemp: String) {
        roundScore = if (!chosenTemp.equals(NO_CHOICE, true) &&
            chosenTemp.toInt() == gameTemperature.getRealTemp()) {
            CORRECT_SCORE
        } else {
            0
        }
        sounds.play(if (roundScore == 0) LOSER else VICTORY)
        currentRound++
        totalScore += roundScore
        gameState.value = GameState.ROUND_END
    }

    fun getResultText(context: Context) : String {
        return if (roundScore == 0) {
            val sorry = context.getString(R.string.sorry)
            String.format(context.getString(R.string.result_text),
                sorry,
                gameTemperature.getLocation(),
                gameTemperature.getRealTemp())
        } else {
            val hooray = context.getString(R.string.hooray)
            String.format(context.getString(R.string.result_text),
                hooray,
                gameTemperature.getLocation(),
                gameTemperature.getRealTemp())
        }
    }

    fun getCorrectIndex() : Int {
        var idx = 0
        for (temp in gameTemperature.getTemps()) {
            if (temp == gameTemperature.getRealTemp()) {
                break
            }
            idx++
        }
        return idx
    }
}