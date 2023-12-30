package com.idh.alarmadespertador.screens.alarmascreens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.idh.alarmadespertador.domain.models.Alarma
import androidx.compose.ui.Modifier
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun AlarmaCard(alarma: Alarma, onAlarmaChanged: (Alarma) -> Unit,onDeleteAlarma: () -> Unit) {

    val horaAlarma = LocalTime.ofSecondOfDay(alarma.tiempoActivacion / 1000 % (24 * 3600))
 //   val horaFormateada = horaAlarma.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))

    val instant = Instant.ofEpochMilli(alarma.tiempoActivacion)
    val zonaLocal = ZoneId.systemDefault()
    val horaLocal = instant.atZone(zonaLocal).toLocalTime()

    val horaFormateada = horaLocal.format(DateTimeFormatter.ofPattern("HH:mm"))

    val isEnabledState = remember { mutableStateOf(alarma.isEnabled) }

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Alarma: ${alarma.label}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Hora: $horaFormateada", style = MaterialTheme.typography.bodyMedium)
       //     Text(text = "Días: ${alarma.dias}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                DeleteIconAlarm(
                    eliminarAlarma = onDeleteAlarma
                )
                Text(text = if (isEnabledState.value) "Activada" else "Desactivada")
                Switch(
                    checked = isEnabledState.value,
                    onCheckedChange = {
                        isEnabledState.value = it
                        onAlarmaChanged(alarma.copy(isEnabled = it))
                    }
                )
            }
        }
    }
}