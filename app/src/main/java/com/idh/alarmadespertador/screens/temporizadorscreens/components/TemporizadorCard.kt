package com.idh.alarmadespertador.screens.temporizadorscreens.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
    onPlayPause: (Int ,EstadoReloj) -> Unit,
    onFinish: () -> Unit,
    viewModel: TemporizadorViewModel
) {

    // Observa los cambios en el estado del temporizador específico
    val temporizadorState by viewModel.temporizadorState.collectAsState()
    val actualizadoTemporizador = temporizadorState[temporizador.id] ?: temporizador

    LaunchedEffect(actualizadoTemporizador.estadoTemp) {
        Log.d("TemporizadorCard", "Estado Temporizador actualizado: ${actualizadoTemporizador.estadoTemp}")
        // Esto forzará la reconstrucción del composable cuando el estado cambie
    }

    // Cálculos basados en el temporizador actualizado
    val horas = actualizadoTemporizador.milisegundos / 3600000
    val minutos = (actualizadoTemporizador.milisegundos % 3600000) / 60000
    val segundos = (actualizadoTemporizador.milisegundos % 60000) / 1000
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {

                // Usar Row en lugar de Column para mostrar horas, minutos y segundos en una línea
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Formatear el tiempo en un formato de cuenta regresiva
                    //  val tiempoFormateado = String.format("%02d:%02d:%02d", horas, minutos, segundos)
                    Text(
                        text = tiempoFormateado,
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 44.sp), // Aumentar el tamaño de la fuente
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                    )
                }
                Text(temporizador.nombreTemporizador, style = MaterialTheme.typography.titleSmall)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {

                Spacer(modifier = Modifier.width(38.dp))

                DeleteIcon(deleteTemporizador = deleteTemporizador)

                Spacer(modifier = Modifier.width(38.dp))

                PlayPauseIcons(
                    temporizadorId = actualizadoTemporizador.id,
                    estadoTemporizador = actualizadoTemporizador.estadoTemp,
                    onPlayPause = onPlayPause
                )

            }
                if (actualizadoTemporizador.estadoTemp == EstadoReloj.COMPLETADO) {
                    onFinish()
                }
            }
        }
    }