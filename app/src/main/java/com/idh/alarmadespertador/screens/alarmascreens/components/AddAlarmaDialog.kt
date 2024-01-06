package com.idh.alarmadespertador.screens.alarmascreens.components

import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.idh.alarmadespertador.core.components.RingtonePickerDropdown
import com.idh.alarmadespertador.domain.models.Alarma
import com.idh.alarmadespertador.viewmodels.AlarmaViewModel
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/* Diálogo para añadir una nueva alarma.
Permite seleccionar la hora, si se desea vibración,
elegir un tono de llamada, y proporcionar un nombre o etiqueta para la alarma.
La función utiliza AlertDialog de Jetpack Compose para mostrar el diálogo y permite
confirmar o cancelar la acción. */

@Composable
fun AddAlarmaDialog(onDismiss: () -> Unit, onConfirm: (Alarma) -> Unit) {
    //  val context = LocalContext.current
    val alarmaViewModel: AlarmaViewModel = viewModel()
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    var isVibrationEnabled by remember { mutableStateOf(false) }
    var selectedRingtoneUri by remember { mutableStateOf("") }
    var selectedRingtoneTitle by remember { mutableStateOf("") }
    //  var diasSeleccionados by remember { mutableStateOf(setOf<String>()) }
    var label by remember { mutableStateOf("") }

    // val diasDeLaSemana = listOf("L", "M", "X", "J", "V", "S", "D")

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                "Nueva Alarma",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TimePickerButton(
                    selectedTime = selectedTime,
                    onTimeSelected = { newTime -> selectedTime = newTime }
                )
                Text(
                    "Hora seleccionada: ${selectedTime.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Vibración",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(18.dp))
                    Switch(
                        checked = isVibrationEnabled,
                        onCheckedChange = { isVibrationEnabled = it }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                )
                {
                RingtonePickerDropdown(
                    selectedRingtone = selectedRingtoneTitle,
                    onRingtoneSelected = { title, uri ->
                        selectedRingtoneTitle = title
                        selectedRingtoneUri = uri
                    }
                )
                }
                /*
                // Fila para los CheckBoxes y las etiquetas de los días
                Row {
                    diasDeLaSemana.forEach { dia ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)  // Asigna el mismo peso a cada columna
                        ) {
                            CheckBoxDia(dia, diasSeleccionados, onDiaCambiado = { diaSeleccionado, isSelected ->
                                if (isSelected) {
                                    diasSeleccionados = diasSeleccionados + diaSeleccionado
                                } else {
                                    diasSeleccionados = diasSeleccionados - diaSeleccionado
                                }
                            })
                            Text(dia, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                 */
                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    label = {
                        Text(
                            "nombre alarma",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    //val diasSeleccionadosStr = procesarDiasSeleccionados(diasSeleccionados)
                    //val diasFinal = if (diasSeleccionadosStr.isBlank()) seleccionarDiaSiguiente() else diasSeleccionadosStr
                    val now = LocalDateTime.now()
                    val adjustedDateTime =
                        now.withHour(selectedTime.hour).withMinute(selectedTime.minute)
                            .withSecond(0).withNano(0)
                    val tiempoActivacion =
                        adjustedDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

                    val nuevaAlarma = Alarma(
                        id = 0,
                        tiempoActivacion,
                        dias = "L",
                        isEnabled = true,
                        vibrate = isVibrationEnabled,
                        soundTitle = selectedRingtoneTitle,
                        soundUri = selectedRingtoneUri,
                        label = label
                    )
                    alarmaViewModel.crearAlarma(nuevaAlarma)
                    Log.d("AddAlarmaDialog", "Título del tono seleccionado: $selectedRingtoneTitle")
                    Log.d("AddAlarmaDialog", "URI del tono seleccionado: $selectedRingtoneUri")
                    onDismiss()
                },
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    "Confirmar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() },
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(
                    "Cancelar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    )
}

//Para la selección de la hora del usuario
@Composable
fun TimePickerButton(selectedTime: LocalTime, onTimeSelected: (LocalTime) -> Unit) {
    val context = LocalContext.current
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            onTimeSelected(LocalTime.of(hour, minute)) // Notifica al componente padre
        },
        selectedTime.hour, // Usa el valor actual de selectedTime
        selectedTime.minute, // Usa el valor actual de selectedTime
        true // Formato de 24 horas
    )
    Button(
        onClick = { timePickerDialog.show() },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Icon(
            Icons.Outlined.Schedule,
            contentDescription = "Seleccionar Hora",
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            "Seleccionar Hora",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

/*
@Composable
fun CheckBoxDia(
    dia: String,
    diasSeleccionados: Set<String>,
    onDiaCambiado: (String, Boolean) -> Unit
) {
    Checkbox(
        checked = dia in diasSeleccionados,
        onCheckedChange = { isChecked -> onDiaCambiado(dia, isChecked) }
    )
}

 */

/*
fun procesarDiasSeleccionados(diasSeleccionados: Set<String>): String {
    val diasDeLaSemana = listOf("L", "M", "X", "J", "V", "S", "D")
    return diasDeLaSemana.joinToString("") { dia ->
        if (dia in diasSeleccionados) dia else "_"
    }
}
// Función para seleccionar el día siguiente si el usuario no selecciona ningún día
fun seleccionarDiaSiguiente(): String {
    val diasDeLaSemana = listOf("L", "M", "X", "J", "V", "S", "D")
    val hoy = LocalDate.now().dayOfWeek.value
    return diasDeLaSemana.joinToString("") { dia ->
        if (diasDeLaSemana.indexOf(dia) == (hoy % 7)) dia else "_"
    }
}
 */

