package com.idh.alarmadespertador.screens.temporizadorscreens


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.idh.alarmadespertador.viewmodels.TemporizadorViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState



@Composable
fun TemporizadorScreen(
    viewModel: TemporizadorViewModel = hiltViewModel()
) {
    val temporizadores by viewModel.temporizador.collectAsState(
        initial = emptyList())

    Text(text = "HOLA")
}









