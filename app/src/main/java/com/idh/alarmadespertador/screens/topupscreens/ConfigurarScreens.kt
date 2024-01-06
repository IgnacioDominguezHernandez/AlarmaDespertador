package com.idh.alarmadespertador.screens.topupscreens

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.idh.alarmadespertador.core.components.ThemeEventBus
import com.idh.alarmadespertador.viewmodels.TopAppViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

//Vista de configurar apariencia, tiene dos botones de tema claro, oscuro
// solo funciona el boton del tema que no está activo.
@Composable
fun ConfigurarApariencia() {

    val scope = rememberCoroutineScope()
    val temaActual by ThemeEventBus.themeEvent.collectAsState(initial = false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Botón para cambiar al tema claro
        Button(
            onClick = {
                scope.launch {
                    ThemeEventBus.emitThemeChange(false) // Tema claro
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!temaActual) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                "Tema Claro",
                style = MaterialTheme.typography.titleLarge,
                color = if (!temaActual) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
            )
        }

        //Lanzamos una corrutina que emite un cambio de tema a través de ThemeEventBus.emitThemeChange(true)
        Spacer(modifier = Modifier.weight(1f))
        // Botón para cambiar al tema oscuro
        Button(
            onClick = {
                scope.launch {
                    ThemeEventBus.emitThemeChange(true) // Tema oscuro
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (temaActual) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                "Tema Oscuro",
                style = MaterialTheme.typography.titleLarge,
                color = if (temaActual) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

//Vista de opcion a usuario de cambiar el tiempo en el que vuelve a sonar la alarma si en vez de apagarse
//se pulsa en Snooze. Guardamos el valor en SHARED PREFERENCES para que se use siempre este valor
@Composable
fun ConfigurarFormato(onSnoozeTimeSelected: (Int) -> Unit = {}) {

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
    val snoozeTimeKey = "snoozeTime"
    var mostrarDialogoConfirmacion by remember { mutableStateOf(false) }
    // Lee el valor guardado o usa 5 como predeterminado
    var snoozeMinutes by remember {
        mutableStateOf(sharedPreferences.getInt(snoozeTimeKey, 5).toFloat())
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sonar en...",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Snooze: ${snoozeMinutes.roundToInt()} minutos",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Slider(
            value = snoozeMinutes,
            onValueChange = { snoozeMinutes = it },
            valueRange = 1f..30f,
            steps = 29,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        //Guardamos el valor en SHARED PREFERENCES para que se use siempre este valor

        Button(
            onClick = {
                val snoozeTime = snoozeMinutes.roundToInt()
                with(sharedPreferences.edit()) {
                    putInt(snoozeTimeKey, snoozeTime)
                    apply()
                }
                mostrarDialogoConfirmacion = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Aceptar", style = MaterialTheme.typography.labelLarge)
        }
    }
    if (mostrarDialogoConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoConfirmacion = false },
            title = {
                Text(
                    "Configuración Guardada",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    "Tiempo del snooze actualizado.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(onClick = { mostrarDialogoConfirmacion = false }) {
                    Text(
                        "Aceptar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        )
    }
}




