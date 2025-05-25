package com.example.db.entities

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

//отслеживаемые рейсы
@Entity(tableName = "tracked_flights", indices = [Index(value = ["scheduled_departure", "flight_iata"], unique = true)])
data class TrackedFlightEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") val id: Int = 0, //id
    @ColumnInfo("airline_iata") val airlineIata: String, //iata авиалинии
    @ColumnInfo("scheduled_arrival", defaultValue = "0") val scheduledArrival: Long = 0, //время прибытия по расписанию
    @ColumnInfo("estimated_arrival", defaultValue = "0") val estimatedArrival: Long = 0, //ожидаемое или фактическое время прибытия
    @ColumnInfo("baggage") val baggageBelt: String = "", //лента для багажа
    @ColumnInfo("arrival_delay") val arrivalDelay: Int = 0, //задержка прибытия
    @ColumnInfo("arrival_gate") val arrivalGate: String, //выход в прибытии
    @ColumnInfo("arrival_iata") val arrivalIata: String, //iata аэропорта прибытия
    @ColumnInfo("arrival_terminal") val arrivalTerminal: String, //терминал аэропорта прибытия
    @ColumnInfo("codeshared_airline_iata") val codesharedAirlineIata: String, //iata авиалинии кодшеринга
    @ColumnInfo("codeshared_flight_iata") val codesharedFlightIata: String, //iata рейса кодшеринга
    @ColumnInfo("scheduled_departure", defaultValue = "0") val scheduledDeparture: Long = 0, //время вылета по расписанию
    @ColumnInfo("estimated_departure", defaultValue = "0") val estimatedDeparture: Long = 0, //ожидаемое или фактическое время вылета
    @ColumnInfo("departure_delay") val departureDelay: Int = 0, //задержка вылета
    @ColumnInfo("departure_gate") val departureGate: String, //выход в аэропорту вылета
    @ColumnInfo("departure_iata") val departureIata: String, //iata аэропорта вылета
    @ColumnInfo("departure_terminal") val departureTerminal: String, //терминал аэропорта вылета
    @ColumnInfo("flight_iata") val flightIata: String, //iata основного рейса
    @ColumnInfo("status") val status: String = "" //статус рейса
) {

    //получение списка отличающихся полей
    fun getDifferentFields(other: TrackedFlightEntity): List<String> {
        //список полей
        val fields = (this::class).memberProperties
        val result = mutableListOf<String>()
        for (field in fields) {
            field.isAccessible = true
            //получаем значения
            val value1 = field.getter.call(this)
            val value2 = field.getter.call(other)
            if (value1 != value2) {
                //игнорируем особые случаи
                if (field.name.contains("scheduled")) continue
                if (field.name == "estimatedDeparture" && (value2 == this.scheduledDeparture || value2 == 0L)) continue
                if (field.name == "estimatedArrival" && (value2 == this.scheduledArrival || value2 == 0L)) continue
                //добавление в список
                result.add(field.name)
            }
        }
        return result
    }
}
