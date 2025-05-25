package com.example.network.util

import android.util.Log
import com.example.network.models.ResponseFlightSchedule
import com.example.network.models.flight_schedule_subclasses.Flight
import com.example.network.models.flight_schedule_subclasses.FlightCodeshared
import com.example.network.models.flight_schedule_subclasses.FlightScheduleDestination
import com.example.network.models.flight_subclasses.FlightAirline
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import java.lang.reflect.Type

//десериализатор для ответа по рейсам
class FlightScheduleDeserializer: JsonDeserializer<Array<ResponseFlightSchedule>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Array<ResponseFlightSchedule> {
        if (json?.isJsonArray == true) {
            val array = json.asJsonArray

            val result = Array(array.size()) { index ->
                val item = array[index].asJsonObject
                //данные о главной авиалинии
                val airlineObject = item.get("airline").asJsonObject
                val airline = FlightAirline(
                    name = airlineObject.get("name").asString,
                    iataCode = airlineObject.get("iataCode").asString,
                    icaoCode = airlineObject.get("icaoCode").asString
                )

                //данные о месте прибытия
                val arrivalObject = item.get("arrival").asJsonObject
                val arrival = with(arrivalObject) {
                    FlightScheduleDestination(
                        iataCode = handleValue(this, "iataCode"),
                        icaoCode = handleValue(this, "icaoCode"),
                        terminal = handleValue(this, "terminal"),
                        gate = handleValue(this, "gate"),
                        scheduledTime = handleValue(this, "scheduledTime"),
                        actualRunway = handleValue(this, "actualRunway"),
                        actualTime = handleValue(this, "actualTime"),
                        baggage = handleValue(this, "baggage"),
                        delay = handleValue(this, "delay"),
                        estimatedRunway = handleValue(this, "estimatedRunway"),
                        estimatedTime = handleValue(this, "estimatedTime")
                    )
                }

                //данные о месте вылета
                val departureObject = item.get("departure").asJsonObject
                val departure = with(departureObject) {
                    FlightScheduleDestination(
                        iataCode = handleValue(this, "iataCode"),
                        icaoCode = handleValue(this, "icaoCode"),
                        terminal = handleValue(this, "terminal"),
                        gate = handleValue(this, "gate"),
                        scheduledTime = handleValue(this, "scheduledTime"),
                        actualRunway = handleValue(this, "actualRunway"),
                        actualTime = handleValue(this, "actualTime"),
                        baggage = handleValue(this, "baggage"),
                        delay = handleValue(this, "delay"),
                        estimatedRunway = handleValue(this, "estimatedRunway"),
                        estimatedTime = handleValue(this, "estimatedTime")
                    )
                }

                //собираем кодшеринг, если он есть
                val codeshared = if (item.get("codeshared") == JsonNull.INSTANCE) null else {

                    val codesharedObject = item.get("codeshared").asJsonObject

                    //номер рейса
                    val codesharedFlightObject = codesharedObject.get("flight").asJsonObject
                    val codesharedFlight = Flight(
                        number = codesharedFlightObject.get("number").asString,
                        iataNumber = codesharedFlightObject.get("iataNumber").asString,
                        icaoNumber = codesharedFlightObject.get(("icaoNumber")).asString
                    )

                    //авиалиния
                    val codesharedAirlineObject = codesharedObject.get("airline").asJsonObject
                    val codesharedAirline = FlightAirline(
                        name = codesharedAirlineObject.get("name").asString,
                        iataCode = codesharedAirlineObject.get("iataCode").asString,
                        icaoCode = codesharedAirlineObject.get("icaoCode").asString
                    )

                    FlightCodeshared(
                        airline = codesharedAirline,
                        flight = codesharedFlight
                    )
                }

                //данные о рейсе
                val flightObject = item.get("flight").asJsonObject
                val flight = Flight(
                    number = flightObject.get("number").asString,
                    iataNumber = flightObject.get("iataNumber").asString,
                    icaoNumber = flightObject.get(("icaoNumber")).asString
                )

                ResponseFlightSchedule(
                    airline = airline,
                    arrival = arrival,
                    codeshared = codeshared,
                    departure = departure,
                    flight = flight,
                    status = item.get("status").asString,
                    type = item.get("type").asString
                )
            }
            return result
        }
        else {
            return emptyArray()
        }
    }

    private fun handleValue(obj: JsonObject, name: String): String? {
        return if (obj.get(name) == JsonNull.INSTANCE) null else obj.get(name).asString
    }
}