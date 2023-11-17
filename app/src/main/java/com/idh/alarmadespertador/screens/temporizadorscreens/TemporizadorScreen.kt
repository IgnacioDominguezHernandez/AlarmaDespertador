package com.idh.alarmadespertador.screens.temporizadorscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idh.alarmadespertador.R

@Composable
fun TemporizadorScreen() {
    // Aquí podrías añadir la lógica para manejar el estado del temporizador
    val tiempoRestante = remember { mutableStateOf("00:00:00") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = tiempoRestante.value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        // Botones para controlar el temporizador
        Row {
            Button(onClick = { /* Iniciar temporizador */ }) {
                Text("Iniciar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { /* Pausar temporizador */ }) {
                Text("Pausar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { /* Reiniciar temporizador */ }) {
                Text("Reiniciar")
            }
        }
    }
}