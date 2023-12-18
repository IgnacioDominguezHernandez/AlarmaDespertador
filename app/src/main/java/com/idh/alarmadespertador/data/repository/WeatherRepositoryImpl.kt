package com.idh.alarmadespertador.data.repository

import android.util.Log
import com.idh.alarmadespertador.data.network.WeatherAPI
import com.idh.alarmadespertador.domain.models.clima.OpenWeatherMap
import com.idh.alarmadespertador.domain.repository.WeatherRepository
import com.idh.alarmadespertador.domain.util.Resource
import retrofit2.Response
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherAPI: WeatherAPI
) : WeatherRepository {

    override suspend fun getWeatherWithLocation(lat: Double, lon: Double): Resource<OpenWeatherMap> {
        Log.d("WeatherRepositoryImpl", "Solicitando datos del clima para lat: $lat, lon: $lon")
        return try {
            val response = weatherAPI.getWeatherWithLocation(lat, lon)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Network error: ${e.message}")
        }
    }

    override suspend fun getWeatherWithCityName(name: String): Resource<OpenWeatherMap> {
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
