package com.wreckingball.whatsitlikeoutside.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import com.wreckingball.whatsitlikeoutside.data.CityDatabase
import com.wreckingball.whatsitlikeoutside.data.CityRepository
import com.wreckingball.whatsitlikeoutside.models.EndViewModel
import com.wreckingball.whatsitlikeoutside.models.GameViewModel
import com.wreckingball.whatsitlikeoutside.models.RulesViewModel
import com.wreckingball.whatsitlikeoutside.utils.PreferencesWrapper
import com.wreckingball.whatsitlikeoutside.utils.Sounds
import com.wreckingball.whatsitlikeoutside.widgets.GameTimer
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module(override = true) {
    single { Sounds() }
    single { PreferencesWrapper(getSharedPrefs(androidContext())) }
    factory { GameTimer() }
    viewModel { EndViewModel(get(), androidApplication()) }
    viewModel { GameViewModel(get(), androidApplication()) }
    viewModel { RulesViewModel(get(), androidApplication()) }
    single { CityRepository(get()) }
    single { Room.databaseBuilder(androidApplication(), CityDatabase::class.java, "city_db").build() }
    single { get<CityDatabase>().cityDao() }
}

private fun getSharedPrefs(context: Context) : SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(context)
}
