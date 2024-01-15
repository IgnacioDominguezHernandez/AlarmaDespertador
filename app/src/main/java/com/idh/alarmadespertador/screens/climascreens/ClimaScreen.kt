package com.idh.alarmadespertador.screens.climascreens

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.idh.alarmadespertador.domain.models.clima.ClimaData
import com.idh.alarmadespertador.viewmodels.ClimaViewModel
import kotlin.math.roundToInt


@Composable
fun ClimaScreen(climaViewModel: ClimaViewModel = hiltViewModel()) {

    //Vista de Clima
    //Se comprueban los permisos del device 

  //  WeatherColorsTheme() {

        // Estado para gestionar el texto de la barra de búsqueda
        var searchText by remember { mutableStateOf("") }
        var mostrarMensajeErrorPermiso by remember { mutableStateOf(false) }

        val locationPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                ) {
                    // Permiso concedido, puedes cargar la información del clima
                    mostrarMensajeErrorPermiso = false
                    climaViewModel.loadWeatherInfo()
                } else {
                    // Manejar el caso de permiso no concedido
                    mostrarMensajeErrorPermiso = true
                }
            }
        )

        if (mostrarMensajeErrorPermiso) {
            Text(
                text = "Se requieren permisos de ubicación para mostrar la información del clima.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
            // Aquí se puede agregar un botón para solicitar nuevamente los permisos.
        }

        LaunchedEffect(Unit) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

        val state = climaViewModel.state
        // Dependiendo del momento se muestra una cosa u otra al usuario
        when {
            state.isLoading -> {
                Box(
                    contentAlignment = Alignment.Center, // Centra el contenido dentro del Box
                    modifier = Modifier.fillMaxSize()    // El Box ocupa todo el espacio disponible
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        text = "Cargando...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 120.dp)
                    )
                }
            }

            state.error != null -> {

                Box(
                    contentAlignment = Alignment.Center, // Centra el contenido dentro del Box
                    modifier = Modifier.fillMaxSize()    // El Box ocupa todo el espacio disponible
                ) {
                    Text(
                        text = "Error al cargar los datos",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium, // Usa una tipografía de Material 3
                        textAlign = TextAlign.Center, // Asegura que el texto esté centrado dentro del Text
                        modifier = Modifier.padding(16.dp) // Añade un poco de padding para evitar que el texto toque los bordes
                    )
                }

            }

            state.climaData != null -> Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Spacer(modifier = Modifier.height(46.dp))
                        // Divider()
                        // Llamada al composable SearchBar
                        SearchBar(
                            searchText = searchText,
                            onSearchTextChanged = { newText -> searchText = newText },
                            climaViewModel = climaViewModel
                        )
                    }
                    item {
                        // Espacio entre la barra de búsqueda y la tarjeta del clima
                        Spacer(modifier = Modifier.height(40.dp))
                        WeatherCard(weatherData = state.climaData)
                    }
                }
            }
        }
//    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    climaViewModel: ClimaViewModel // Agrega el ViewModel aquí
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChanged,
        singleLine = true,
        placeholder = {
            Text(
                "Introduzca una ciudad",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        shape = RoundedCornerShape(40.dp),
        colors = OutlinedTextFieldDefaults.colors(
            MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary
        ),
        trailingIcon = {
            IconButton(onClick = {
                climaViewModel.loadWeatherDataByCityName(searchText) // Llama a la función del ViewModel
                keyboardController?.hide() // Oculta el teclado
            }) {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search")
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = androidx.compose.ui.text.input.ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                climaViewModel.loadWeatherDataByCityName(searchText) // Llama a la función del ViewModel
                keyboardController?.hide() // Oculta el teclado
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    )
}

@Composable
fun WeatherCard(weatherData: ClimaData) {

    // Imprimir en log el código de condición original
    Log.d("WeatherCard", "ID recibido: ${weatherData.id}, Código de condición: ${weatherData.icono}")

    // Usa directamente el código de condición de weatherData para la URL del ícono
    val iconUrl = "https://openweathermap.org/img/wn/${weatherData.icono}@2x.png"
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(weatherData.ciudad, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = iconUrl, // URL completa del icono
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(100.dp) // Tamaño del icono
                )
                Text(
                    text = "${weatherData.temperatura.roundToInt()}°C",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                weatherData.descripcion,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Divider()
            Spacer(modifier = Modifier.height(12.dp))
            // Detalles del clima
            weatherDetails(weatherData)
        }
    }
}


@Composable
fun weatherDetails(weatherData: ClimaData) {
    val details = listOf(
        "id" to weatherData.id.toString(),
        "Amanecer" to weatherData.amanecer,
        "Atardecer" to weatherData.atardecer,
        "Sensación térmica" to "${weatherData.seSiente.roundToInt()}°C",
        "Max. Temp." to "${weatherData.maxTemp.roundToInt()}°C",
        "Min. Temp." to "${weatherData.minTemp.roundToInt()}°C",
        "Precipitaciones" to weatherData.humedad,
        "Presión" to weatherData.presion
    )
    details.forEach { (label, value) ->
        WeatherDetail(label, value)
    }
}

@Composable
fun WeatherDetail(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ClimaScreenPreview() {
    // Datos simulados para el preview
    val mockWeatherData = ClimaData(
        ciudad = "Ciudad de Ejemplo",
        icono = "https://openweathermap.org/img/wn/10d@2x.png",
        temperatura = 25.0,
        descripcion = "Cielo claro",
        humedad = "65%",
        presion = "1013 hPa",
        maxTemp = 28.0,
        minTemp = 22.0,
        amanecer = "06:00",
        atardecer = "20:00",
        seSiente = 26.0,
        id = 500
    )

    WeatherCard(weatherData = mockWeatherData)
}
