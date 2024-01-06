package com.idh.alarmadespertador.domain.repository

import com.idh.alarmadespertador.domain.models.clima.OpenWeatherMap
import com.idh.alarmadespertador.domain.util.Resource

//define los métodos para obtener datos del clima:
interface WeatherRepository {

    //// Obtiene el clima para una ubicación específica dadas las coordenadas latitud y longitud.
    suspend fun getWeatherWithLocation(lat: Double, lon: Double): Resource<OpenWeatherMap>

    // Obtiene el clima para una ciudad específica por nombre.
    suspend fun getWeatherWithCityName(name: String): Resource<OpenWeatherMap>
}