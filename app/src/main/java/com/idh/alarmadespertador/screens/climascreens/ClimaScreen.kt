package com.idh.alarmadespertador.screens.climascreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.outlined.Addchart
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.idh.alarmadespertador.R
import com.idh.alarmadespertador.models.ItemsBottomNav.Itembottomnav1.icon


//deberá recibir los datos del clima climaData: ClimaData
@Composable
fun ClimaScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.relojtres),
            contentDescription = "Icono del Clima",
            modifier = Modifier.size(150.dp)
        )
        Text(text = "Temperatura: 24°C")
        Text(text = "Condición: Soleado")
        // Más información del clima...
    }
}

data class ClimaData(val iconoClima: Int, val temperatura: Int, val condicion: String)
