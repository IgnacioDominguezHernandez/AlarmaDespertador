package com.idh.alarmadespertador.screens.temporizadorscreens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.idh.alarmadespertador.domain.models.EstadoReloj

@Composable
fun PlayPauseIcons(
    estadoTemporizador: EstadoReloj, // El estado actual del temporizador
    onPlayPause: (EstadoReloj) -> Unit // Callback para actualizar el estado del temporizador
) {
    when (estadoTemporizador) {
        EstadoReloj.ACTIVO -> {
            IconButton(onClick = { onPlayPause(EstadoReloj.PAUSADO) }) {
                Icon(imageVector = Icons.Default.Pause, contentDescription = "Pausar")
            }
        }
        EstadoReloj.PAUSADO -> {
            IconButton(onClick = { onPlayPause(EstadoReloj.ACTIVO) }) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Reproducir")
            }
        }
        else -> Unit // No mostrar botones para otros estados, como COMPLETADO
    }
}
