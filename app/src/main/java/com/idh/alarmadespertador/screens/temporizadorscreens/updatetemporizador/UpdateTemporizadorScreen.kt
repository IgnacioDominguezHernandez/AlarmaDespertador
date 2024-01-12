package com.idh.alarmadespertador.screens.temporizadorscreens.updatetemporizador

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.idh.alarmadespertador.screens.temporizadorscreens.updatetemporizador.components.UpdateTemporizadorContent
import com.idh.alarmadespertador.viewmodels.TemporizadorViewModel

//Vista de update temporizador
@Composable
fun UpdateTemporizadorScreen(
    viewModel: TemporizadorViewModel = hiltViewModel(),
    navigateBack: () -> Unit
){
    
    Column {
        // Botón o ícono de flecha hacia atrás
        IconButton(onClick = navigateBack) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
        }


    }
}

