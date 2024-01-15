package com.idh.alarmadespertador.screens.meditacionscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.idh.alarmadespertador.screens.meditacionscreen.components.DialogoMeditacion
import com.idh.alarmadespertador.viewmodels.MeditacionViewModel
import com.idh.alarmadespertador.viewmodels.TopAppViewModel

@Composable
fun MeditacionScreen(
    viewModel: MeditacionViewModel = hiltViewModel(),
    topAppViewModel: TopAppViewModel = hiltViewModel()
) {

    //Botones inhablitados una vez pulsado iniciar meditación
    var elementosHabilitados by remember { mutableStateOf(true) }
    var duracionSeleccionada by remember { mutableStateOf(10f) } // Duración predeterminada en float
    // var mostrarDialogo by remember { mutableStateOf(false) }
    val melodiaAleatoria = viewModel.melodias.random()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Duración de la Meditación: ${duracionSeleccionada.toInt()} minutos",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(36.dp))
        Slider(
            value = duracionSeleccionada,
            onValueChange = { if (elementosHabilitados) duracionSeleccionada = it },
            enabled = elementosHabilitados,
            valueRange = 1f..45f, // Rango de valores del Slider
            onValueChangeFinished = {
                // Esta es una opción para realizar acciones cuando el usuario ha finalizado de seleccionar un valor.
            },
            steps = 44 // Número de pasos discretos en el Slider
        )
        Spacer(modifier = Modifier.height(36.dp))
        Button(
            onClick = {
                if (elementosHabilitados) {
                    viewModel.iniciarMeditacion(melodiaAleatoria)
                    //   mostrarDialogo = true
                    elementosHabilitados = false
                }
            },
            enabled = elementosHabilitados
        ) {
            Text(
                "Iniciar Meditación",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
    //Se abre un dialog con la música y la cuenta atrás
    if (viewModel.openDialog) {
        DialogoMeditacion(
            viewModel = viewModel,
            topAppViewModel = topAppViewModel,
            durationInMinutes = duracionSeleccionada.toInt(),
            melodyUri = melodiaAleatoria,
            onDismiss = {
                viewModel.closeDialog()
                elementosHabilitados = true
            }
        )
    }

}




