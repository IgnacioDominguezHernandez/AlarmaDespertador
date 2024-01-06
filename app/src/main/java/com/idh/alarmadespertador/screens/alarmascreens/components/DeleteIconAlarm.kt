package com.idh.alarmadespertador.screens.alarmascreens.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.idh.alarmadespertador.core.constants.Constantes

//Composable para el botón Delete de los cards en alarma.
@Composable
fun DeleteIconAlarm(
    eliminarAlarma: () -> Unit
) {
    IconButton(onClick = eliminarAlarma, modifier = Modifier.shadow(1.dp, shape = CircleShape)) {
        Icon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = Constantes.DELETE_TEMPORIZADOR,
            modifier = Modifier.size(24.dp)
        )
    }
}