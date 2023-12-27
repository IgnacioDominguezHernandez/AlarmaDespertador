package com.idh.alarmadespertador.screens.alarmascreens.components

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.idh.alarmadespertador.core.components.RingtonePickerDropdown
import com.idh.alarmadespertador.domain.models.Alarma
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun AddAlarmaDialog(onDismiss: () -> Unit, onConfirm: (Alarma) -> Unit) {
    //  val context = LocalContext.current
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    var isVibrationEnabled by remember { mutableStateOf(false) }
    var selectedRingtoneUri by remember { mutableStateOf("") }
    var selectedRingtoneTitle by remember { mutableStateOf("") }
    var diasSeleccionados by remember { mutableStateOf(setOf<String>()) }

    val diasDeLaSemana = listOf("L", "M", "X", "J", "V", "S", "D")

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Nueva Alarma") },
        text = {
            Column {
                TimePickerButton(
                    selectedTime = selectedTime,
                    onTimeSelected = { newTime -> selectedTime = newTime })
                Text("Hora seleccionada: ${selectedTime.format(DateTimeFormatter.ofPattern("HH:mm"))}")
                Switch(
                    checked = isVibrationEnabled,
                    onCheckedChange = { isVibrationEnabled = it }
                )
                RingtonePickerDropdown(
                    selectedRingtone = selectedRingtoneTitle,
                    onRingtoneSelected = { title, uri ->
                        selectedRingtoneTitle = title
                        selectedRingtoneUri = uri
                    }
                )
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
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val diasSeleccionadosStr = procesarDiasSeleccionados(diasSeleccionados)
                    val diasFinal = if (diasSeleccionadosStr.isBlank()) seleccionarDiaSiguiente() else diasSeleccionadosStr
                    val now = LocalDateTime.now()
                    val adjustedDateTime =
                        now.withHour(selectedTime.hour).withMinute(selectedTime.minute)
                            .withSecond(0).withNano(0)
                    val tiempoActivacion =
                        adjustedDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

                    onConfirm(
                        Alarma(
                            id = 0,
                            tiempoActivacion,
                            dias = diasFinal,
                            isEnabled = true,
                            vibrate = isVibrationEnabled,
                            soundTitle = selectedRingtoneTitle,
                            soundUri = selectedRingtoneUri,
                            label = ""
                        )
                    )
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}

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
    Button(onClick = { timePickerDialog.show() }) {
        Text("Seleccionar Hora")
    }
}

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

