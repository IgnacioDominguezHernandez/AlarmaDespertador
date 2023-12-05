package com.idh.alarmadespertador.screens.temporizadorscreens.updatetemporizador

import android.util.Log
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

    LaunchedEffect(temporizadorId){
        viewModel.getTemporizador(temporizadorId)
    }

    val temp = viewModel.temporizador
    LaunchedEffect(temp) {
        if (temp.nombreTemporizador != null) {
            Log.d("UpdateTemporizadorScreen", "Temporizador ID: ${temp.nombreTemporizador}")
            // y otros datos...
        }
    }

    val updateMilisegundos = { horas: Int, minutos: Int, segundos: Int ->
        val totalMilisegundos = (horas * 3600 + minutos * 60 + segundos) * 1000
        viewModel.updateMilisegundos(totalMilisegundos)
    }

    Column {
        // Botón o ícono de flecha hacia atrás
        IconButton(onClick = navigateBack) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
        }

        // Contenido de la pantalla
        UpdateTemporizadorContent(
            padding = PaddingValues(),
            temporizador = temp,

            updateTemporizador = { temporizador ->
                Log.d("Lamando a update temporizador", "ID: ${temporizador.nombreTemporizador}")

                viewModel.updateTemporizador(temporizador)
            },
            navigateBack = navigateBack
        )
    }
}

