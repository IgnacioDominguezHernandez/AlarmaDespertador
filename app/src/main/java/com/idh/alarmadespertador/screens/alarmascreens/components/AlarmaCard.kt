package com.idh.alarmadespertador.screens.alarmascreens.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.idh.alarmadespertador.domain.models.Alarma
import androidx.compose.ui.Modifier

@Composable
fun AlarmaCard(alarma: Alarma, onAlarmaChanged: (Alarma) -> Unit) {
    Card(modifier = Modifier.padding(8.dp)) {
        // Mostrar detalles de la alarma
        // Incluir un Switch para activar/desactivar la alarma
    }
}