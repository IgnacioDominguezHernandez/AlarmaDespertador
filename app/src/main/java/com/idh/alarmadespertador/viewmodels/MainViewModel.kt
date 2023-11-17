package com.idh.alarmadespertador.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    //El bottomSheet está oculto aqui
    var showBottomSheet by mutableStateOf(false)
}