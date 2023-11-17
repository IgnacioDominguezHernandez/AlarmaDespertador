package com.idh.alarmadespertador.screens.alarmascreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.idh.alarmadespertador.R



    //Aqui tengo que poner el Botón Flotante y un LazyColumn
    //con las alarmas que el usuario vaya poniendo

    //Esta clase define la interfaz de usuario para la pantalla de alarma de la aplicación
    //Composable, lo que significa que define una parte de la UI de la aplicación.

@Composable
fun AlarmaScreen() {
    // Layout principal
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* acción al presionar */ }) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Alarma")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Lista de alarmas
            //LazyColumn, similar a un RecyclerView en Android clásico.
            LazyColumn {
                items<Any>(/* lista de alarmas */) { alarma ->
                    // Elemento de la lista para cada alarma
                }
            }
        }
    }
}

fun <LazyItemScope> items(itemContent: LazyItemScope.(index: Int) -> Unit) {

}
