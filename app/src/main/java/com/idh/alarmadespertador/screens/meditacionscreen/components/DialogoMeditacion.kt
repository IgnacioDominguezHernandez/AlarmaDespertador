package com.idh.alarmadespertador.screens.meditacionscreen.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.idh.alarmadespertador.domain.models.Temporizador
import com.idh.alarmadespertador.viewmodels.MeditacionViewModel
import com.idh.alarmadespertador.viewmodels.TopAppViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DialogoMeditacion(
    viewModel: MeditacionViewModel,
    topAppViewModel: TopAppViewModel,
    durationInMinutes: Int,
    melodyUri: Int,
    onDismiss: () -> Unit
) {

    Log.d("MeditacionContent", "Melodía URI: $melodyUri")
    Log.d("MeditacionContent", "Duración en minutos: $durationInMinutes")

    var elapsedTimeMillis by remember { mutableStateOf(durationInMinutes * 60 * 1000L) }
    var elapsedTimePassedMillis by remember { mutableStateOf(0L) }

    val totalSeconds = elapsedTimeMillis / 1000
    val valorInicial : Int = durationInMinutes * 60
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    val totalSecondsPassed = elapsedTimePassedMillis / 1000
    val minutesPassed = totalSecondsPassed / 60
    val secondsPassed = totalSecondsPassed % 60

    var showDialogdos by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModel.isPlaying.value) {
        coroutineScope.launch {
            while (viewModel.isPlaying.value && elapsedTimeMillis > 0) {
                delay(1000) // Espera 1 segundo
                elapsedTimeMillis -= 1000
                elapsedTimePassedMillis += 1000
            }
            if (elapsedTimeMillis <= 0) {
                showDialogdos = true // Mostrar el diálogo cuando se haya agotado el tiempo
            }
        }
    }

    if (showDialogdos) {
        viewModel.pausarOReanudarReproduccion()
        AlertDialog(
            onDismissRequest = {
                showDialogdos = false
            },
            title = {
                Text("¡Bien hecho!", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
            },
            text = {
                Text("Que tengas un bonito dia!", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialogdos = false
                        viewModel.detenerReproduccion()
                        viewModel.closeDialog()
                        onDismiss()

                        val nuevoTemporizador = Temporizador(
                            id = 1, // Siempre es el ID 1
                            veces = 1, // Actualiza veces
                            tiempo_transcurrido = valorInicial, // Calcula el tiempo en segundos
                            completado = 1
                        )
                        Log.d("NuevoTemporizador", "Tiempo Transcurrido (segundos): ${elapsedTimePassedMillis / 1000}")
                        viewModel.viewModelScope.launch {
                            topAppViewModel.updateTemporizadorInRoom(nuevoTemporizador)
                            onDismiss()
                        }
                    },
                    modifier = Modifier.fillMaxWidth() // Ocupa todo el ancho disponible
                ) {
                    Text("Aceptar", style = MaterialTheme.typography.bodyMedium)
                }
            }
        )
    }

    val mediaPlayer = viewModel.getMediaPlayer() // Obtener el MediaPlayer del ViewModel

    Log.d("MeditacionContent", "isPlaying al entrar: ${viewModel.isPlaying.value}")

    DisposableEffect(Unit) {
        onDispose {
            if (viewModel.isPlaying.value && mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
            }
        }
    }

    LaunchedEffect(viewModel.isPlaying.value) {
        // Actualiza la vista en función de isPlaying
        Log.d("MeditacionContent", "LaunchEffect: ${viewModel.isPlaying.value}")
    }

        Log.d("MeditacionContent", "Melodía URI: $melodyUri")
        Log.d("MeditacionContent", "Duración en minutos: $durationInMinutes")
        Log.d("MeditacionContent", "Tiempo restante: ${elapsedTimeMillis / 1000} segundos")

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.75F), // Ocupa el 75% de la pantalla
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tiempo restante",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "${String.format("%02d:%02d", minutes, seconds)}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(36.dp))

                Button(
                    onClick = {
                        viewModel.pausarOReanudarReproduccion()
                    }, modifier = Modifier
                        .fillMaxWidth() // Hace que el botón sea tan ancho como sea posible
                        .height(68.dp), // Ajusta la altura para que el botón sea más grande
                        colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.primary, // Color de fondo del botón
                        contentColor = MaterialTheme.colorScheme.onPrimary // Color del contenido del botón (texto, íconos)
                    )
                ) {
                    Text(
                        text = if (viewModel.isPlaying.value) "Pausar" else "Reanudar",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(36.dp))

                Button(
                    onClick = {
                        viewModel.detenerReproduccion()
                        viewModel.closeDialog()


                        val nuevoTemporizador = Temporizador(
                            id = 1, // Siempre es el ID 1
                            veces = 1, // Actualiza veces
                            tiempo_transcurrido = (elapsedTimePassedMillis / 1000).toInt(), // Calcula el tiempo en segundos
                            completado = 0
                        )
                        Log.d("NuevoTemporizadorCancelar", "Tiempo Transcurrido (segundos): ${elapsedTimePassedMillis / 1000}")
                        viewModel.viewModelScope.launch {
                            topAppViewModel.updateTemporizadorInRoom(nuevoTemporizador)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth() // Hace que el botón sea tan ancho como sea posible
                        .height(68.dp),
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text("Cancelar", style = MaterialTheme.typography.bodyMedium)
                }
            }

        }
    }

