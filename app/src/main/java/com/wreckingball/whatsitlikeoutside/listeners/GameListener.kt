package com.wreckingball.whatsitlikeoutside.listeners

interface GameListener {
    fun startGame()
    fun endGame(totalScore: Int)
    fun playAgain()
}