package com.idh.alarmadespertador.data.network

import com.idh.alarmadespertador.domain.models.clima.OpenWeatherMap
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("weather?&appid=dde270949a52c73fd2860b9a6d679bf9&units=metric&lang=es")
    suspend fun getWeatherWithLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): Response<OpenWeatherMap>

    @GET("weather?&appid=dde270949a52c73fd2860b9a6d679bf9&units=metric&lang=es")
    suspend fun getWeatherWithCityName(@Query("q") name: String): Response<OpenWeatherMap>
}

//getWeatherWithLocation