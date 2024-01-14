package com.idh.alarmadespertador.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.idh.alarmadespertador.core.constants.Constantes.Companion.ALARMA_SCREEN
import com.idh.alarmadespertador.core.constants.Constantes.Companion.CLIMA_SCREEN
import com.idh.alarmadespertador.core.constants.Constantes.Companion.MEDITACION_SCREEN
import com.idh.alarmadespertador.screens.topupscreens.ConfigurarApariencia
import com.idh.alarmadespertador.screens.topupscreens.ConfigurarFormato
import com.idh.alarmadespertador.screens.alarmascreens.AlarmaScreen
import com.idh.alarmadespertador.screens.climascreens.ClimaScreen
import com.idh.alarmadespertador.screens.meditacionscreen.MeditacionScreen
import com.idh.alarmadespertador.screens.meditacionscreen.updatetemporizador.UpdateTemporizadorScreen
import com.idh.alarmadespertador.screens.topupscreens.AcercaDe
import com.idh.alarmadespertador.screens.topupscreens.EstadisticaMeditacion

/* Cada llamada a Composable define una ruta y su pantalla asociada.
    Por ejemplo, composable(NavScreen.AlarmScreen.name)
   { AlarmaScreen() } define la ruta para la pantalla de alarma.
    Estas rutas utilizan el enum NavScreen para nombrar cada ruta. */

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavScreen.AlarmScreen.name
    ) {
        composable(NavScreen.AlarmScreen.name) {
            AlarmaScreen()
        }
        composable(NavScreen.MeditacionScreen.name) {
            MeditacionScreen()
        }
        composable(NavScreen.ClimaScreen.name) {
            ClimaScreen()
        }
        composable(NavScreen.ConfigurarApariencia.name) {
            ConfigurarApariencia()
        }
        composable(NavScreen.AcercaDe.name) {
            AcercaDe()
        }
        composable(NavScreen.ConfigurarFormato.name) {
            ConfigurarFormato()
        }
        composable(NavScreen.EstadisticaMeditacion.name) {
            EstadisticaMeditacion()
        }
        composable(
            route = "rutaDeUpdateTemporizadorScreen/{temporizadorId}", // Usa la ruta directamente
            arguments = listOf(
                navArgument("temporizadorId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val temporizadorId = backStackEntry.arguments?.getInt("temporizadorId") ?: 0
            UpdateTemporizadorScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun getCurrentScreenTitle(navController: NavHostController): String {
    // Obtiene el destino actual de la navegación
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    return when (currentDestination?.route) {
        NavScreen.AlarmScreen.name -> ALARMA_SCREEN
        NavScreen.MeditacionScreen.name -> MEDITACION_SCREEN
        NavScreen.ClimaScreen.name -> CLIMA_SCREEN
        // Añadir aquí más casos para otras pantallas
        else -> "Aplicación"
    }
}
