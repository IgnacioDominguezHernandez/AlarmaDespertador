package com.idh.alarmadespertador.screens.temporizadorscreens

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
    val temporizadores by viewModel.temporizadores.collectAsState(initial = emptyList())

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column {
            TemporizadorContent(
                padding = PaddingValues(all = 16.dp), // Ajusta el padding según las necesidades
                temporizadores = temporizadores,
                deleteTemporizador = { temporizador ->
                    viewModel.deleteTemporizador(temporizador)
                },
                navigateToUpdateTemporizadorScreen = navigateToUpdateTemporizadorScreen,
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

