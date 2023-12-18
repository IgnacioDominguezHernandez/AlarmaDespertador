package com.idh.alarmadespertador.screens.climascreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.idh.alarmadespertador.domain.models.clima.ClimaData
import com.idh.alarmadespertador.viewmodels.ClimaViewModel
@Composable
fun ClimaScreen(climaViewModel: ClimaViewModel = hiltViewModel()) {
    // Estado para gestionar el texto de la barra de búsqueda

    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        climaViewModel.loadWeatherInfo()
    }

    val state = climaViewModel.state

    when {
        state.isLoading -> {
            Text(text = "cargando")
            CircularProgressIndicator()
        }

            state.error != null -> {

                Text(
                    text = "error",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                )

            }

        state.climaData != null ->     Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF1E90FF)
        ) {

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(66.dp))
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
                    Spacer(modifier = Modifier.height(46.dp))
                    // Resto de tu UI
                    WeatherCard(weatherData = state.climaData)
                    // ... puedes agregar más contenido aquí si es necesario
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
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
        placeholder = { Text("Introduzca una ciudad") },
        shape = RoundedCornerShape(50.dp),
        colors = OutlinedTextFieldDefaults.colors(),
        trailingIcon = {
            IconButton(onClick = {
                climaViewModel.loadWeatherDataByCityName(searchText) // Llama a la función del ViewModel
                keyboardController?.hide() // Oculta el teclado
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
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
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun WeatherCard(weatherData: ClimaData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
     //   color = Color(0xFF1E90FF),
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
                Image(
                    painter = rememberImagePainter(weatherData.icono),
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(48.dp)  // Tamaño del icono
                )
                Text(
                    text = "${weatherData.temperatura}°C",
                    style = MaterialTheme.typography.displayMedium
                )
            }
            Text(weatherData.descripcion, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
            WeatherDetail("Amanecer", weatherData.amanecer)
            WeatherDetail("Atardecer", weatherData.atardecer)
            WeatherDetail("Sensación térmica", "${weatherData.seSiente}°C")
            WeatherDetail("Max. Temp.", "${weatherData.maxTemp}°C")
            WeatherDetail("Min. Temp.", "${weatherData.minTemp}°C")
            WeatherDetail("Precipitaciones", weatherData.humedad)
            WeatherDetail("Presión", weatherData.presion)
            // Asumiendo que tienes un campo para el viento en ClimaData
            // WeatherDetail("Viento", "${weatherData.viento} m/s")
        }
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
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(value, style = MaterialTheme.typography.bodyLarge)
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
        seSiente = 26.0
    )

    WeatherCard(weatherData = mockWeatherData)
}
