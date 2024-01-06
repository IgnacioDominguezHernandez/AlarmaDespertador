package com.idh.alarmadespertador.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idh.alarmadespertador.core.components.ThemeEventBus
import com.idh.alarmadespertador.ui.theme.AlarmadespertadorTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopAppViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    // utilizado para gestionar la preferencia del tema oscuro en una aplicación Android,
    // utilizando SharedPreferences para almacenar la elección del usuario y un bus de eventos para notificar los cambios
    //Lanza una coroutina en viewModelScope y utiliza ThemeEventBus.emitThemeChange(isDarkTheme) para
    // emitir un evento de cambio de tema. Esto permite que otras partes de la aplicación que
    // estén observando este evento puedan reaccionar y actualizar la UI en consecuencia

    fun setDarkTheme(isDarkTheme: Boolean) {
        sharedPreferences.edit().putBoolean("darkTheme", isDarkTheme).apply()
        // Además, emite el evento de cambio de tema
        viewModelScope.launch {
            ThemeEventBus.emitThemeChange(isDarkTheme)
        }
    }
}


