package com.idh.alarmadespertador.core.components

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

//selector de tonos de llamada para Android, implementado en Jetpack Compose
@Composable
fun RingtonePickerDropdown(
    selectedRingtone: String,
    onRingtoneSelected: (String, String) -> Unit
) {
    //RingtoneManager para obtener una lista de tonos de llamada disponibles, que se almacenan en ringtones
    var currentRingtone: Ringtone? by remember { mutableStateOf(null) }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val ringtones: List<Pair<String, String>> = remember {
        val ringtoneManager = RingtoneManager(context).apply {
            setType(RingtoneManager.TYPE_ALARM)
        }
        val cursor = ringtoneManager.cursor
        val ringtonesList = mutableListOf<Pair<String, String>>()
        if (cursor.moveToFirst()) {
            do {
                val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
                val ringtoneUri = ringtoneManager.getRingtoneUri(cursor.position)
                ringtonesList.add(title to ringtoneUri.toString())
            } while (cursor.moveToNext())
        }
        ringtonesList
    }
    //DisposableEffect para detener el tono de llamada cuando el componente se descarta
    DisposableEffect(Unit) {
        onDispose {
            currentRingtone?.stop()
        }
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Text(
                if (selectedRingtone.isNotEmpty()) selectedRingtone else "tono de llamada",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            ringtones.forEach { (title, uri) ->
                DropdownMenuItem(
                    text = {
                        Text(
                            title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        playRingtoneSample(context, uri, currentRingtone) { newRingtone ->
                            currentRingtone = newRingtone
                        }
                        onRingtoneSelected(title, uri)
                        expanded = false
                    }
                )
            }
        }
    }
}


fun playRingtoneSample(
    context: Context,
    ringtoneUri: String,
    currentRingtone: Ringtone?,
    onUpdate: (Ringtone) -> Unit
) {
    currentRingtone?.stop()

    val uri = Uri.parse(ringtoneUri)
    val newRingtone = RingtoneManager.getRingtone(context, uri)
    newRingtone.play()
    onUpdate(newRingtone)
}

/*
fun playRingtoneSample(context: Context, ringtoneUri: String, currentRingtoneState: MutableState<Ringtone?>) {

    // Detener el tono actual si está sonando
    currentRingtoneState.value?.stop()

    val uri = Uri.parse(ringtoneUri)
    val ringtone = RingtoneManager.getRingtone(context, uri)
    currentRingtoneState.value = ringtone
    ringtone.play()
}
 */
