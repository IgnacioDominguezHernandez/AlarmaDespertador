package com.idh.alarmadespertador.screens.temporizadorscreens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.idh.alarmadespertador.core.constants.Constantes.Companion.DELETE_TEMPORIZADOR

@Composable
fun DeleteIcon (
    deleteTemporizador : () -> Unit
) {
    IconButton(onClick =  deleteTemporizador ) {
        Icon(imageVector = Icons.Default.Delete,
            contentDescription  = DELETE_TEMPORIZADOR)
    }
}