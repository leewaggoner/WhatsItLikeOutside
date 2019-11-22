package com.wreckingball.whatsitlikeoutside.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.wreckingball.whatsitlikeoutside.R
import com.wreckingball.whatsitlikeoutside.databinding.EndFragmentBinding
import com.wreckingball.whatsitlikeoutside.listeners.GameListener
import com.wreckingball.whatsitlikeoutside.models.EndViewModel
import kotlinx.android.synthetic.main.end_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EndFragment : Fragment(R.layout.end_fragment) {
    var score = 0
    private val model: EndViewModel by viewModel()
    private lateinit var gameListener: GameListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GameListener) {
            gameListener = context
        } else {
            throw RuntimeException("$context must implement GameListener")

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: EndFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.end_fragment, container, false)
        binding.vm = model
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        model.calcHighScore(context!!, score)
        playAgain.setOnClickListener {
            gameListener.playAgain()
        }
    }
}