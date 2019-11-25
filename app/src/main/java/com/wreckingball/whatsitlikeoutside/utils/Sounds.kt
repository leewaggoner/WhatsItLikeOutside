package com.wreckingball.whatsitlikeoutside.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import com.wreckingball.whatsitlikeoutside.R

const val WHOOSH = "whoosh"
const val BEEP = "beep"
const val LOSER = "loser"
const val VICTORY = "winner"

private const val MAX_STREAMS = 4

class Sounds {
    private var soundPool: SoundPool? = null
    private val sound: MutableMap<String, Int?> = hashMapOf()
    var isOn = true

    fun init(context: Context) {
        soundPool ?:  run {
            soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()

                SoundPool.Builder()
                    .setMaxStreams(25)
                    .setAudioAttributes(audioAttributes)
                    .build()
            } else {
                SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0)
            }
            sound[WHOOSH] = soundPool?.load(context, R.raw.whoosh, 1)
            sound[BEEP] = soundPool?.load(context, R.raw.beep, 1)
            sound[LOSER] = soundPool?.load(context, R.raw.loser, 1)
            sound[VICTORY] = soundPool?.load(context, R.raw.victory, 1)
        }
    }

    fun play(id: String) {
        if (isOn) {
            sound[id]?.let {
                soundPool?.play(it, 0.5f, 0.5f, 0, 0, 1.0f)
            }
        }
    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }
}