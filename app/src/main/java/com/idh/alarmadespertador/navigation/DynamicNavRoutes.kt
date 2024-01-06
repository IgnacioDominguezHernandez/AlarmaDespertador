package com.idh.alarmadespertador.navigation

//El objeto DynamicNavRoutes es un singleton que proporciona métodos
// para generar rutas dinámicas en el sistema de navegación:

object DynamicNavRoutes {
    // Genera la ruta para la pantalla de edición de temporizadores con un ID específico.
    fun updateTemporizadorScreen(temporizadorId: Int) = "updateTemporizadorScreen/$temporizadorId"
    // fun updateAlarmaScreen(alarmaId: Int) = "updateAlarmaScreen/$alarmaId"

}



