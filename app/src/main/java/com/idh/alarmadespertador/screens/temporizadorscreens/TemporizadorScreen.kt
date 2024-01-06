package com.idh.alarmadespertador.screens.temporizadorscreens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.idh.alarmadespertador.screens.temporizadorscreens.components.AddTemporizadorAlertDialog
import com.idh.alarmadespertador.screens.temporizadorscreens.components.TemporizadorContent
import com.idh.alarmadespertador.viewmodels.TemporizadorViewModel

//Vista de temporizador screen, tambien recibe los temporizadores de la BD y los pasa a
//Temporizador Content que los pasa a temporizadorCard
@Composable
fun TemporizadorScreen(
    viewModel: TemporizadorViewModel = hiltViewModel(),
    navigateToUpdateTemporizadorScreen: (temporizadorId: Int) -> Unit
) {

    val temporizadorState = viewModel.temporizadorState.collectAsState()

    Log.d("TemporizadorScreen", "Temporizadores en pantalla: ${temporizadorState.value.size}")

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openDialog() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Agregar Temporizador")
            }
        },
        floatingActionButtonPosition = FabPosition.End, // Posición del FAB
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { // Asegúrate de aplicar el padding interno del Scaffold

            Column {
                // Aquí, convertimos el mapa de temporizadores a una lista
                val temporizadores = temporizadorState.value.values.toList()

                TemporizadorContent(
                    padding = PaddingValues(all = 16.dp),
                    temporizadores = temporizadores,
                    deleteTemporizador = { temporizador ->
                        viewModel.deleteTemporizador(temporizador)
                    },
                    onPlayPause = { temporizadorId, nuevoEstado ->
                        viewModel.cambiarEstadoTemporizadorEnMemoria(temporizadorId, nuevoEstado)
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
        }
    }
}


