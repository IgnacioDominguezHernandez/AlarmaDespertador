package com.idh.alarmadespertador.screens.temporizadorscreens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.idh.alarmadespertador.domain.models.EstadoReloj
import com.idh.alarmadespertador.domain.models.Temporizador
import com.idh.alarmadespertador.screens.temporizadorscreens.components.AddTemporizadorAlertDialog
import com.idh.alarmadespertador.screens.temporizadorscreens.components.AddTemporizadorFloatingActionButton
import com.idh.alarmadespertador.screens.temporizadorscreens.components.TemporizadorContent
import com.idh.alarmadespertador.viewmodels.TemporizadorViewModel

@Composable
fun TemporizadorScreen(
    viewModel: TemporizadorViewModel = hiltViewModel(),
    navigateToUpdateTemporizadorScreen: (temporizadorId: Int) -> Unit
) {

    val temporizadorState = viewModel.temporizadorState.collectAsState()

    Log.d("TemporizadorScreen", "Temporizadores en pantalla: ${temporizadorState.value.size}")

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            // Aquí, convertimos el mapa de temporizadores a una lista
            val temporizadores = temporizadorState.value.values.toList()

            TemporizadorContent(
                padding = PaddingValues(all = 16.dp),
                temporizadores = temporizadores, // Pasamos la lista de temporizadores
                deleteTemporizador = { temporizador ->
                    viewModel.deleteTemporizador(temporizador)
                },
                onPlayPause = { temporizadorId, nuevoEstado ->
                    viewModel.cambiarEstadoTemporizadorEnMemoria(temporizadorId ,nuevoEstado)
                },
                onFinish = {
                    Log.d("TemporizadorSCREEN", "El temporizador ha llegado a cero.")
                },
                navigateToUpdateTemporizadorScreen = navigateToUpdateTemporizadorScreen,
                viewModel = viewModel
            )
            AddTemporizadorAlertDialog(
                openDialog = viewModel.openDialog,
                closeDialog = { viewModel.closeDialog() },
                addTemporizador = { temporizador ->
                    viewModel.addTemporizador(temporizador)
                }
            )
        }
        AddTemporizadorFloatingActionButton(
            openDialog = {
                viewModel.openDialog()
            }
        )
    }
}

