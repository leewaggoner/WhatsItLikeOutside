package com.wreckingball.whatsitlikeoutside.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.wreckingball.whatsitlikeoutside.R
import com.wreckingball.whatsitlikeoutside.listeners.GameListener
import com.wreckingball.whatsitlikeoutside.models.MAX_ROUNDS
import kotlinx.android.synthetic.main.rules_fragment.*

class RulesFragment : Fragment(R.layout.rules_fragment) {
    private var gameListener: GameListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GameListener) {
            gameListener = context
        } else {
            throw RuntimeException("$context must implement GameListener")

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rules_text.text = String.format(getText(R.string.rules).toString(), MAX_ROUNDS)
        start_button.setOnClickListener {
            gameListener?.startGame()
        }
    }
}