package com.idh.alarmadespertador.screens.topupscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.idh.alarmadespertador.viewmodels.TopAppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun EstadisticaMeditacion(viewModel: TopAppViewModel = hiltViewModel()) {

    //Pantalla de estadísticas
    var veces = 0
    var segundos = 0
    var completado = 0
    var porcentajeCompletado = 0
    var tiempoPromedio = 0

    var showDialog by remember { mutableStateOf(false) }

    //Consulta a la BD
    runBlocking {
        launch(Dispatchers.IO) {
            // Realiza la consulta en un hilo secundario
            val temporizador = viewModel.getTemporizadorFromRoom(1)

            if (temporizador != null) {
                // Actualiza las variables en el hilo principal
                veces = temporizador.veces
                segundos = temporizador.tiempo_transcurrido
                completado = temporizador.completado

                if (veces > 0) {
                    tiempoPromedio = segundos / veces
                }
            }
        }
    }
    //Interfaz para mostrar datos
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text("Meditaciones: $veces", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        val tiempoPromedio = formatTiempo(tiempoPromedio)

        Text(
            "Tiempo promedio por sesión:",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "$tiempoPromedio",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Completadas $completado sesiones de $veces",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))
        if (veces != 0) {
            porcentajeCompletado = (completado * 100) / veces
        }

        Text(
            "Porcentaje: $porcentajeCompletado%",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(48.dp))

        //Muestra Dialogo
        Button(
            onClick = {
                showDialog = true
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Borrar Datos", style = MaterialTheme.typography.bodyMedium)
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                // Manejar el cierre del cuadro de diálogo (por ejemplo, al tocar fuera del cuadro de diálogo)
                showDialog = false
            },
            text = {
                Text(
                    "¿Estás seguro?",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
            },
            //Llama a la BD para hacer un Delete
            confirmButton = {
                Button(
                    onClick = {
                        // El usuario confirmó la acción
                        viewModel.viewModelScope.launch(Dispatchers.IO) {
                            // Eliminar temporizador 1 en un hilo secundario (Dispatcher.IO)
                            viewModel.deleteTemporizadorFromRoom(1)
                        }
                        showDialog = false // Cierra el cuadro de diálogo
                    }
                ) {
                    Text("Borrar", style = MaterialTheme.typography.bodyMedium)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        // El usuario canceló la acción
                        showDialog = false // Cierra el cuadro de diálogo
                    }
                ) {
                    Text("Cancelar", style = MaterialTheme.typography.bodyMedium)
                }
            }
        )
    }
}

@Composable
private fun formatTiempo(tiempoSegundos: Int): String {
    val segundos = tiempoSegundos % 60
    val minutos = (tiempoSegundos / 60) % 60
    val horas = tiempoSegundos / 3600
    return String.format("%02d:%02d:%02d", horas, minutos, segundos)
}




