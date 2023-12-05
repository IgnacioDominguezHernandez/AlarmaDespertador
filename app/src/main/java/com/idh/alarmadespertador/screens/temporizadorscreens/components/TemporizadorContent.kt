package com.idh.alarmadespertador.screens.temporizadorscreens.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.idh.alarmadespertador.domain.models.EstadoReloj
import com.idh.alarmadespertador.domain.models.Temporizador

@Composable
fun TemporizadorContent (
    padding: PaddingValues, // padding del elemento padre
    temporizadores: List<Temporizador>, // Asegúrate de que sea una lista
    deleteTemporizador: (temporizador: Temporizador) -> Unit,
    navigateToUpdateTemporizadorScreen: (temporizadorId: Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        items(temporizadores) { temporizador ->
            TemporizadorCard(
                temporizador = temporizador,
                deleteTemporizador = { deleteTemporizador(temporizador) },
                navigateToUpdateTemporizadorScreen = navigateToUpdateTemporizadorScreen
            )
        }
    }
}