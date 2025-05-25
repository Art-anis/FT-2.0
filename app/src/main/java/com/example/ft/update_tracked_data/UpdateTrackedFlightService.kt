package com.example.ft.update_tracked_data

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.airport.AirportRepository
import com.example.db.dao.AirportDao
import com.example.db.dao.CityDao
import com.example.db.dao.TrackedFlightDao
import com.example.db.entities.TrackedFlightEntity
import com.example.ft.App
import com.example.ft.MainActivity
import com.example.ft.R
import com.example.ft.UpdateDataActivity
import com.example.ft.notifications.scheduleService
import com.example.network.api.FlightScheduleAPI
import com.example.network.models.ResponseFlightSchedule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.reflect.full.memberProperties

//сервис отслеживания изменений данных о рейсе
class UpdateTrackedFlightService: Service() {

    //объект job для корутины
    private val job = SupervisorJob()
    //scope для корутины
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    //при запуске
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //получаем api расписаний
        val api by inject<FlightScheduleAPI>()

        //dao отслеживаемых рейсов
        val dao by inject<TrackedFlightDao>()

        intent?.let {
            //извлекаем данные из intent
            val airlineIata = it.getStringExtra("airlineIata") ?: ""
            val airportIata = it.getStringExtra("airportIata") ?: ""
            val flightNumber = it.getStringExtra("flightNumber") ?: ""
            //запускаем корутину
            scope.launch {
                //делаем запрос в api
                val result = api.getScheduledFlight(
                    airportIata = airportIata,
                    type = "departure",
                    airlineIata = airlineIata,
                    flightNumber = flightNumber
                )

                //разбиваем код рейса
                val flightIata = flightNumber.splitFlightIata(airlineIata)

                //если все ок
                if (result.code() == 200) {
                    //если тело не пустое
                    if (!result.body().isNullOrEmpty()) {
                        result.body()?.let { flights ->
                            //в нормальном сценарии должен быть всего один результат
                            if (flights.size == 1) {
                                //выделяем его
                                val apiFlight = flights[0]

                                //переводим дату вылета в миллисекунды
                                val formattedDeparture = if (apiFlight.departure?.estimatedTime.toMillis() != 0L) apiFlight.departure?.estimatedTime.toMillis()
                                    else apiFlight.departure?.scheduledTime.toMillis()

                                //получаем запись из БД
                                val dbFlight = dao.getFlightByIata(flightIata, formattedDeparture)

                                //получаем фактические времена вылета и прибытия, если есть
                                val actualDepartureTime = apiFlight.departure?.actualTime.toMillis()
                                val actualArrivalTime = apiFlight.arrival?.actualTime.toMillis()

                                //если нашли запись
                                dbFlight?.let {
                                    //конвертируем ответ api в формат БД
                                    val convertedApiFlight = apiFlight.toEntity(dbFlight.id, actualDepartureTime, actualArrivalTime)

                                    //названия городов
                                    val departureCity =
                                        getCityNameByAirportIata(dbFlight.departureIata)
                                    val arrivalCity =
                                        getCityNameByAirportIata(dbFlight.arrivalIata)

                                    //если есть отличия
                                    if (convertedApiFlight != dbFlight) {
                                        //получаем список названий отличающихся полей
                                        val differentFields = dbFlight.getDifferentFields(convertedApiFlight)

                                        //сами поля в соответствии со старым и новым значением
                                        val differences = differentFields.associateWith { fieldName ->
                                            val field = TrackedFlightEntity::class.memberProperties.first { field -> field.name == fieldName }
                                            Pair(
                                                field.getter.call(dbFlight).toString(),
                                                field.getter.call(convertedApiFlight)
                                                    .toString()
                                            )
                                        }.toMutableMap()

                                        //контекст для intent
                                        val context = App.getInstance().applicationContext

                                        //если рейс отменен
                                        if (convertedApiFlight.status == "cancelled") {
                                            //собираем intent в список отслеживаемых рейсов
                                            val activityIntent = Intent(context, MainActivity::class.java)
                                            activityIntent.putExtra("flightNumber", flightIata)
                                                .putExtra("date", dbFlight.scheduledDeparture)
                                                .putExtra("cancelled", true)

                                            val pendingIntent = PendingIntent.getActivity(
                                                context,
                                                1,
                                                activityIntent,
                                                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                                            )

                                            //собираем уведомление и запускаем его
                                            val notification = NotificationCompat.Builder(App.getInstance().applicationContext, "0")
                                                .setSmallIcon(R.mipmap.ic_launcher)
                                                .setContentTitle("Flight $flightIata has been cancelled!")
                                                .setContentText("Click to remove it from your watchlist!")
                                                .setContentIntent(pendingIntent)
                                                .setAutoCancel(true)
                                                .build()

                                            val notificationManager = App.getInstance().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                                            notificationManager.notify(dbFlight.id, notification)
                                        }
                                        //если самолет вылетел
                                        else if (actualDepartureTime != 0L && actualArrivalTime == 0L) {
                                            //собираем intent в экран просмотра обновлений
                                            val activityIntent = Intent(context, UpdateDataActivity::class.java)
                                            differences["estimatedDeparture"] = Pair(dbFlight.scheduledDeparture.toString(), actualDepartureTime.toString())

                                            activityIntent.putExtra("flightNumber", flightIata)
                                                .putExtra("departure", Pair(departureCity ?: "", dbFlight.departureIata))
                                                .putExtra("arrival", Pair(arrivalCity ?: "", dbFlight.arrivalIata))
                                                .putExtra("differences", HashMap(differences) as Serializable)
                                                .putExtra("departureTime", dbFlight.scheduledDeparture)

                                            val pendingIntent = PendingIntent.getActivity(
                                                context,
                                                1,
                                                activityIntent,
                                                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                                            )

                                            //запускаем уведомление
                                            postUpdate(
                                                id = dbFlight.id,
                                                intent = pendingIntent,
                                                title = "Flight $flightIata has taken off!"
                                            )
                                        }
                                        //иначе просто выводим данные об обновлениях
                                        else {
                                            //собираем intent
                                            val activityIntent = Intent(context, UpdateDataActivity::class.java)
                                            activityIntent.putExtra("flightNumber", flightIata)
                                                .putExtra("differences", HashMap(differences) as Serializable)
                                                .putExtra("departure", Pair(departureCity ?: "", dbFlight.departureIata))
                                                .putExtra("arrival", Pair(arrivalCity ?: "", dbFlight.arrivalIata))
                                                .putExtra("departureTime", dbFlight.scheduledDeparture)

                                            val pendingIntent = PendingIntent.getActivity(context, 1, activityIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

                                            //запускаем уведомление
                                            postUpdate(
                                                id = dbFlight.id,
                                                intent = pendingIntent,
                                                title = "Data about flight $flightIata has been updated!"
                                            )
                                        }
                                    }
                                    //отличий нет, планируем следующую проверку
                                    else {
                                        val calendar = Calendar.getInstance()
                                        calendar.timeInMillis = Date().time
                                        //если осталось менее часа, то интервал - 10 минут
                                        if (lessThanHourDifference(dbFlight.estimatedDeparture, Date().time)) {
                                            calendar.add(Calendar.MINUTE, 10)
                                        }
                                        //иначе - интервал час
                                        else {
                                            calendar.add(Calendar.HOUR_OF_DAY, 1)
                                        }
                                        //планируем запуск сервиса
                                        scheduleService(
                                            flightNumber = flightNumber,
                                            airlineIata = airlineIata,
                                            airportIata = airportIata,
                                            date = calendar.time
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //останавливаем сервис
        stopSelf()
        return super.onStartCommand(intent, flags, startId)
    }

    //определяем, если разница между датами менее часа
    private fun lessThanHourDifference(a: Long, b: Long): Boolean {
        return ((a - b) / 1000 / 60 / 60) < 1
    }

    //получение названия города по iata аэропорта
    private suspend fun getCityNameByAirportIata(iata: String): String? {
        //получеам dao
        val cityDao by inject<CityDao>()
        val airportDao by inject<AirportDao>()

        //получаем аэропорт
        val airport = airportDao.getAirportByIata(iata)
        return airport?.let {
            //получаем название города
            cityDao.getCityByIata(it.cityIata)?.name
        }
    }

    //перевод даты в миллисекунды
    private fun String?.toMillis(): Long {
        if (this != null) {
            //убираем миллисекунды и Т из текста
            val edited = this.removeSuffix(".000").replace("T", "-")
            //форматируем дату
            val format = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.getDefault())
            return format.parse(edited)?.time ?: 0
        }
        else return 0
    }

    //запуск уведомления
    private fun postUpdate(
        id: Int, //id уведомления
        intent: PendingIntent, //intent
        title: String //название уведомления
    ) {
        val context = App.getInstance().applicationContext
        //собираем уведомление и запускаем его
        val notification = NotificationCompat.Builder(context, "0")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText("Click to find out more!")
            .setContentIntent(intent)
            .setAutoCancel(true)
            .build()

        val notificationManager = App.getInstance().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id, notification)
    }

    //разбиение iata рейса пробелом
    private fun String.splitFlightIata(airlineIata: String): String {
        return airlineIata + " " + this.replace(airlineIata, "")
    }

    //конвертация ответа api в формат БД
    private fun ResponseFlightSchedule.toEntity(id: Int, actualDeparture: Long, actualArrival: Long): TrackedFlightEntity {
        //получаем iata кодшеринга
        val codesharedFlightIata = this.codeshared?.let { codeshared ->
            codeshared.airline?.iataCode?.let { airlineIata ->
                codeshared.flight?.iataNumber?.splitFlightIata(airlineIata)
            }
        } ?: ""

        //iata рейса
        val flightIata = this.flight?.let { flight ->
            this.airline?.iataCode?.let { airlineIata ->
                flight.iataNumber?.splitFlightIata(airlineIata)
            }
        } ?: ""

        //собираем сущность БД
        return TrackedFlightEntity(
            id = id,
            airlineIata = this.airline?.iataCode ?: "",
            scheduledArrival = this.arrival?.scheduledTime.toMillis(),
            estimatedArrival = if (actualArrival == 0L) this.arrival?.estimatedTime.toMillis() else actualArrival,
            baggageBelt = this.arrival?.baggage ?: "",
            arrivalDelay = this.arrival?.delay?.toInt() ?: 0,
            arrivalGate = this.arrival?.gate ?: "",
            arrivalIata = this.arrival?.iataCode ?: "",
            arrivalTerminal = this.arrival?.terminal ?: "",
            codesharedAirlineIata = this.codeshared?.airline?.iataCode ?: "",
            codesharedFlightIata = codesharedFlightIata,
            scheduledDeparture = this.departure?.scheduledTime.toMillis(),
            estimatedDeparture = if (actualDeparture == 0L) this.departure?.estimatedTime.toMillis() else actualDeparture,
            departureDelay = this.departure?.delay?.toInt() ?: 0,
            departureGate = this.departure?.gate ?: "",
            departureIata = this.departure?.iataCode ?: "",
            departureTerminal = this.departure?.terminal ?: "",
            flightIata = flightIata,
            status = this.status ?: ""
        )
    }
}