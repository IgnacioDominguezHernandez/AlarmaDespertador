package com.idh.alarmadespertador.screens.temporizadorscreens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {closeDialog() } ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                    Text("Agregar Temporizador", style = MaterialTheme.typography.titleLarge)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(Modifier.height(16.dp))
                    // Este Spacer empuja el texto al centro
                    // Muestra el tiempo en formato 00:00:00
                    Text(
                        text = String.format("%02d:%02d:%02d", horas, minutos, segundos),
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 48.sp) // Cambia el tamaño del texto
                    )

                    Spacer(modifier = Modifier.height(26.dp))

                    // NumberPickers para horas, minutos y segundos
                    com.idh.alarmadespertador.screens.temporizadorscreens.updatetemporizador.components.NumberPicker(
                        value = horas,
                        range = 0..23,
                        onValueChange = { horas = it },
                        label = "horas"
                    )
                    com.idh.alarmadespertador.screens.temporizadorscreens.updatetemporizador.components.NumberPicker(
                        value = minutos,
                        range = 0..59,
                        onValueChange = { minutos = it },
                        label = "minutos"
                    )
                    com.idh.alarmadespertador.screens.temporizadorscreens.updatetemporizador.components.NumberPicker(
                        value = segundos,
                        range = 0..59,
                        onValueChange = { segundos = it },
                        label = "segundos"
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = nombreTemporizador,
                        onValueChange = { nombreTemporizador = it },
                        label = { Text("Nombre del Temporizador") }
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp) // Ajusta el padding según sea necesario
                    ) {
                        Checkbox(
                            checked = vibracion,
                            onCheckedChange = { vibracion = it }
                        )
                        Text("Vibración")
                    }

                    Button(onClick = { /* Implementar lógica */ }) {
                        Text("Seleccionar Notificación")
                    }

                    Spacer(modifier = Modifier.height(26.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(onClick = {
                            horas = 0; minutos = 0; segundos = 0; nombreTemporizador = ""
                        }) {
                            Text("Reset")
                        }
                        Button(
                            onClick = {
                                val totalMillis = (horas * 3600000) + (minutos * 60000) + (segundos * 1000)
                                if (totalMillis > 0) {
                                    val newTemporizador = Temporizador(
                                        id = 0, // ID generado automáticamente
                                        milisegundos = totalMillis,
                                        nombreTemporizador = nombreTemporizador,
                                        vibracion = vibracion,
                                        sonidoUri = "", // URI de sonido en blanco por ahora
                                        estadoTemp = EstadoReloj.ACTIVO
                                    )
                                    addTemporizador(newTemporizador)
                                    closeDialog()
                                }
                            },
                            enabled = horas != 0 || minutos != 0 || segundos != 0 // Habilitado solo si algún valor es diferente de cero
                        ) {
                            Text("Confirmar")
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = { TextButton(onClick = closeDialog) { Text(DISMISS) } }
        )
    }
}

@Composable
fun NumberPicker(value: Int, range: IntRange, onValueChange: (Int) -> Unit, label: String) {
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