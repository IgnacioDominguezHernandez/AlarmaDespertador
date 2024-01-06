package com.idh.alarmadespertador.domain.location

import android.location.Location

interface LocationTracker {

    // Método suspendido, lo que significa que puede realizar operaciones de
    // larga duración (como acceder a la ubicación del GPS) sin bloquear el hilo principal.
    // Retorna un objeto `Location?`, indicando que puede devolver un objeto `Location` o null si no se puede obtener la ubicación.
    suspend fun getCurrentLocation(): Location?
}