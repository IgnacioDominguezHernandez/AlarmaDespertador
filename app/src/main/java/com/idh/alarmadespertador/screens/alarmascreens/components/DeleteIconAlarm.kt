package com.idh.alarmadespertador.screens.alarmascreens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.idh.alarmadespertador.core.constants.Constantes

@Composable
fun DeleteIconAlarm (
    eliminarAlarma : () -> Unit
) {
    IconButton(onClick =  eliminarAlarma ) {
        Icon(imageVector = Icons.Default.Delete,
            contentDescription  = Constantes.DELETE_TEMPORIZADOR
        )
    }
}