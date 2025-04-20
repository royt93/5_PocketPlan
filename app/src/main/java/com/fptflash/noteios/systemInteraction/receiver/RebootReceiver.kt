package com.fptflash.noteios.systemInteraction.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.fptflash.noteios.systemInteraction.handler.noti.AlarmHandler
import com.jakewharton.threetenabp.AndroidThreeTen
import com.fptflash.noteios.data.settings.SettingId
import com.fptflash.noteios.data.settings.SettingsManager
import com.fptflash.noteios.data.sleepreminder.SleepReminder
import com.fptflash.noteios.systemInteraction.handler.storage.StorageHandler

class RebootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        AndroidThreeTen.init(context)
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            StorageHandler.path = context.filesDir.absolutePath
            SettingsManager.init()
            AlarmHandler.run {
                val time = SettingsManager.getSetting(SettingId.BIRTHDAY_NOTIFICATION_TIME) as String
                setBirthdayAlarms(time, context = context)
            }
            SleepReminder(context).updateReminder()
        }
    }
}
