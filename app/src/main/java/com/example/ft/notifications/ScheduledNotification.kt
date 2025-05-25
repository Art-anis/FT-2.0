package com.example.ft.notifications

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.ft.App
import com.example.ft.R
import com.example.ft.update_tracked_data.UpdateTrackedFlightService
import java.util.Calendar
import java.util.Date

//receiver для уведомлений
class ScheduledNotification: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            context?.let {
                //получаем данные из intent
                val flightNumber = intent.getStringExtra("flightNumber")
                val id = intent.getIntExtra("notificationId", 0)
                val airlineIata = intent.getStringExtra("airlineIata")
                val airportIata = intent.getStringExtra("airportIata")

                //получаем название и описание уведомления в зависимости от id
                val (title, description) = if (id % 10 == 7) {
                    //ставим новое напоминание за сутки до рейса
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = Date().time
                    calendar.add(Calendar.DAY_OF_MONTH, 6)
                    scheduleReminder(
                        calendar = calendar,
                        flightNumber = flightNumber ?: "",
                        id = "${id / 10}1".toInt(),
                        airlineIata = airlineIata ?: "",
                        airportIata = airportIata ?: ""
                    )
                    Pair("Your flight is in a week!", "Flight $flightNumber departs in a week!")
                } else {
                    //запускаем сервис сейчас
                    scheduleService(
                        flightNumber ?: "",
                        airlineIata = airlineIata ?: "",
                        airportIata = airportIata ?: ""
                    )
                    Pair("Your flight is tomorrow!", "Flight $flightNumber leaves tomorrow!")
                }
                //собираем уведомление и запускаем его
                val notification = NotificationCompat.Builder(context, "0")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(description)
                    .setContentTitle(title)
                    .build()
                val manager = App.getInstance()
                    .getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                manager.notify(id, notification)
            }
        }
    }
}

//запуск сервиса
fun scheduleService(
    flightNumber: String, //номер рейса
    airlineIata: String, //код авиалинии
    airportIata: String, //код аэропорта вылета
    date: Date? = null //дата запуска сервиса
) {
    val context = App.getInstance().applicationContext
    //intent для запуска сервиса
    val serviceIntent = Intent(context, UpdateTrackedFlightService::class.java)
    serviceIntent.putExtra("flightNumber", flightNumber)
        .putExtra("airlineIata", airlineIata)
        .putExtra("airportIata", airportIata)

    //если запуск отложенный, то ставим в AlarmManager
    if (date != null) {
        val pendingIntent = PendingIntent.getService(context, 2, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = App.getInstance().getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(RTC_WAKEUP, date.time, pendingIntent)
    }
    //иначе сразу запускаем
    else {
        context.startService(serviceIntent)
    }
}

//ставим напоминание
fun scheduleReminder(
    calendar: Calendar, //дата запуска напоминания
    flightNumber: String, //номер рейса
    airlineIata: String, //код авиалинии
    airportIata: String, //код аэропорта
    id: Int //id записи
) {
    //собираем intent
    val context = App.getInstance().applicationContext
    val intent = Intent(context, ScheduledNotification::class.java)
    intent.putExtra("flightNumber", flightNumber)
        .putExtra("notificationId", id)
        .putExtra("airlineIata", airlineIata)
        .putExtra("airportIata", airportIata)
    val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

    //ставим отложенный запуск
    val alarmManager = App.getInstance().getSystemService(ALARM_SERVICE) as AlarmManager
    alarmManager.setExactAndAllowWhileIdle(RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
}