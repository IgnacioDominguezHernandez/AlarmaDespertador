package com.idh.alarmadespertador.core.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.idh.alarmadespertador.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavegacionTopBar(navController: NavHostController) {

    //variable de estado (mutableStateOf) que controla la visibilidad de un menú desplegable
    val (showPuntos, setShowPuntos) = remember { mutableStateOf(false) }
    val mainViewModel : MainViewModel = viewModel()
    TopAppBar(
        title = { Text(text = "Alarma") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        ),
        actions = {
            // Tus íconos y lógica de acciones aquí, por ejemplo:
            IconButton(onClick = {
                mainViewModel.showBottomSheet = true
            }) {
                Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Configuración")
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.Info , contentDescription = "nosetodavia")
            }
            DropdownMenuController(showPuntos, setShowPuntos)
        }
    )
}

@Composable
fun DropdownMenuController(showPuntos: Boolean, setShowPuntos: (Boolean) -> Unit) {
    IconButton(onClick = { setShowPuntos(true) }) {
        Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = "Más Opciones")
    }
    DropdownMenu(expanded = showPuntos, onDismissRequest = { setShowPuntos(false) }) {
        DropdownMenuItem(text = { Text(text = "Temas") }, onClick = { setShowPuntos(false) })
        DropdownMenuItem(text = { Text(text = "Acerca de") }, onClick = { setShowPuntos(false) })
        DropdownMenuItem(text = { Text(text = "Recursos") }, onClick = { setShowPuntos(false) })
    }
}
