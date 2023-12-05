package com.idh.alarmadespertador.screens.temporizadorscreens.updatetemporizador.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idh.alarmadespertador.domain.models.Temporizador


/*
@Preview(showBackground = true)
@Composable
fun PreviewUpdateTemporizadorContent() {
    // Crea un temporizador de ejemplo con valores predeterminados
    val temporizadorDeEjemplo = Temporizador(
        id = 1, // ID de ejemplo
        milisegundos = 0, // 1 hora en milisegundos
        nombreTemporizador = "Ejemplo",
        vibracion = true,
        sonidoUri = "uri_del_sonido_ejemplo",
        estadoTemp = EstadoReloj.ACTIVO
    )

    UpdateTemporizadorContent(
        padding = PaddingValues(),
        temporizador = temporizadorDeEjemplo,
     //   updateNombreTemporizador = {}, // Implementación vacía para la vista previa
        updateTemporizador = {}, // Implementación vacía para la vista previa
        navigateBack = {} // Implementación vacía para la vista previa
    )
}

 */

@Composable
fun UpdateTemporizadorContent(
    padding: PaddingValues,
    temporizador: Temporizador,
    updateTemporizador: (temporizador : Temporizador) -> Unit,
    navigateBack: () -> Unit
) {

    Log.d("Lamando a update EN CONTENT", "ID: ${temporizador.nombreTemporizador}")
    // Calcula horas, minutos y segundos a partir de los milisegundos
    // Inicializa los estados con valores predeterminados
    var horas by remember { mutableStateOf(0) }
    var minutos by remember { mutableStateOf(0) }
    var segundos by remember { mutableStateOf(0) }
    var vibracion by remember { mutableStateOf(false) }
    var nombreTemporizador by remember { mutableStateOf("") }

    // Actualiza los estados cuando el temporizador cambia
    LaunchedEffect(temporizador) {
        horas = temporizador.milisegundos / 3600000
        minutos = (temporizador.milisegundos % 3600000) / 60000
        segundos = (temporizador.milisegundos % 60000) / 1000
        vibracion = temporizador.vibracion
        nombreTemporizador = temporizador.nombreTemporizador
    }
    Log.d("UpdateTemporizadorContent", "ID: ${temporizador.id}, Horas: $horas, Minutos: $minutos, Segundos: $segundos, Vibración: $vibracion, Nombre: $nombreTemporizador")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(Modifier.width(10.dp)) // Añade un espacio entre el ícono y el próximo elemento
            IconButton(onClick = navigateBack,
                modifier = Modifier
                    .size(48.dp) // Ajusta el tamaño del botón
                    .then(Modifier.weight(1f, fill = false))) {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    modifier = Modifier.size(52.dp))
            }
            Spacer(Modifier.width(66.dp)) // Añade un espacio entre el ícono y el próximo elemento
            Text(
                "Actualizar",
                modifier = Modifier.weight(8f),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 24.sp)
            )
            Spacer(Modifier.weight(1f))
        }
        Spacer(Modifier.height(36.dp))
         // Este Spacer empuja el texto al centro
        // Muestra el tiempo en formato 00:00:00
        Text(
            text = String.format("%02d:%02d:%02d", horas, minutos, segundos),
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 48.sp) // Cambia el tamaño del texto
        )

        Spacer(modifier = Modifier.height(36.dp))

        // NumberPickers para horas, minutos y segundos
        NumberPicker(
            value = horas,
            range = 0..23,
            onValueChange = { horas = it },
            label = "horas"
        )
        NumberPicker(
            value = minutos,
            range = 0..59,
            onValueChange = { minutos = it },
            label = "minutos"
        )
        NumberPicker(
            value = segundos,
            range = 0..59,
            onValueChange = { segundos = it },
            label = "segundos"
        )
        Spacer(modifier = Modifier.height(26.dp))

        // Checkbox para vibración
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = vibracion,
                onCheckedChange = { isChecked ->
                    vibracion = isChecked
                }
            )
            Text(text = "Vibración")
        }
        Spacer(modifier = Modifier.height(36.dp))

        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            // Calcula el total de milisegundos a partir de horas, minutos y segundos
            val totalMilisegundos = (horas * 3600000) + (minutos * 60000) + (segundos * 1000)

            Button(
                onClick = {
                    horas = 0
                    minutos = 0
                    segundos = 0
                    // Restablecer también el nombre si es necesario
                    nombreTemporizador= ""
                }
            ) {
                Text("Reset")
            }

            Button(
                onClick = {
                    val temporizadorActualizado = Temporizador(
                        id = temporizador.id, // Mantén el mismo ID para actualizar el temporizador existente
                        milisegundos = totalMilisegundos,
                        nombreTemporizador = temporizador.nombreTemporizador, // o cualquier valor que hayas actualizado
                        vibracion = vibracion, // Valor actual de vibración
                        sonidoUri = temporizador.sonidoUri, // o la URI actualizada de la música
                        estadoTemp = temporizador.estadoTemp // o el estado actualizado
                    )
                    updateTemporizador(temporizadorActualizado) // Llama a la función para actualizar el temporizador
                    navigateBack() // Vuelve a la pantalla anterior o cierra el diálogo
                },
                enabled = horas != 0 || minutos != 0 || segundos != 0 // Habilitado solo si algún valor es diferente de cero
            ) {
                Text("Confirmar")
            }

        }
        }

    }

@Composable
fun NumberPicker(value: Int, range: IntRange, onValueChange: (Int) -> Unit, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Mostrar el valor actual seleccionado con una etiqueta
        Text(
            text = "$value $label",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
            modifier = Modifier.align(Alignment.Start).
            padding(start = 16.dp)
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