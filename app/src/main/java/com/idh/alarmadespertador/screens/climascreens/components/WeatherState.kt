package com.idh.alarmadespertador.screens.climascreens.components

import com.idh.alarmadespertador.domain.models.clima.ClimaData
import com.idh.alarmadespertador.domain.models.clima.OpenWeatherMap

data class WeatherState(
    val weatherInfo: OpenWeatherMap? = null,
    val climaData: ClimaData? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)