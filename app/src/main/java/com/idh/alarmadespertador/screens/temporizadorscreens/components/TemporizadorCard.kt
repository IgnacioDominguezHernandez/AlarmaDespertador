package com.idh.alarmadespertador.screens.temporizadorscreens.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.idh.alarmadespertador.domain.models.EstadoReloj
import com.idh.alarmadespertador.domain.models.Temporizador
import com.idh.alarmadespertador.screens.temporizadorscreens.components.PlayPauseIcons

@Preview(showBackground = true, name = "Temporizador Card Preview")
@Composable
fun TemporizadorCardPreview() {
    // Crear un temporizador de ejemplo
    val temporizadorEjemplo = Temporizador(
        id = 1,
        milisegundos = 3600000, // 1 hora en milisegundos
        nombreTemporizador = "Temporizador Ejemplo",
        vibracion = false,
        sonidoUri = "uri_de_ejemplo",
        estadoTemp = EstadoReloj.ACTIVO
    )

    // Simular funciones que se pasarían normalmente al TemporizadorCard
    val deleteTemporizador = { /* acción de eliminar */ }
    val navigateToUpdateTemporizadorScreen = { _: Int -> /* acción de navegación */ }
    val onPlayPause = { _: EstadoReloj -> /* acción de play/pause */ }

    TemporizadorCard(
        temporizador = temporizadorEjemplo,
        deleteTemporizador = deleteTemporizador,
        navigateToUpdateTemporizadorScreen = navigateToUpdateTemporizadorScreen,

    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TemporizadorCard(
    temporizador: Temporizador,
    deleteTemporizador: () -> Unit,
    navigateToUpdateTemporizadorScreen: (temporizadorId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    // Convertir milisegundos a horas, minutos y segundos
    val horas = temporizador.milisegundos / 3600000
    val minutos = (temporizador.milisegundos % 3600000) / 60000
    val segundos = (temporizador.milisegundos % 60000) / 1000

    var remainingTime by remember { mutableStateOf(temporizador.milisegundos) }
    var timerState by remember { mutableStateOf(temporizador.estadoTemp) }

    // Lógica del contador regresivo
    LaunchedEffect(timerState) {
        if (timerState == EstadoReloj.ACTIVO) {
            // Aquí, decrementar remainingTime cada segundo
        }
    }

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 4.dp,
                bottom = 4.dp
            )
            .fillMaxWidth()
            .combinedClickable(
                onClick = { /* Acción para un clic normal, si es necesario */ },
                onLongClick = {
                    navigateToUpdateTemporizadorScreen(temporizador.id)
                }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {

                // Usar Row en lugar de Column para mostrar horas, minutos y segundos en una línea
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Formatear el tiempo en un formato de cuenta regresiva
                    val tiempoFormateado = String.format("%02d:%02d:%02d", horas, minutos, segundos)
                    Text(
                        text = tiempoFormateado,
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 44.sp), // Aumentar el tamaño de la fuente
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                    )
                }
                Text(temporizador.nombreTemporizador, style = MaterialTheme.typography.titleSmall)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {

                Spacer(modifier = Modifier.width(38.dp))

                DeleteIcon(deleteTemporizador = deleteTemporizador)

                Spacer(modifier = Modifier.width(38.dp))

            }
        }
    }
}


