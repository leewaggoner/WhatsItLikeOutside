package com.wreckingball.whatsitlikeoutside

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.wreckingball.whatsitlikeoutside.fragments.EndFragment
import com.wreckingball.whatsitlikeoutside.fragments.GameFragment
import com.wreckingball.whatsitlikeoutside.fragments.RulesFragment
import com.wreckingball.whatsitlikeoutside.listeners.GameListener

class MainActivity : AppCompatActivity(), GameListener {
    private val gameFragment = GameFragment()
    private val rulesFragment = RulesFragment()
    private val endFragment = EndFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            showFragment(rulesFragment)
        }
    }

    override fun startGame() {
        showFragment(gameFragment)
    }

    override fun endGame(totalScore: Int) {
        endFragment.score = totalScore
        showFragment(endFragment)
    }

    override fun playAgain() {
        showFragment(gameFragment)
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
