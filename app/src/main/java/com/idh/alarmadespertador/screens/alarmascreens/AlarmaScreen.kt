package com.idh.alarmadespertador.screens.alarmascreens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.idh.alarmadespertador.screens.alarmascreens.components.AddAlarmaDialog
import com.idh.alarmadespertador.screens.alarmascreens.components.AlarmaCard
import com.idh.alarmadespertador.viewmodels.AlarmaViewModel

@Composable
fun AlarmaScreen(viewModel: AlarmaViewModel = hiltViewModel()) {

    val alarmas = viewModel.alarmas.collectAsState(initial = emptyList())

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Agregar Alarma")
            }
        }
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(alarmas.value) { alarma ->
                AlarmaCard(alarma = alarma, onAlarmaChanged = { viewModel.actualizarAlarma(it) })
            }
        }
        if (showDialog) {
            AddAlarmaDialog(
                onDismiss = { showDialog = false },
                onConfirm = { alarmaData ->
                    // Aquí manejas la confirmación del diálogo, por ejemplo, agregando la alarma
                //    viewModel.addAlarma(alarmaData)
                    showDialog = false
                }
            )
        }
    }
}
