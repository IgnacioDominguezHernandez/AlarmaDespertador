package com.idh.alarmadespertador

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.idh.alarmadespertador.core.components.NavegacionInferior
import com.idh.alarmadespertador.core.components.NavegacionTopBar
import com.idh.alarmadespertador.core.components.NotificationHelper
import com.idh.alarmadespertador.core.components.ThemeEventBus
import com.idh.alarmadespertador.domain.models.ItemsOptionsConfig.*
import com.idh.alarmadespertador.navigation.AppNavigation
import com.idh.alarmadespertador.ui.theme.AlarmadespertadorTheme
import com.idh.alarmadespertador.viewmodels.MainViewModel
import com.idh.alarmadespertador.viewmodels.TopAppViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

//my weather API key dde270949a52c73fd2860b9a6d679bf9
//my weather APIname API_Weather_FP
//icon URL :  https://openweathermap.org/img/wn/10d@2x.png

//Uso de @AndroidEntryPoint: Esta anotación indica que MainActivity es un punto de entrada
// para la inyección de dependencias proporcionada por Hilt. Esto permite que MainActivity
// acceda a servicios o componentes inyectados en toda la aplicación.
// Dentro de onCreate, se inicializa sharedPreferences usando getSharedPreferences.
// Esto permite acceder a un archivo de preferencias privado llamado "MyAppPreferences".
// Se obtiene el valor de isDarkTheme de sharedPreferences, que determina si el tema oscuro
// está habilitado o no. Si no se encuentra este valor, se usa true como valor predeterminado.
//launchIn(this.lifecycleScope) asegura que los eventos se
// manejen dentro del alcance del ciclo de vida de la actividad
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.Theme_Alarmadespertador)

        super.onCreate(savedInstanceState)
        NotificationHelper.createNotificationChannel(this)

        sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val isDarkTheme = sharedPreferences.getBoolean("darkTheme", true)

        setContent {
            AlarmadespertadorTheme(useDarkTheme = isDarkTheme) {
                MyApp()
            }
        }

        // Usa el LifecycleOwner de la actividad directamente
        ThemeEventBus.themeEvent.onEach { newIsDarkTheme ->
            sharedPreferences.edit().putBoolean("darkTheme", newIsDarkTheme).apply()
            recreate()
        }.launchIn(this.lifecycleScope) // Usar el scope del LifecycleOwner de la actividad
    }
}

//AlarmadespertadorTheme
/* Función composable que configura la navegación de la aplicación utilizando NavHost.
   rememberNavController crea y recuerda un NavController que gestiona la navegación entre composables.
   NavHost define la navegación entre diferentes pantallas de la aplicación, con "splash_screen" como la pantalla de inicio. */

@Composable
fun MyApp() {
        /*El NavHost define la navegación entre diferentes composables.
            El startDestination está configurado como "splash_screen".*/
        val navController = rememberNavController()

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

/*  Es un composable que define la pantalla principal de la aplicación.
    Llama a Contenido, pasando un nuevo NavController. */

@Composable
fun MainScreen() {

    val navController = rememberNavController()
    Contenido(navController = navController)

}

/*  Composable que define la estructura básica de la interfaz de usuario utilizando Scaffold.
    Configura la barra superior (NavegacionTopBar) y la barra de navegación inferior (NavegacionInferior).
    Utiliza AppNavigation para gestionar el contenido principal de las pantallas, basándose en la navegación. */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Contenido (
    navController: NavHostController
) {
    val mainViewModel : MainViewModel = viewModel()
    Log.d("Contenido", "MainViewModel hashCode: ${mainViewModel.hashCode()}")
    val sheetState  = rememberModalBottomSheetState()
    Scaffold(
        topBar = {
                 NavegacionTopBar(navController = navController)
        },
        bottomBar = {
            NavegacionInferior(navController)
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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

/*  Composable que muestra una hoja modal en la parte inferior de la pantalla con opciones de configuración.
    Utiliza un Column para organizar los elementos verticalmente y un Row para los elementos de configuración.
    Cada elemento de configuración es clickeable y navega a la ruta correspondiente. */

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
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 38.dp)
    ) {
        Text(text = "Configuraciones",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        , modifier = Modifier.padding(bottom = 16.dp)
        )
        //Aquí opciones de configuración
        itemsConfiguration.forEach { item ->
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(48.dp)
                    .clickable {
                        scope
                            .launch {
                                sheetState.hide()
                            }
                            .invokeOnCompletion {
                                mainViewModel.showBottomSheet = false
                            }
                        navController.navigate(item.ruta)
                    }
                ) {
                Icon(item.icon, contentDescription = item.title)
                Spacer(modifier = Modifier.width(54.dp))
                Text(text = item.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

/*  Es un composable que actúa como pantalla de bienvenida (splash).
    Usa LaunchedEffect para ejecutar una acción después de un retraso (delay), en este caso, navegar a la pantalla principal.
    Muestra una imagen (reloj) mientras se encuentra en pantalla. */

@Composable
fun SplashScreen(navController: NavController) {

    LaunchedEffect(key1 = true) {
        delay(1000L)
        navController.navigate("main_screen") {
            popUpTo("splash_screen") { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
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



