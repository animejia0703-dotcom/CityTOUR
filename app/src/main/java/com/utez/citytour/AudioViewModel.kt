package com.utez.citytour

import android.app.Application
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class AudioViewModel(application: Application) : AndroidViewModel(application) {

    private var player: MediaPlayer? = null

    var estado by mutableStateOf(AudioState.DETENIDO)
        private set

    var hayError by mutableStateOf(false)
        private set

    fun reproducir() {
        if (estado == AudioState.REPRODUCIENDO) return
        if (player == null) {
            val mp = MediaPlayer.create(getApplication<Application>(), R.raw.catedral_audio)
            if (mp == null) {
                hayError = true
                return
            }
            hayError = false
            mp.setOnCompletionListener { detener() }
            player = mp
        }
        player?.start()
        estado = AudioState.REPRODUCIENDO
    }

    fun pausar() {
        if (estado != AudioState.REPRODUCIENDO) return
        player?.pause()
        estado = AudioState.EN_PAUSA
    }

    fun detener() {
        player?.release()
        player = null
        estado = AudioState.DETENIDO
    }

    override fun onCleared() {
        player?.release()
        player = null
        super.onCleared()
    }
}
