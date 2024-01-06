package com.idh.alarmadespertador.core.components

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object ThemeEventBus {

    // _themeEvent es una instancia privada de MutableSharedFlow que puede emitir eventos de cambio de tema.
    // MutableSharedFlow es un tipo de flujo que permite emitir valores (en este caso, Boolean) a múltiples colectores.

    private val _themeEvent = MutableSharedFlow<Boolean>()

    // themeEvent es una versión pública y de solo lectura de _themeEvent. Se utiliza SharedFlow en lugar de StateFlow
    // porque no se necesita mantener el último valor emitido para nuevos colectores.

    val themeEvent = _themeEvent.asSharedFlow()

    // La función emitThemeChange se usa para emitir eventos de cambio de tema.
    // Cuando se llama a esta función con un valor booleano, emite ese valor a través de _themeEvent.
    // Los colectores de themeEvent recibirán este valor para realizar acciones correspondientes, como cambiar el tema de la app.
    // Se introduce esta clase para así manejar el cambio de Theme de la aplicación.

    suspend fun emitThemeChange(isDarkTheme: Boolean) {
        _themeEvent.emit(isDarkTheme)
    }
}
