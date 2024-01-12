package com.idh.alarmadespertador.screens.temporizadorscreens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.idh.alarmadespertador.core.constants.Constantes.Companion.DELETE_TEMPORIZADOR

//Icono Delete para la vista del temporizador Card
@Composable
fun DeleteIcon (
    deleteTemporizador : () -> Unit
) {
    IconButton(onClick =  deleteTemporizador ) {
        Icon(imageVector = Icons.Outlined.Delete,
            contentDescription  = DELETE_TEMPORIZADOR)
    }
}