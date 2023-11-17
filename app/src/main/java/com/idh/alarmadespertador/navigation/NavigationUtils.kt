package com.idh.alarmadespertador.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun currentRoute (navController: NavController): String? =
    navController.currentBackStackEntryAsState().value?.destination?.route


/*navController.currentBackStackEntryAsState() es una llamada que obtiene
la entrada actual (es decir, la pantalla o vista actual) en la pila de navegación como un estado observable
Este estado se actualiza automáticamente cuando la navegación cambia (por ejemplo, cuando
el usuario navega a una pantalla diferente). navBackStackEntry.value?.destination?.route obtiene
la ruta de la entrada actual de la pila de navegación
 Esta ruta es un String que identifica la pantalla actual en el sistema de navegación */