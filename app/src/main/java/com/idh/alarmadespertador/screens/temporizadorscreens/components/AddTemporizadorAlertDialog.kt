package com.idh.alarmadespertador.screens.temporizadorscreens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idh.alarmadespertador.core.constants.Constantes.Companion.DISMISS
import com.idh.alarmadespertador.domain.models.EstadoReloj
import com.idh.alarmadespertador.domain.models.Temporizador

@Preview(showBackground = true)
@Composable
fun PreviewAddTemporizadorAlertDialog() {
    Surface(color = MaterialTheme.colorScheme.background) {
        AddTemporizadorAlertDialog(
            openDialog = true,
            closeDialog = {},
            addTemporizador = {},
        )
    }
}
// Vista del Alert para añadir un nuevo temporizador.
// Este temporizador cuando está con los datos necesarios se guarda en la BD a traves del ViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTemporizadorAlertDialog(
    openDialog: Boolean,
    closeDialog: () -> Unit,
    addTemporizador: (temporizador: Temporizador) -> Unit,
) {
    if (openDialog) {

        var nombreTemporizador by remember { mutableStateOf("") }
        var horas by remember { mutableStateOf(0) }
        var minutos by remember { mutableStateOf(0) }
        var segundos by remember { mutableStateOf(0) }
        var vibracion by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { closeDialog },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Agregar Temporizador",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(Modifier.height(24.dp))
                    // Este Spacer empuja el texto al centro
                    // Muestra el tiempo en formato 00:00:00
                    Text(
                        text = String.format("%02d:%02d:%02d", horas, minutos, segundos),
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 48.sp),
                        color = MaterialTheme.colorScheme.onSurface// Cambia el tamaño del texto
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // NumberPickers para horas, minutos y segundos
                    NumberPickerM(
                        value = horas,
                        range = 0..23,
                        onValueChange = { horas = it },
                        label = "horas"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    NumberPickerM(
                        value = minutos,
                        range = 0..59,
                        onValueChange = { minutos = it },
                        label = "minutos"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    NumberPickerM(
                        value = segundos,
                        range = 0..59,
                        onValueChange = { segundos = it },
                        label = "segundos"
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    TextField(
                        value = nombreTemporizador,
                        onValueChange = { nombreTemporizador = it },
                        label = {
                            Text(
                                "nombre temporizador",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp)
                    ) {
                        Switch(
                            checked = vibracion,
                            onCheckedChange = { vibracion = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Vibración",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(32.dp)) // Espacio entre el switch y el botón

                        Button(onClick = { /* Implementar lógica */ }) {
                            Text(
                                "Melodía", style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp)) // Espacio entre las filas

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                horas = 0; minutos = 0; segundos = 0; nombreTemporizador = ""
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer, // Cambia el color del botón
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer // Cambia el color del texto/icono
                            )
                        ) {
                            Text(
                                "Reset", style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { /* Lógica de confirmación */ },
                    enabled = horas != 0 || minutos != 0 || segundos != 0,
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
                OutlinedButton(
                    onClick = { closeDialog() },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Cancelar", style = MaterialTheme.typography.bodyMedium)
                }
            }
        )
    }
}

@Composable
fun NumberPickerM(value: Int, range: IntRange, onValueChange: (Int) -> Unit, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Mostrar el valor actual seleccionado con una etiqueta
        Text(
            text = "$value $label",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp)
        )
        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = value.toFloat(),
            onValueChange = {
                onValueChange(it.toInt())
            },
            valueRange = range.first.toFloat()..range.last.toFloat(),
            steps = range.last - range.first - 1
        )
    }
}