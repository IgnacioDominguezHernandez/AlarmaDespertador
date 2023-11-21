package com.idh.alarmadespertador.screens.radioscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.idh.alarmadespertador.R

@Composable
fun RadioScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cualquier icono representativo de radio
        Image(
            painter = painterResource(id = R.drawable.radiominimalista),
            contentDescription = "Icono de Radio",
            modifier = Modifier.size(250.dp)
        )
        Text(text = "Estación de Radio XYZ")
        // Aquí agregamos botones de control de la radio
    }
}