package com.idh.alarmadespertador.data.repository

import android.util.Log
import com.idh.alarmadespertador.data.network.WeatherAPI
import com.idh.alarmadespertador.domain.models.clima.OpenWeatherMap
import com.idh.alarmadespertador.domain.repository.WeatherRepository
import com.idh.alarmadespertador.domain.util.Resource
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherAPI: WeatherAPI
) : WeatherRepository {

    //Clase que implementa la interfaz WeatherRepository. Se encarga de realizar las operaciones de red
    //para obtener datos del clima utilizando WeatherAPI.
    //La clase utiliza Retrofit para realizar llamadas a la API y procesa las respuestas.

    // Función para obtener el clima basado en la ubicación geográfica (latitud y longitud).
    override suspend fun getWeatherWithLocation(
        lat: Double,
        lon: Double
    ): Resource<OpenWeatherMap> {
        Log.d("WeatherRepositoryImpl", "Solicitando datos del clima para lat: $lat, lon: $lon")
        return try {
            // Realiza la petición a la API.
            val response = weatherAPI.getWeatherWithLocation(lat, lon)
            // Verifica si la respuesta es exitosa y contiene un cuerpo.
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Network error: ${e.message}")
        }
    }

    // Función para obtener el clima basado en el nombre de una ciudad.
    override suspend fun getWeatherWithCityName(name: String): Resource<OpenWeatherMap> {
        // Estructura similar a la función anterior.
        return try {
            val response = weatherAPI.getWeatherWithCityName(name)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Network error: ${e.message}")
        }
    }
}
