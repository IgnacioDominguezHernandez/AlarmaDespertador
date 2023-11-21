package com.idh.alarmadespertador.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.idh.alarmadespertador.screens.ConfigurarApariencia
import com.idh.alarmadespertador.screens.ConfigurarFormato
import com.idh.alarmadespertador.screens.alarmascreen.AlarmaScreen
import com.idh.alarmadespertador.screens.climascreens.ClimaScreen
import com.idh.alarmadespertador.screens.radioscreens.RadioScreen
import com.idh.alarmadespertador.screens.temporizadorscreens.TemporizadorScreen

/* Cada llamada a Composable define una ruta y su pantalla asociada. Por ejemplo, composable(NavScreen.AlarmScreen.name)
   { AlarmaScreen() } define la ruta para la pantalla de alarma.
    Estas rutas utilizan el enum NavScreen para nombrar cada ruta. */

@Composable
fun AppNavigation (
    navController: NavHostController
) {
    NavHost (
        navController = navController,
        startDestination = NavScreen.AlarmScreen.name
    ) {
        composable(NavScreen.AlarmScreen.name) {
            AlarmaScreen()
        }
        composable(NavScreen.RadioScreen.name) {
            RadioScreen()
        }
        composable(NavScreen.TemporizadorScreen.name) {
            TemporizadorScreen()
        }
        composable(NavScreen.ClimaScreen.name) {
            ClimaScreen()
        }
        composable(NavScreen.ConfigurarApariencia.name) {
            ConfigurarApariencia()
        }
        composable(NavScreen.ConfigurarFormato.name) {
            ConfigurarFormato()
        }

    }
}