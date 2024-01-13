package com.idh.alarmadespertador.viewmodels

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.idh.alarmadespertador.R
import com.idh.alarmadespertador.domain.repository.TemporizadorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject
@HiltViewModel
class MeditacionViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repo: TemporizadorRepository //Para interactuar con la base de datos
) : ViewModel() {

    private var mediaPlayer: MediaPlayer? = null

    fun getMediaPlayer(): MediaPlayer? {
        return mediaPlayer
    }

    val isPlaying = mutableStateOf(false)

    fun iniciarMeditacion(melodiaResId: Int) {
        try {
            mediaPlayer = MediaPlayer.create(context, melodiaResId)
            mediaPlayer?.start()
            isPlaying.value = true
        } catch (e: Exception) {
            Log.e("MeditacionViewModel", "Error al iniciar MediaPlayer: ${e.message}")
        }
        openDialog()
    }

    fun pausarOReanudarReproduccion() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                isPlaying.value = false
            } else {
                it.start()
                isPlaying.value = true
            }
        }
    }

    fun detenerReproduccion() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop()
        }
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying.value = false
    }
    override fun onCleared() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onCleared()
    }

    var openDialog by mutableStateOf(false)

    fun closeDialog() {
        openDialog = false
    }

    fun openDialog() {
        openDialog = true
    }

    val melodias = listOf(
        R.raw.airymeditationb,
        R.raw.path,
        R.raw.hzconnectwith,
        R.raw.meditationature,
        R.raw.relaxingmusicvoluno
    )

}