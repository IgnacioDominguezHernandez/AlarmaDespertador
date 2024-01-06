package com.idh.alarmadespertador.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/* MainViewModel está manteniendo el estado de un bottom sheet (hoja inferior) con la variable showBottomSheet.
Esta variable utiliza mutableStateOf de Jetpack Compose, lo que indica que es una variable de estado observable que
puede causar que la UI se recomponga cuando cambie su valor. */

/* Esto significa que cualquier cambio en showBottomSheet será observado por la UI, y los composable que dependen
de este estado se actualizará automáticamente. Esta es una forma efectiva de mantener
la UI sincronizada con el estado de la aplicación */

class MainViewModel : ViewModel() {
    //El bottomSheet está oculto aqui
    var showBottomSheet by mutableStateOf(false)
}