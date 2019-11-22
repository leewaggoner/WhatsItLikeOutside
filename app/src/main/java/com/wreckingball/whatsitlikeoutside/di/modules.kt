package com.wreckingball.whatsitlikeoutside.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.wreckingball.whatsitlikeoutside.models.EndViewModel
import com.wreckingball.whatsitlikeoutside.models.GameTimer
import com.wreckingball.whatsitlikeoutside.models.GameViewModel
import com.wreckingball.whatsitlikeoutside.utils.PreferencesWrapper
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module(override = true) {
    single { PreferencesWrapper(
        getSharedPrefs(
            androidContext()
        )
    ) }
    factory { GameTimer() }
    viewModel { EndViewModel() }
    viewModel { GameViewModel() }
}

private fun getSharedPrefs(context: Context) : SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(context)
}
