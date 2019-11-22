package com.wreckingball.whatsitlikeoutside.fragments

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.wreckingball.whatsitlikeoutside.R
import com.wreckingball.whatsitlikeoutside.databinding.GameFragmentBinding
import com.wreckingball.whatsitlikeoutside.listeners.GameListener
import com.wreckingball.whatsitlikeoutside.models.*
import kotlinx.android.synthetic.main.game_fragment.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

const val OFF_SCREEN = 1000f
const val ANIM_DURATION = 1000L

class GameFragment : Fragment(R.layout.game_fragment), GameTimerListener {
    private var radioPos = 0f
    private val radioButtons = arrayOfNulls<RadioButton>(4)
    private var textPos = 0f
    private val model : GameViewModel by viewModel()
    private var gameListener: GameListener? = null
    private val gameTimer: GameTimer by inject()
    private lateinit var binding: GameFragmentBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GameListener) {
            gameListener = context
        } else {
            throw RuntimeException("$context must implement GameListener")

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        binding.vm = model
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameTimer.initialize(context!!, progressBarCircle, timeLeft, this)
        binding.timeData = gameTimer

        radioPos = radioButton1.x
        radioButtons[0] = radioButton1
        radioButtons[1] = radioButton2
        radioButtons[2] = radioButton3
        radioButtons[3] = radioButton4
        textPos = game_text.x

        model.getGameState().observe(this, Observer {
            gameState ->
            when(gameState) {
                GameState.ROUND_START -> {
                    startNewRound()                }
                GameState.ROUND_END -> {
                    displayRoundEndElements()
                }
                else -> {}
            }
        })

        nextRound.setOnClickListener {
            if (!model.isGameOver()) {
                initNewRound()
            } else {
                clearRadioButtons()
                gameListener?.endGame(model.getTotalScore())
            }
        }

        initNewRound()
    }

    private fun initNewRound() {
        nextRound.visibility = View.INVISIBLE
        clearRadioButtons()
        gameTimer.reset(context!!)
        showLoadingDialog()
        model.initRound(activity!!)
    }

    private fun clearRadioButtons() {
        radioGroup.clearCheck()
        for (i in 0..3) {
            radioButtons[i]?.visibility = View.INVISIBLE
            radioButtons[i]?.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        }
    }

    private fun startNewRound(/*temperatureResponse: TemperatureResponse*/) {
        hideLoadingDialog()
        val gameTemp = model.getGameTemperature()

        //set up game text
        game_text.text = String.format(getText(R.string.game_text).toString(), model.getLocation())
        game_text.visibility = View.VISIBLE

        //set up choices
        val gameTemps = gameTemp.getTemps()
        for (i in 0..3) {
            radioButtons[i]?.text = gameTemps[i].toString()
        }
        startAnimations()
    }

    private fun startAnimations() {
        val handler = Handler()
        var delay = 0L
        for (radio in radioButtons) {
            handler.postDelayed({ animateRadioButton(radio!!) }, delay)
            delay += 500
        }
    }

    private fun animateRadioButton(radioButton: RadioButton) {
        radioButton.x = radioButton.x + OFF_SCREEN
        radioButton.visibility = View.VISIBLE
        radioButton.isEnabled = true
        val anim = ObjectAnimator.ofFloat(radioButton, "translationX", radioPos)
        anim.duration = ANIM_DURATION
        anim.start()
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }
            override fun onAnimationRepeat(animation: Animator?) {
            }
            override fun onAnimationCancel(animation: Animator?) {
            }
            override fun onAnimationEnd(animation: Animator?) {
                if (radioButton.id == radioButton4.id) {
                    gameTimer.showTimer(true)
                    gameTimer.start()
                }
            }
        })
    }

    private fun showLoadingDialog() {
        progressBar.visibility = View.VISIBLE
        activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun hideLoadingDialog() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        progressBar.visibility = View.INVISIBLE
    }

    override fun onEndOfRound() {
        for (i in 0..3) {
            radioButtons[i]?.isEnabled = false
        }
        var chosenTemp = NO_CHOICE
        if (radioGroup.checkedRadioButtonId != -1) {
            val radioButton: RadioButton = radioGroup.findViewById(radioGroup.checkedRadioButtonId)
            chosenTemp = radioButton.text.toString()
        }
        model.endRound(chosenTemp)
    }

    private fun displayRoundEndElements() {
        slideView(game_text, -OFF_SCREEN) {showResulText()}
    }

    private fun slideView(view: View, toX: Float, end: () -> Unit) {
        val anim = ObjectAnimator.ofFloat(view, "translationX", toX)
        anim.duration =1000
        anim.start()
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }
            override fun onAnimationRepeat(animation: Animator?) {
            }
            override fun onAnimationCancel(animation: Animator?) {
            }
            override fun onAnimationEnd(animation: Animator?) {
                end()
            }
        })
    }

    private fun showResulText() {
        game_text.x = textPos + OFF_SCREEN
        game_text.text = model.getResultText(context!!)
        slideView(game_text, textPos) {showCorrectAnswer()}
    }

    private fun showCorrectAnswer() {
        totalScore.visibility = View.VISIBLE
        totalScore.text = String.format(getString(R.string.total_score), model.getTotalScore())
        roundScore.visibility = View.VISIBLE
        roundScore.text = String.format(getString(R.string.round_score), model.getRoundScore())
        val idx = model.getCorrectIndex()
        radioButtons[idx]?.setTextColor(ContextCompat.getColor(context!!, android.R.color.holo_orange_light))
        gameTimer.showTimer(false)
        if (model.isGameOver()) {
            nextRound.text = getString(R.string.next)
        }
        nextRound.visibility = View.VISIBLE
    }
}