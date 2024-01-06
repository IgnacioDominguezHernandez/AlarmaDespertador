package com.idh.alarmadespertador.screens.climascreens.components

import com.idh.alarmadespertador.domain.models.clima.ClimaData
import com.idh.alarmadespertador.domain.models.clima.OpenWeatherMap

//modelo de datos utilizado para manejar el estado de la obtención de la información meteorológica
//objeto que contiene información obtenida de la API de OpenWeatherMap
data class WeatherState(

    val weatherInfo: OpenWeatherMap? = null,
    val climaData: ClimaData? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)