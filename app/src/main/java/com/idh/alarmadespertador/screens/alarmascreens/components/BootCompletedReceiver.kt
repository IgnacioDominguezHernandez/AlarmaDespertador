package com.idh.alarmadespertador.screens.alarmascreens.components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        Log.d("AlarmaReceiver", "Reiniciando y reprogramando alarmas después de BOOT_COMPLETED")

        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val serviceIntent = Intent(context, ReprogramarAlarmasService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
                }
            }
        }
    }

    private fun reprogramarAlarmasActivas(context: Context) {
        // Obtén las alarmas de tu base de datos o SharedPreferences
        // y utiliza AlarmManager para reprogramarlas
    }

