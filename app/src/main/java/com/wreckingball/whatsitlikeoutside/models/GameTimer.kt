package com.wreckingball.whatsitlikeoutside.models

import android.content.Context
import android.os.CountDownTimer
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import com.wreckingball.whatsitlikeoutside.R
import com.wreckingball.whatsitlikeoutside.utils.BEEP
import com.wreckingball.whatsitlikeoutside.utils.Sounds
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.TimeUnit

private const val TIMER_INTERVAL = 10L
private const val MAX_TIME = (10L * 1000L)
private const val TIME_ALMOST_UP = "3"

interface GameTimerListener {
    fun onEndOfRound()
}

class GameTimer : KoinComponent {
    private lateinit var progressCircle: ProgressBar
    private lateinit var timeView: TextView
    private lateinit var listener: GameTimerListener
    private lateinit var countDownTimer: CountDownTimer
    private var flag = false
    private val sounds: Sounds by inject()
    var timeLeft = ObservableField("")

    fun initialize(context: Context, progressCircle: ProgressBar, timeView: TextView, listener: GameTimerListener) {
        this.progressCircle = progressCircle
        this.timeView = timeView
        this.listener = listener

        countDownTimer = object : CountDownTimer(MAX_TIME, TIMER_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                progressCircle.progress = (millisUntilFinished / 10).toInt()
                val time = timeFormatter(millisUntilFinished)
                if (!timeLeft.get().equals(time)) {
                    timeLeft.set(time)
                    if (time.toInt() <= TIME_ALMOST_UP.toInt()) {
                        sounds.play(BEEP)
                    }
                }
                if (time.equals(TIME_ALMOST_UP, true) && !flag) {
                    flag = true
                    progressCircle.progressDrawable = ContextCompat.getDrawable(context, R.drawable.circle_red)
                }
            }

            override fun onFinish() {
                timeLeft.set("0")
                listener.onEndOfRound()
            }
        }
    }

    fun showTimer(show: Boolean) {
        if (show) {
            progressCircle.visibility = View.VISIBLE
            timeView.visibility = View.VISIBLE
        } else {
            progressCircle.visibility = View.INVISIBLE
            timeView.visibility = View.INVISIBLE
        }
    }

    fun reset(context: Context) {
        flag = false
        progressCircle.progressDrawable = ContextCompat.getDrawable(context, R.drawable.circle_yellow)
        timeLeft.set("0")
    }

    fun start() {
        countDownTimer.start()
    }

    private fun timeFormatter(milliSeconds: Long): String {
        val adjustedMillis = milliSeconds + 1000
        return String.format("%d", TimeUnit.MILLISECONDS.toSeconds(adjustedMillis))
    }
}