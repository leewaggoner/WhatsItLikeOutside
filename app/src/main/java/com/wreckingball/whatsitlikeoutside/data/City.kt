package com.wreckingball.whatsitlikeoutside.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
class City(@PrimaryKey(autoGenerate = true) val id: Int,
           @ColumnInfo(name = "weatherName") val weatherName: String,
           @ColumnInfo(name = "loction") val location: String)