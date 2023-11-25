package com.idh.alarmadespertador.screens.temporizadorscreens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.idh.alarmadespertador.core.constants.Constantes.Companion.ADD
import com.idh.alarmadespertador.core.constants.Constantes.Companion.ADD_TEMPORIZADOR
import com.idh.alarmadespertador.core.constants.Constantes.Companion.DISMISS
import com.idh.alarmadespertador.core.constants.Constantes.Companion.NO_VALUE
import com.idh.alarmadespertador.core.constants.Constantes.Companion.HORAS
import com.idh.alarmadespertador.core.constants.Constantes.Companion.MINUTOS
import com.idh.alarmadespertador.domain.models.Temporizador
import kotlinx.coroutines.job

@Composable
fun AddTemporizadorAlertDialog (
    openDialog: Boolean,
    closeDialog : () -> Unit,
    addTemporizador : (temporizador : Temporizador) -> Unit
) {
    if (openDialog) {
        var nombretemporizador by remember { mutableStateOf (NO_VALUE) }
        var horas by remember { mutableStateOf (NO_VALUE) }
        var minutos by remember { mutableStateOf (NO_VALUE) }
        var segundos by remember { mutableStateOf (NO_VALUE) }
        val focusRequester = FocusRequester()

        AlertDialog(
            onDismissRequest = { closeDialog },
            title = {
                Text(ADD_TEMPORIZADOR)
            },
            text = {
                Column() {
                    TextField(
                        value = horas,
                        onValueChange = {horas = it},
                        placeholder = {
                            Text(HORAS)
                        },
                        modifier = Modifier.focusRequester(focusRequester)
                    )
                    LaunchedEffect(Unit){
                        coroutineContext.job.invokeOnCompletion {
                            focusRequester.requestFocus()
                        }
                    }
                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )
                    TextField(
                        value = minutos,
                        onValueChange = {minutos = it},
                        placeholder = {
                            Text(MINUTOS)
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        closeDialog()
                        val temporizador = Temporizador (0 , nombretemporizador ,horas, minutos, segundos, estado = true)
                        addTemporizador(temporizador)
                    }) {
                    Text(ADD)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = closeDialog) {
                    Text(DISMISS)
                }
            }

        )
    }
}