package com.fptflash.noteios.systemInteraction.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jakewharton.threetenabp.AndroidThreeTen
import com.fptflash.noteios.R
import com.fptflash.noteios.data.birthdaylist.Birthday
import com.fptflash.noteios.data.birthdaylist.BirthdayList
import com.fptflash.noteios.data.settings.SettingId
import com.fptflash.noteios.data.settings.SettingsManager
import com.fptflash.noteios.data.sleepreminder.SleepReminder
import com.fptflash.noteios.systemInteraction.handler.noti.AlarmHandler
import com.fptflash.noteios.systemInteraction.handler.noti.NotificationHandler
import com.fptflash.noteios.systemInteraction.handler.storage.StorageHandler
import org.threeten.bp.LocalDate

class NotificationReceiver : BroadcastReceiver() {
    private lateinit var context: Context
    private lateinit var localDate: LocalDate

    override fun onReceive(context: Context, intent: Intent) {
        this.context = context
        AndroidThreeTen.init(this.context)
        StorageHandler.path = context.filesDir.absolutePath
        this.localDate = LocalDate.now()
        when (intent.extras?.get("Notification")) {
            "Birthday" -> birthdayNotifications()
            "SReminder" -> checkSleepNotification(intent)
        }
        SettingsManager.init()
        val time = SettingsManager.getSetting(SettingId.BIRTHDAY_NOTIFICATION_TIME) as String
        AlarmHandler.setBirthdayAlarms(time, context = context)
    }

    private fun checkSleepNotification(intent: Intent) {
        SleepReminder(context).reminder[intent.extras?.get("weekday")]?.updateAlarm(
            intent.extras?.getInt("requestCode")!!
        )
        sRNotification()
    }

    private fun sRNotification() {
        NotificationHandler.createNotification(
            channelId = "Sleep Reminder",
            name = context.resources.getString(R.string.menuTitleSleep),
            requestCode = 200,
            contentTitle = context.resources.getString(R.string.sleepNotificationTitle),
            contentText = context.resources.getString(R.string.sleepNotificationText),
            icon = R.drawable.ic_action_sleepreminder,
            intentValue = "SReminder",
            myContext = context,
            timeOutMs = 3 * 60 * 60 * 1000
        )
    }

    private fun birthdayNotifications() {
        val birthdayList = BirthdayList(context.resources.getStringArray(R.array.months))
        if (birthdayList.isEmpty()) {
            return
        }
        val notifiableUpcomingBirthdays = getUpcomingBirthdays(birthdayList)
        val notifiableCurrentBirthdays = getCurrentBirthdays(birthdayList)
        if (notifiableCurrentBirthdays.size > 1) {
            notifyCurrentBirthdays(notifiableCurrentBirthdays.size)
        } else if (notifiableCurrentBirthdays.size == 1) {
            notifyBirthdayNow(notifiableCurrentBirthdays[0])
        }
        if (notifiableUpcomingBirthdays.size > 1) {
            notifyUpcomingBirthdays(notifiableUpcomingBirthdays.size)
        } else if (notifiableUpcomingBirthdays.size == 1) {
            notifyUpcomingBirthday(notifiableUpcomingBirthdays[0])
        }
    }

    private fun getUpcomingBirthdays(birthdayList: BirthdayList): ArrayList<Birthday> {
        val upcomingBirthdays = ArrayList<Birthday>()
        birthdayList.forEach { n ->
            val calculatedDate = LocalDate.now().plusDays(n.daysToRemind.toLong())
            if (n.notify && calculatedDate.monthValue == n.month &&
                calculatedDate.dayOfMonth == n.day && n.daysToRemind > 0
            ) {
                upcomingBirthdays.add(n)
            }
        }
        return upcomingBirthdays
    }

    private fun getCurrentBirthdays(birthdayList: BirthdayList): ArrayList<Birthday> {
        val currentBirthdays = ArrayList<Birthday>()
        birthdayList.forEach { n ->
            if (n.notify && n.month == localDate.monthValue &&
                n.day == localDate.dayOfMonth
            ) {
                currentBirthdays.add(n)
            }
        }
        return currentBirthdays
    }

    private fun notifyBirthdayNow(birthday: Birthday) {
        NotificationHandler.createNotification(
            channelId = "Birthday Notification",
            name = context.resources.getString(R.string.menuTitleBirthdays),
            requestCode = 100,
            contentTitle = context.resources.getString(R.string.birthdayNotificationTitle),
            contentText = context.resources.getString(R.string.birthdayNotificationSingleText, birthday.name),
            icon = R.drawable.ic_action_birthday,
            intentValue = "birthdays",
            myContext = context
        )
    }

    private fun notifyCurrentBirthdays(currentBirthdays: Int) {
        NotificationHandler.createNotification(
            channelId = "Birthday Notification",
            name = context.resources.getString(R.string.menuTitleBirthdays),
            requestCode = 102,
            contentTitle = context.resources.getString(R.string.birthdayNotificationTitle),
            contentText = context.resources.getString(R.string.birthdayNotificationMultText, currentBirthdays),
            icon = R.drawable.ic_action_birthday,
            intentValue = "birthdays",
            myContext = context
        )
    }

    private fun notifyUpcomingBirthday(birthday: Birthday) {
        NotificationHandler.createNotification(
            channelId = "Birthday Notification",
            name = context.resources.getString(R.string.birthdayNotificationTitleUpc),
            requestCode = 101,
            contentTitle = context.resources.getString(R.string.birthdayNotificationTitleUpc),
            contentText = context.resources.getString(
                R.string.birthdayNotificationSingleUpcText,
                birthday.name,
                birthday.daysToRemind,
                context.resources.getQuantityString(R.plurals.dayIn, birthday.daysToRemind)
            ),
            icon = R.drawable.ic_action_birthday,
            intentValue = "birthdays",
            myContext = context
        )
    }

    private fun notifyUpcomingBirthdays(upcomingBirthdays: Int) {
        NotificationHandler.createNotification(
            channelId = "Birthday Notification",
            name = context.resources.getString(R.string.birthdayNotificationTitleUpc),
            requestCode = 103,
            contentTitle = context.resources.getString(R.string.birthdayNotificationTitleUpc),
            contentText = context.resources.getString(R.string.birthdayNotificationMultUpcText, upcomingBirthdays),
            icon = R.drawable.ic_action_birthday,
            intentValue = "birthdays",
            myContext = context
        )
    }
}
