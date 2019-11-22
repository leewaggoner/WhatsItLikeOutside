package com.wreckingball.whatsitlikeoutside.models

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wreckingball.whatsitlikeoutside.utils.PreferencesWrapper
import org.koin.core.KoinComponent
import org.koin.core.inject

private const val HIGH_SCORE_KEY = "highScoreKey"

class EndViewModel : ViewModel(), KoinComponent {
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
}