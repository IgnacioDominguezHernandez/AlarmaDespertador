package com.idh.alarmadespertador.screens.temporizadorscreens.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idh.alarmadespertador.domain.models.Temporizador
import com.idh.alarmadespertador.viewmodels.MeditacionViewModel
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TemporizadorCard(
    temporizador: Temporizador, // Pasa el objeto Temporizador completo
    viewModel: MeditacionViewModel
) {

    MostrarDialogoFinTemporizador()

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Mostrar el tiempo inicial y el tiempo restante
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatearTiempo(temporizador.milisegundosInicial),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = formatearTiempo(temporizador.milisegundosRestantes),
                    fontSize = 24.sp
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Iconos de acciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
            }
        }
    }
}

fun formatearTiempo(milisegundos: Long): String {
    // Convierte milisegundos a minutos y segundos, por ejemplo
    val minutos = TimeUnit.MILLISECONDS.toMinutes(milisegundos)
    val segundos = TimeUnit.MILLISECONDS.toSeconds(milisegundos) % 60
    return String.format("%02d:%02d", minutos, segundos)
}



