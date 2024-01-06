package com.idh.alarmadespertador.data.network

import com.idh.alarmadespertador.domain.models.clima.OpenWeatherMap
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// Define dos funciones para obtener información del tiempo utilizando la API de OpenWeatherMap.
// Utiliza Retrofit.
interface WeatherAPI {

    // Esta función obtiene el tiempo actual basado en la ubicación geográfica (latitud y longitud).
    // La anotación @GET define la URL relativa del endpoint para la petición GET.
    // Los parámetros "lat" y "lon" se añaden a la URL como parámetros de consulta (query).
    // La función es suspendida, lo que indica que se puede llamar dentro de una corrutina y es asincrónica.

    @GET("weather?&appid=dde270949a52c73fd2860b9a6d679bf9&units=metric&lang=es")
    suspend fun getWeatherWithLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): Response<OpenWeatherMap>

    // Esta función obtiene el tiempo actual basado en el nombre de una ciudad.
    // Similar a la función anterior, usa @GET para definir la URL relativa y es una función suspendida.
    // "name" se utiliza como parámetro de consulta para especificar el nombre de la ciudad.

    @GET("weather?&appid=dde270949a52c73fd2860b9a6d679bf9&units=metric&lang=es")
    suspend fun getWeatherWithCityName(@Query("q") name: String): Response<OpenWeatherMap>
}

//getWeatherWithLocation