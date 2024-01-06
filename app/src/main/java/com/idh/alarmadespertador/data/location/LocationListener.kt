package com.idh.alarmadespertador.data.location

import android.location.Location
import android.os.Bundle
import android.util.Log


object LocationListener : android.location.LocationListener {

    var lat: Double = 0.0
    var lon: Double = 0.0
    var cityName: String = ""

    override fun onLocationChanged(location: Location) {
        lat = location.latitude
        lon = location.longitude
        Log.d("LocationUpdate", "Latitud: ${location.latitude}, Longitud: ${location.longitude}")
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }

}

