package com.idh.alarmadespertador

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.idh.alarmadespertador.core.components.NavegacionInferior
import com.idh.alarmadespertador.core.components.NavegacionTopBar
import com.idh.alarmadespertador.domain.models.ItemsOptionsConfig.*
import com.idh.alarmadespertador.navigation.AppNavigation
import com.idh.alarmadespertador.viewmodels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

/*Dentro de MyApp, se configura un NavController se usa un Scaffold para estructurar la UI,
incluyendo una barra de navegación inferior (BottomNavigationBar).*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
        val navController = rememberNavController()
        /*El NavHost define la navegación entre diferentes composables.
            El startDestination está configurado como "splash_screen".*/
        NavHost(
            navController = navController,
            startDestination = "splash_screen",
            modifier = Modifier.padding() // Aquí aplicas el padding
        ) {
            composable("splash_screen") {
                SplashScreen(navController = navController)
            }
            composable("main_screen") {
                MainScreen()
            }
            // Agrega aquí más composable para otras pantallas
        }
}

/*
@Composable
fun BottomNavigationBar(navController: NavController) {
    // Implementa tu BottomAppBar aquí
    // Usa navController.navigate("destino") para manejar la navegación
}
 */

@Composable
fun MainScreen() {

    val navController = rememberNavController()
    Contenido(navController = navController)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Contenido (
    navController: NavHostController
) {
    val mainViewModel : MainViewModel = viewModel()
    val sheetState  = rememberModalBottomSheetState()
    Scaffold(
        topBar = {
                 NavegacionTopBar(navController = navController)
        },
        bottomBar = {
            NavegacionInferior(navController)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            AppNavigation(navController = navController)
        }
        if (mainViewModel.showBottomSheet) {
            ModalBottomSheet(onDismissRequest = {
                mainViewModel.showBottomSheet = false
            }) {
            ContentBottomSheet(navController , sheetState, mainViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentBottomSheet(
    navController: NavHostController,
    sheetState: SheetState,
    mainViewModel: MainViewModel) {
    val itemsConfiguration = listOf(
        ItemOptConfig1,
        ItemOptConfig2
    )
    val scope = rememberCoroutineScope()
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .padding(horizontal = 38.dp)
    ) {
        Text(text = "Configuraciones",
            style = MaterialTheme.typography.bodyMedium)
        //Aquí opciones de configuración
        itemsConfiguration.forEach { item ->
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(48.dp)
                    .clickable {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            mainViewModel.showBottomSheet = false
                        }
                        navController.navigate(item.ruta)
                    }
                ) {
                Icon(item.icon, contentDescription = item.title)
                Spacer(modifier = Modifier.width(24.dp))
                Text(text = item.title)
            }
        }
    }
}

/*En SplashScreen, se utiliza LaunchedEffect para realizar
una acción una vez que este Composable se ha lanzado. Aquí, el efecto causa un
retraso de X segundo (delay(xxxL)) y luego navega a "main_screen" El SplashScreen
se mostrará inicialmente debido a que es el startDestination en el NavHost*/

//Para mirar
/*Considerar si hay alguna inicialización de la aplicación que deba ocurrir durante
el SplashScreen. Este es un buen momento para realizar operaciones de carga inicial.*/

@Composable
fun SplashScreen(navController: NavController) {

    LaunchedEffect(key1 = true) {
        delay(1000L)
        navController.navigate("main_screen") {
            popUpTo("splash_screen") { inclusive = true }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.relojtres),
            "",
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
        )
    }
}



