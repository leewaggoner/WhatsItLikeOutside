package com.wreckingball.whatsitlikeoutside.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface CityDao {
    @Query("SELECT * FROM cities")
    fun getCities() : List<City>

    @Insert(onConflict = REPLACE)
    fun insertAll(cities: List<City>)

    @Query("DELETE FROM cities")
    fun deleteAll()
}