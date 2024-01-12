package com.idh.alarmadespertador.screens.temporizadorscreens.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.idh.alarmadespertador.domain.models.EstadoReloj
import com.idh.alarmadespertador.domain.models.Temporizador
import com.idh.alarmadespertador.viewmodels.TemporizadorViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TemporizadorCard(
    temporizador: Temporizador, // Pasa el objeto Temporizador completo
    deleteTemporizador: () -> Unit,
    navigateToUpdateTemporizadorScreen: (temporizadorId: Int) -> Unit,
    onPlayPause: (Int, EstadoReloj) -> Unit,
    onFinish: () -> Unit,
    viewModel: TemporizadorViewModel
) {

    // Observa los cambios en el estado del temporizador específico
    val temporizadorState by viewModel.temporizadorState.collectAsState()
    val actualizadoTemporizador = temporizadorState[temporizador.id] ?: temporizador


    var milisegundosRestantes by remember { mutableStateOf(temporizador.milisegundos) }

    var mostrarDialogoFin by remember { mutableStateOf(false) }

    LaunchedEffect(actualizadoTemporizador.estadoTemp, actualizadoTemporizador.id) {
        while (milisegundosRestantes > 0 && actualizadoTemporizador.estadoTemp == EstadoReloj.ACTIVO) {
            delay(1000)
            if (actualizadoTemporizador.estadoTemp == EstadoReloj.ACTIVO) {
                milisegundosRestantes -= 1000
            }
        }
        if (milisegundosRestantes <= 0 && actualizadoTemporizador.estadoTemp == EstadoReloj.COMPLETADO) {
            onFinish()
            mostrarDialogoFin = true
        }
    }

    MostrarDialogoFinTemporizador(
        mostrarDialogo = mostrarDialogoFin,
        onDismiss = {
            mostrarDialogoFin = false
            val temporizadorActualizado = temporizador.copy(estadoTemp = EstadoReloj.PAUSADO)
            viewModel.updateTemporizadorState(temporizadorActualizado)
        },
        nombreTemporizador = temporizador.nombreTemporizador,
        idTemporizador = temporizador.id
    )

    // Convertir milisegundos a horas, minutos, segundos
    val horas = milisegundosRestantes / 3600000
    val minutos = (milisegundosRestantes % 3600000) / 60000
    val segundos = (milisegundosRestantes % 60000) / 1000
    val tiempoFormateado = String.format("%02d:%02d:%02d", horas, minutos, segundos)


    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 4.dp,
                bottom = 4.dp
            )
            .fillMaxWidth()
            .combinedClickable(
                onClick = { /*  */ },
                onLongClick = {
                    navigateToUpdateTemporizadorScreen(temporizador.id)
                }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Usar Row en lugar de Column para mostrar horas, minutos y segundos en una línea
                // Formatear el tiempo en un formato de cuenta regresiva
                //  val tiempoFormateado = String.format("%02d:%02d:%02d", horas, minutos, segundos)
                Text(
                    text = tiempoFormateado,
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp), // Aumentar el tamaño de la fuente
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )
                PlayPauseIcons(
                    temporizadorId = actualizadoTemporizador.id,
                    estadoTemporizador = actualizadoTemporizador.estadoTemp,
                    onPlayPause = { temporizadorId, nuevoEstado ->
                        viewModel.cambiarEstadoTemporizador(temporizadorId, nuevoEstado)}
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Box para el nombre del temporizador (a la izquierda)
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = temporizador.nombreTemporizador,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            // Box para el botón de eliminar (centrado)
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                DeleteIcon(deleteTemporizador = deleteTemporizador)
            }

            // Box adicional para mantener el equilibrio (a la derecha)
            Box(
                modifier = Modifier.weight(1f)
            ) {
                // Aquí podrías agregar otro elemento si es necesario, o dejarlo vacío para equilibrar el diseño
            }
        }
        if (actualizadoTemporizador.estadoTemp == EstadoReloj.COMPLETADO) {
            onFinish()
        }
    }
}
