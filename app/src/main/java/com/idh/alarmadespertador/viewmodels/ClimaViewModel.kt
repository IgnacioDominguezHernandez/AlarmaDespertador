package com.idh.alarmadespertador.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idh.alarmadespertador.domain.location.LocationTracker
import com.idh.alarmadespertador.domain.models.clima.ClimaData
import com.idh.alarmadespertador.domain.models.clima.OpenWeatherMap
import com.idh.alarmadespertador.domain.repository.WeatherRepository
import com.idh.alarmadespertador.domain.util.Resource
import com.idh.alarmadespertador.screens.climascreens.components.WeatherState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

//ViewModel del clima. Utiliza Hilt para inyectar dependencias del WeatherRepository y LocationTracker.
// Estas dependencias se utilizan para obtener datos meteorológicos y rastrear la ubicación actual del dispositivo.
@HiltViewModel
class ClimaViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker
) : ViewModel() {

    var state by mutableStateOf(WeatherState())
        private set

    // Esta función inicia el proceso de obtener la información del clima. Se ejecuta en el
    // viewModelScope para asegurar que las operaciones se cancelen si el ViewModel se desecha.
    //Utiliza LocationTracker para obtener la ubicación actual del dispositivo. Si la ubicación está
    // disponible, procede a solicitar datos del clima
    //Llama a weatherRepository.getWeatherWithLocation pasando la latitud y longitud de la ubicación actual.
    // Se utiliza un when para manejar los casos de éxito y error en la respuesta
    fun loadWeatherInfo() {
        viewModelScope.launch {
            Log.d("ClimaViewModel", "Iniciando la carga de datos del clima...")
            state = state.copy(isLoading = true, error = null)
            locationTracker.getCurrentLocation()?.let { location ->
                Log.d(
                    "ClimaViewModel",
                    "Ubicación obtenida: Lat ${location.latitude}, Lon ${location.longitude}"
                )
                when (val result = weatherRepository.getWeatherWithLocation(
                    location.latitude,
                    location.longitude
                )) {
                    is Resource.Success -> {

                        val ciudad = result.data?.sys?.country
                        val descripcionClima = result.data?.weather?.firstOrNull()?.description

                        val amanecer = result.data?.sys?.sunrise

                        // Imprime los datos en el log para verificar
                        Log.d("ClimaViewModel", "Ciudad recibida: $ciudad")
                        Log.d("ClimaViewModel", "amanecer: $amanecer")

                        Log.d("ClimaViewModel", "Datos del clima recibidos: $result")

                        val climaData = result.data?.let { transformToClimaData(it) }

                        state = state.copy(
                            weatherInfo = result.data,
                            climaData = climaData,
                            isLoading = false,
                            error = null
                        )
                    }

                    is Resource.Error -> {
                        Log.e(
                            "ClimaViewModel",
                            "Error al cargar datos del clima: ${result.message}"
                        )
                        state = state.copy(
                            weatherInfo = null,
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            } ?: run {
                Log.e("ClimaViewModel", "No se pudo obtener la ubicación.")
                state = state.copy(
                    isLoading = false,
                    error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                )
            }
        }
    }

    //La función loadWeatherDataByCityName en la clase ClimaViewModel se encarga de cargar y
    //gestionar los datos del clima para una ciudad específica, basándose en su nombre
    //Inicio de la Carga de Datos: Al invocar esta función, se inicia una operación asíncrona dentro del viewModelScope.
    // Se registra un mensaje indicando que se está cargando el clima para la ciudad especificada.
    //WeatherRepository.getWeatherWithCityName, pasando el nombre de la ciudad, para solicitar los
    // datos del clima. La función when maneja los dos posibles resultados de esta solicitud:
    // éxito (Resource.Success) o error (Resource.Error).
    fun loadWeatherDataByCityName(cityName: String) {
        viewModelScope.launch {
            Log.d("ClimaViewModel", "Cargando datos del clima para la ciudad: $cityName")
            state = state.copy(isLoading = true, error = null)
            when (val result = weatherRepository.getWeatherWithCityName(cityName)) {
                is Resource.Success -> {
                    val climaData = result.data?.let { transformToClimaData(it) }
                    state = state.copy(
                        weatherInfo = result.data,
                        climaData = climaData,
                        isLoading = false,
                        error = null
                    )
                }

                is Resource.Error -> {
                    Log.e("ClimaViewModel", "Error al cargar datos del clima: ${result.message}")
                    state = state.copy(
                        weatherInfo = null,
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    //La función transformToClimaData en la clase ClimaViewModel es responsable de transformar los datos del
    // clima obtenidos de la API OpenWeatherMap en un objeto ClimaData. Es más fácil de manejar en la interfaz
    // de usuario de la aplicación.
    //Recibe un objeto weatherData del tipo OpenWeatherMap, que contiene los datos del
    // clima en bruto como resultado de una consulta a la API.
    //Crea y devuelve un objeto ClimaData, que es una representación más amigable y adaptada de los datos del clima
    // Devuelve un objeto ClimaData con todos los datos del clima convenientemente
    // formateados y listos para ser utilizados en la UI.
    private fun transformToClimaData(weatherData: OpenWeatherMap): ClimaData {
        return ClimaData(
            ciudad = weatherData.name,
            icono = weatherData.weather.firstOrNull()?.icon.orEmpty(),
            temperatura = weatherData.main.temp,
            descripcion = weatherData.weather.firstOrNull()?.description.orEmpty(),
            humedad = "${weatherData.main.humidity} %",
            presion = "${weatherData.main.pressure} hPa",
            maxTemp = weatherData.main.tempMax,
            minTemp = weatherData.main.tempMin,
            amanecer = formatTime(weatherData.sys.sunrise),
            atardecer = formatTime(weatherData.sys.sunset),
            seSiente = weatherData.main.feelsLike,
            id = weatherData.id
        )
    }

    // a función formatTime es utilizada para convertir un tiempo expresado en segundos desde la época Unix
    // (1 de enero de 1970 a las 00:00:00 UTC) en una cadena de texto que representa la hora en formato HH:mm (horas y minutos).
    // Utiliza Instant.ofEpochSecond para convertir timeInSeconds a un objeto Instant.
    // Este objeto representa un punto específico en el tiempo.
    private fun formatTime(timeInSeconds: Int): String {
        val instant = Instant.ofEpochSecond(timeInSeconds.toLong())
        Log.d("Debug", "Instant: $instant")
        val formatter = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    }

    //La función getIconUrl se utiliza para generar una URL completa para obtener el ícono del clima de OpenWeatherMap
    fun getIconUrl(iconCode: String): String {
        val baseUrl = "https://openweathermap.org/img/wn/"
        return "$baseUrl$iconCode@2x.png"
    }

}

