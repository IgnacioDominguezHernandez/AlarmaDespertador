package com.idh.alarmadespertador.screens.temporizadorscreens.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MostrarDialogoFinTemporizador(
    mostrarDialogo: Boolean,
    onDismiss: () -> Unit, // Función que se llama cuando el diálogo se cierra
    nombreTemporizador: String,
    idTemporizador: Int
) {
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text("Temporizador Finalizado") },
            text = { Text("El temporizador $nombreTemporizador (ID: $idTemporizador) ha finalizado.") },
            confirmButton = {
                Button(onClick = { onDismiss() }) {
                    Text("Aceptar")
                }
            }
        )
    }
}
