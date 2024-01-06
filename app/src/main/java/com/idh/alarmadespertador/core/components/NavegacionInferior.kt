package com.idh.alarmadespertador.core.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.idh.alarmadespertador.domain.models.ItemsBottomNav.*
import com.idh.alarmadespertador.navigation.currentRoute

//@Composable: parte de la UI de Jetpack Compose. Puede ser reutilizada en diferentes partes de la aplicación
@Composable
//Acepta un NavHostController como parámetro, que se utiliza para manejar la navegación dentro de la aplicación
fun NavegacionInferior(
    navController: NavController
) {
    //Define una lista menuItems de elementos que representarán los ítems en la barra de navegación inferior.
    //Instancias de un objeto
    val menuItems = listOf(
        Itembottomnav1,
        Itembottomnav3,
        Itembottomnav4
    )
    //Utiliza BottomAppBar y NavigationBar para construir la interfaz de la barra de navegación inferior.
    //Dentro del NavigationBar, se itera sobre menuItems para crear un NavigationBarItem para cada elemento.
    BottomAppBar(tonalElevation = 8.dp) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        ) {
            menuItems.forEach { item ->
                val selected = currentRoute(navController) == item.ruta
                NavigationBarItem(selected = selected,
                    //Para cada NavigationBarItem, se establece un onClick que cambia la navegación a la ruta asociada con ese ítem
                    onClick = {
                        navController.navigate(item.ruta) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    //Se muestra un icono y un texto para cada ítem, basados en las propiedades del objeto del menú.
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
            }
        }
    }
}