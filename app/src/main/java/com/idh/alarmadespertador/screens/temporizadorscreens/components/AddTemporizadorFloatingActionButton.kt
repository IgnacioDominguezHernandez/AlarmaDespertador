package com.idh.alarmadespertador.screens.temporizadorscreens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.idh.alarmadespertador.core.constants.Constantes.Companion.ADD_TEMPORIZADOR
@Composable
fun AddTemporizadorFloatingActionButton(
    openDialog: () -> Unit
){
    FloatingActionButton(
        onClick =  openDialog ,
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = ADD_TEMPORIZADOR
        )
    }
}