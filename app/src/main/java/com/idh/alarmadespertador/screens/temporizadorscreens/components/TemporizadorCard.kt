package com.idh.alarmadespertador.screens.temporizadorscreens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.idh.alarmadespertador.domain.models.Temporizador

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemporizadorCard(
    temporizador: Temporizador,
    deleteTemporizador: () -> Unit,
    navigateToUpdateTemporizadorScreen: (temporizadorId: Int) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 4.dp,
                bottom = 4.dp
            )
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp),
        onClick = {
            navigateToUpdateTemporizadorScreen(temporizador.id)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column() {
                Text(temporizador.horas)
                Text(temporizador.minutos)
                Text(temporizador.segundos)
            }
            Spacer(modifier = Modifier.weight(1f)
            )
            DeleteIcon(
                deleteTemporizador = deleteTemporizador
            )
        }
    }
}


