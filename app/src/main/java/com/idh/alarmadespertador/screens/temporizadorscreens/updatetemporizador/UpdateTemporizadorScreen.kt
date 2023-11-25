package com.idh.alarmadespertador.screens.temporizadorscreens.updatetemporizador

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.idh.alarmadespertador.screens.temporizadorscreens.updatetemporizador.components.UpdateTemporizadorContent
import com.idh.alarmadespertador.viewmodels.TemporizadorViewModel


@Composable
fun UpdateTemporizadorScreen(
    viewModel: TemporizadorViewModel = hiltViewModel(),
    temporizadorId: Int,
    navigateBack: () -> Unit
){
    LaunchedEffect(Unit){
        viewModel.getTemporizador(temporizadorId)
    }

    Column {
        // Botón o ícono de flecha hacia atrás
        IconButton(onClick = navigateBack) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver atrás")
        }

        // Contenido de la pantalla
        UpdateTemporizadorContent(
            padding = PaddingValues(),
            temporizador = viewModel.temporizador,
            updateNombreTemporizador = { nombreTemporizador ->
                viewModel.updateNombreTemporizador(nombreTemporizador)
            },
            updateHora = { hora ->
                viewModel.updateHora(hora)
            },
            updateMinutos = { minutos ->
                viewModel.updateMinutos(minutos)
            },
            updateSegundos = { segundos ->
                viewModel.updateSegundos(segundos)
            },
            updateTemporizador = { temporizador ->
                viewModel.updateTemporizador(temporizador)
            },
            navigateBack = navigateBack
        )
    }
}

