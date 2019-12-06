package com.wreckingball.whatsitlikeoutside.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream

private val TAG = "Utils"

inline fun <reified T> getJsonObjects(context: Context, fileNameId: Int) : T? {
    try {
        val inputStream: InputStream = context.resources.openRawResource(fileNameId)
        val jsonStr = inputStream.bufferedReader().use { it.readText() }
        Log.d("Utils", jsonStr)
        val type = object : TypeToken<T>() {}.type
        return Gson().fromJson(jsonStr, type)
    } catch (e: Exception) {
        Log.e("Utils", "Exception: " + e.message, e)
    }

    return null
}
