package com.idh.alarmadespertador.domain.repository

import android.util.Log
import com.idh.alarmadespertador.domain.models.clima.OpenWeatherMap
import com.idh.alarmadespertador.domain.util.Resource
import retrofit2.Response

interface WeatherRepository {

        suspend fun getWeatherWithLocation(lat: Double, lon: Double): Resource<OpenWeatherMap>

                // Resto de la implementación...
        suspend fun getWeatherWithCityName(name: String): Resource<OpenWeatherMap>
}