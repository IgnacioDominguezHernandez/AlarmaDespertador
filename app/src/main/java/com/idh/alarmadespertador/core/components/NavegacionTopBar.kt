package com.idh.alarmadespertador.core.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Chair
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Snooze
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.idh.alarmadespertador.navigation.NavScreen
import com.idh.alarmadespertador.navigation.getCurrentScreenTitle
import com.idh.alarmadespertador.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavegacionTopBar(navController: NavHostController) {

    val title = getCurrentScreenTitle(navController)
    //variable de estado (mutableStateOf) que controla la visibilidad de un menú desplegable
    val (showPuntos, setShowPuntos) = remember { mutableStateOf(false) }
    val mainViewModel: MainViewModel = viewModel()
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        // Ajustando el color del contenedor y la elevación
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        ),
        actions = {
            // íconos y lógica de acciones
            IconButton(onClick = {
                mainViewModel.showBottomSheet = true
            }) {
                Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Configuración")
            }
            // Controlador del menú desplegable
            DropdownMenuController(navController, showPuntos, setShowPuntos)
        },
    )
}

//El componente DropdownMenu se expande según el estado de showPuntos.
// Si showPuntos es true, se muestra el menú; si es false, se oculta.
// El evento onDismissRequest se maneja para establecer showPuntos en false,
// cerrando así el menú cuando se hace clic fuera de él.

@Composable
fun DropdownMenuController(
    navController: NavController,
    showPuntos: Boolean,
    setShowPuntos: (Boolean) -> Unit
) {
    IconButton(onClick = { setShowPuntos(true) }) {
        Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = "Más Opciones")
    }
    //DropdownMenuController, los tres puntos con sus distintas opciones
    DropdownMenu(expanded = showPuntos,
        onDismissRequest = { setShowPuntos(false) }) {
        DropdownMenuItem(
            onClick = {
                setShowPuntos(false)
                navController.navigate(NavScreen.ConfigurarApariencia.name)
            },
            modifier = Modifier
                .width(200.dp)
                .height(48.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Palette, contentDescription = "Temas")
            },
            text = {
                Text(
                    text = "Temas",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        )
        DropdownMenuItem(
            onClick = {
                setShowPuntos(false)
                navController.navigate(NavScreen.EstadisticaMeditacion.name)
            },
            modifier = Modifier
                .width(200.dp)
                .height(48.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Assessment, contentDescription = "Estadísticas")
            },
            text = {
                Text(
                    text = "Estadisticas",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        )
        DropdownMenuItem(
            onClick = {
                setShowPuntos(false)
                navController.navigate(NavScreen.ConfigurarFormato.name)
            },
            modifier = Modifier
                .width(200.dp)
                .height(48.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Snooze, contentDescription = "Postponer")
            },
            text = {
                Text(
                    text = "Postponer",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        )
        DropdownMenuItem(
            //Como en todos, cierro el dropdownmenu y redirijo a la vista correspondiente
            onClick = {
                setShowPuntos(false)
                navController.navigate(NavScreen.AcercaDe.name)
            },
            modifier = Modifier
                .width(200.dp)
                .height(48.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Info, contentDescription = "Acerca de")
            },
            text = {
                Text(
                    text = "Acerca de",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        )
    }
}
