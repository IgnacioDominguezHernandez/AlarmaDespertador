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

@Composable
fun BottomNavigation (
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