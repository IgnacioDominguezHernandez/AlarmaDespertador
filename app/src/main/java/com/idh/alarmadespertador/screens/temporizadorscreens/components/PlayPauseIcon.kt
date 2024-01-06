package com.idh.alarmadespertador.screens.temporizadorscreens.components

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.idh.alarmadespertador.domain.models.EstadoReloj

//Iconos de Play y pause de los temporizadores del Card
@Composable
fun PlayPauseIcons(
    temporizadorId: Int,
    estadoTemporizador: EstadoReloj,
    onPlayPause: (Int, EstadoReloj) -> Unit
) {
    IconButton(onClick = {
        val nuevoEstado = if (estadoTemporizador == EstadoReloj.ACTIVO) EstadoReloj.PAUSADO else EstadoReloj.ACTIVO
        Log.d("PlayPauseInteraction", "Clic en PlayPause: Estado actual: $estadoTemporizador, Nuevo estado: $nuevoEstado")
        onPlayPause(temporizadorId, nuevoEstado) // Primero el ID, luego el estado
    }) {
        val icono = if (estadoTemporizador == EstadoReloj.ACTIVO) Icons.Default.Pause else Icons.Default.PlayArrow
        Icon(imageVector = icono, contentDescription = if (estadoTemporizador == EstadoReloj.ACTIVO) "Pausar" else "Reproducir")
    }
}


