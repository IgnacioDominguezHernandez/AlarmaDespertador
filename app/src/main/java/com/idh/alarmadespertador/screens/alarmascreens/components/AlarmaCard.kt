package com.idh.alarmadespertador.screens.alarmascreens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

//Vista de los card de la Alarma
@Composable
fun AlarmaCard(alarma: Alarma, onAlarmaChanged: (Alarma) -> Unit, onDeleteAlarma: () -> Unit) {

    val horaAlarma = LocalTime.ofSecondOfDay(alarma.tiempoActivacion / 1000 % (24 * 3600))
    //   val horaFormateada = horaAlarma.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))

    val instant = Instant.ofEpochMilli(alarma.tiempoActivacion)
    val zonaLocal = ZoneId.systemDefault()
    val horaLocal = instant.atZone(zonaLocal).toLocalTime()

    val horaFormateada = horaLocal.format(DateTimeFormatter.ofPattern("HH:mm"))

    val isEnabledState = remember { mutableStateOf(alarma.isEnabled) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium, // Forma del card
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation( // Elevación del card
            defaultElevation = 6.dp
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Fila para el nombre y su valor
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nombre:",
                    style = MaterialTheme.typography.bodySmall, // Tamaño pequeño para el rótulo
                    color = MaterialTheme.colorScheme.onSurface // Color para el texto del rótulo
                )
                Spacer(modifier = Modifier.width(8.dp)) // Espaciado entre texto y valor
                Text(
                    text = alarma.label,
                    style = MaterialTheme.typography.bodyLarge, // Tamaño grande para el nombre de la alarma
                    color = MaterialTheme.colorScheme.primary // Color primario para el nombre de la alarma
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hora:",
                    style = MaterialTheme.typography.bodySmall, // Tamaño pequeño para el rótulo
                    color = MaterialTheme.colorScheme.onSurface // Color para el texto del rótulo
                )
                Spacer(modifier = Modifier.width(8.dp)) // Espaciado entre texto y valor
                Text(
                    text = horaFormateada,
                    style = MaterialTheme.typography.titleMedium, // Tamaño mediano para la hora
                    color = MaterialTheme.colorScheme.secondary // Color secundario para la hora
                )
            }
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
                Text(
                    text = if (isEnabledState.value) "Activada" else "Desactivada",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
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