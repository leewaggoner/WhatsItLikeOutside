package com.wreckingball.whatsitlikeoutside.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [City::class], version = 1, exportSchema = false)
abstract class CityDatabase : RoomDatabase() {
    abstract fun cityDao() : CityDao
}