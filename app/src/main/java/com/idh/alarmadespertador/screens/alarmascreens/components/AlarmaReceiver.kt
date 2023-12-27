package com.idh.alarmadespertador.screens.alarmascreens.components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager

class AlarmaReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        // Aquí el sonido de la alarma
        val alarmToneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone = RingtoneManager.getRingtone(context, alarmToneUri)
        ringtone.play()
    }
}