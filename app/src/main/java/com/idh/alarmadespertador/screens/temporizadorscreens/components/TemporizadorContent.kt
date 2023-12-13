package com.idh.alarmadespertador.screens.temporizadorscreens.components

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.idh.alarmadespertador.domain.models.EstadoReloj
import com.idh.alarmadespertador.domain.models.Temporizador
import com.idh.alarmadespertador.viewmodels.TemporizadorViewModel

@Composable
fun TemporizadorContent(
    padding: PaddingValues, // padding del elemento padre
    temporizadores: List<Temporizador>, // Asegúrate de que sea una lista
    deleteTemporizador: (temporizador: Temporizador) -> Unit,
    navigateToUpdateTemporizadorScreen: (temporizadorId: Int) -> Unit,
    onPlayPause: (Int ,EstadoReloj) -> Unit,
    onFinish: () -> Unit,
    viewModel: TemporizadorViewModel
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        items(temporizadores) { temporizador ->
            Log.d("TemporizadorContent", "Mostrando temporizador ID: ${temporizador.id}")
            TemporizadorCard(
                temporizador = temporizador,
                deleteTemporizador = { deleteTemporizador(temporizador) },
                navigateToUpdateTemporizadorScreen = navigateToUpdateTemporizadorScreen,
                onPlayPause = { temporizadorId ,nuevoEstado ->
                    onPlayPause(temporizadorId ,nuevoEstado)
                },
                onFinish = onFinish,
                viewModel = viewModel
            )
        }
    }
}