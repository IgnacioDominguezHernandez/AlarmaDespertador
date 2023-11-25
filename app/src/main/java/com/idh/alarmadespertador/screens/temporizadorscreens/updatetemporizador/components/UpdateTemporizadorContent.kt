package com.idh.alarmadespertador.screens.temporizadorscreens.updatetemporizador.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.idh.alarmadespertador.core.constants.Constantes.Companion.HORAS
import com.idh.alarmadespertador.core.constants.Constantes.Companion.MINUTOS
import com.idh.alarmadespertador.core.constants.Constantes.Companion.SEGUNDOS
import com.idh.alarmadespertador.core.constants.Constantes.Companion.NOMBRE_TEMPORIZADOR
import com.idh.alarmadespertador.core.constants.Constantes.Companion.UPDATE
import com.idh.alarmadespertador.domain.models.Temporizador

@Composable
fun UpdateTemporizadorContent(
    padding: PaddingValues,
    temporizador: Temporizador,
    updateNombreTemporizador: (nombreTemporizador: String) -> Unit,
    updateHora: (horas: String) -> Unit,
    updateMinutos: (minutos: String) -> Unit,
    updateSegundos: (segundos: String) -> Unit,
    updateTemporizador: (temporizador : Temporizador) -> Unit,
    navigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        TextField(
            value = temporizador.nombreTemporizador,
            onValueChange = {nombreTemporizador->
                updateNombreTemporizador(nombreTemporizador)
            },
            placeholder = {
                Text(NOMBRE_TEMPORIZADOR)
            }
        )
        Spacer(modifier = Modifier
            .height(8.dp))
        TextField(
            value = temporizador.horas,
            onValueChange = {horas->
                updateHora(horas)
            },
            placeholder = {
                Text(HORAS)
            }
        )
        Spacer(modifier = Modifier
            .height(8.dp))
        TextField(
            value = temporizador.minutos,
            onValueChange = {minutos->
                updateMinutos(minutos)
            },
            placeholder = {
                Text(MINUTOS)
            }
        )
        Spacer(modifier = Modifier
            .height(8.dp))
        TextField(
            value = temporizador.segundos,
            onValueChange = {segundos->
                updateSegundos(segundos)
            },
            placeholder = {
                Text(SEGUNDOS)
            }
        )
        Button(
            onClick = {
                updateTemporizador(temporizador)
                navigateBack()
            }
        ){
            Text(UPDATE)
        }
    }
}