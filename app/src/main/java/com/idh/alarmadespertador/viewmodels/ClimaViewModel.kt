package com.idh.alarmadespertador.viewmodels

import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idh.alarmadespertador.data.location.LocationListener
import com.idh.alarmadespertador.data.network.WeatherAPI
import com.idh.alarmadespertador.domain.location.LocationTracker
import com.idh.alarmadespertador.domain.models.clima.ClimaData
import com.idh.alarmadespertador.domain.models.clima.OpenWeatherMap
import com.idh.alarmadespertador.domain.repository.WeatherRepository
import com.idh.alarmadespertador.domain.util.Resource
import com.idh.alarmadespertador.screens.climascreens.components.WeatherState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
@HiltViewModel
class ClimaViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker
) : ViewModel() {

    var state by mutableStateOf(WeatherState())
        private set

    fun loadWeatherInfo() {
        viewModelScope.launch {
            Log.d("ClimaViewModel", "Iniciando la carga de datos del clima...")
            state = state.copy(isLoading = true, error = null)
            locationTracker.getCurrentLocation()?.let { location ->
                Log.d("ClimaViewModel", "Ubicación obtenida: Lat ${location.latitude}, Lon ${location.longitude}")
                when (val result = weatherRepository.getWeatherWithLocation(location.latitude, location.longitude)) {
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
                        Log.e("ClimaViewModel", "Error al cargar datos del clima: ${result.message}")
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
            seSiente = weatherData.main.feelsLike
        )
    }

    private fun formatTime(timeInSeconds: Int): String {
        val instant = Instant.ofEpochSecond(timeInSeconds.toLong())
        Log.d("Debug", "Instant: $instant")
        val formatter = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    }

    fun getIconUrl(iconCode: String): String {
        val baseUrl = "https://openweathermap.org/img/wn/"
        return "$baseUrl$iconCode@2x.png"
    }

}

