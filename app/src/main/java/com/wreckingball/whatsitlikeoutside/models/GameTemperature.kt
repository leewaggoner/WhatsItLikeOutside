package com.wreckingball.whatsitlikeoutside.models

import kotlin.random.Random

class GameTemperature(private val location : String, private val realTemp: Int) {
    private var temps = mutableListOf<Int>()
    init {
        val collection = mutableListOf<Int>()
        val random = Random(System.currentTimeMillis())
        collection.add(realTemp + random.nextInt(1, 3))
        collection.add(realTemp + random.nextInt(4, 7))
        collection.add(realTemp + random.nextInt(8, 11))
        collection.add(realTemp + random.nextInt(12, 15))
        collection.add(realTemp + random.nextInt(16, 19))
        collection.add(realTemp - random.nextInt(1, 3))
        collection.add(realTemp - random.nextInt(4, 7))
        collection.add(realTemp - random.nextInt(8, 11))
        collection.add(realTemp - random.nextInt(12, 15))
        collection.add(realTemp - random.nextInt(16, 19))
        collection.shuffle(random)

        temps.add(realTemp)
        temps.add(collection[0])
        temps.add(collection[1])
        temps.add(collection[2])
        temps.shuffle()
    }

    fun getLocation(): String {
        return location
    }

    fun getTemps(): Array<Int> {
        return temps.toTypedArray()
    }

    fun getRealTemp() : Int {
        return realTemp
    }
}