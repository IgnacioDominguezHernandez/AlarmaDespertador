package com.idh.alarmadespertador.screens.temporizadorscreens.updatetemporizador

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

import com.idh.alarmadespertador.viewmodels.MeditacionViewModel

//Vista de update temporizador
@Composable
fun UpdateTemporizadorScreen(
    viewModel: MeditacionViewModel = hiltViewModel(),
    navigateBack: () -> Unit
){
    
    Column {
        // Botón o ícono de flecha hacia atrás
        IconButton(onClick = navigateBack) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
        }

    }
}

