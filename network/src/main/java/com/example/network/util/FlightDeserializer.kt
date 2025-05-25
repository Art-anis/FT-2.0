package com.example.network.util

import com.example.network.models.ResponseFutureFlight
import com.example.network.models.flight_schedule_subclasses.Flight
import com.example.network.models.future_flight_subclasses.FutureFlightAircraft
import com.example.network.models.flight_subclasses.FlightAirline
import com.example.network.models.flight_schedule_subclasses.FlightCodeshared
import com.example.network.models.future_flight_subclasses.FutureFlightDestination
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import java.lang.reflect.Type

//кастомный десериализатор для рейсов
class FlightDeserializer: JsonDeserializer<Array<ResponseFutureFlight>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Array<ResponseFutureFlight> {
        //работаем только, если в ответе массив
        if (json?.isJsonArray == true) {
            val array = json.asJsonArray
            //собираем массив
            val result = Array(array.size()) { index ->
                //сам элемент
                val item = array[index].asJsonObject

                //день недели
                val weekday = item.get("weekday").asString

                //данные о месте вылета
                val departureObject = item.get("departure").asJsonObject
                val departure = FutureFlightDestination(
                    iataCode = departureObject.get("iataCode").asString,
                    icaoCode = departureObject.get("icaoCode").asString,
                    terminal = departureObject.get("terminal").asString,
                    gate = departureObject.get("gate").asString,
                    scheduledTime = departureObject.get("scheduledTime").asString
                )

                //данные о месте прибытия
                val arrivalObject = item.get("arrival").asJsonObject
                val arrival = FutureFlightDestination(
                    iataCode = arrivalObject.get("iataCode").asString,
                    icaoCode = arrivalObject.get("icaoCode").asString,
                    terminal = arrivalObject.get("terminal").asString,
                    gate = arrivalObject.get("gate").asString,
                    scheduledTime = arrivalObject.get("scheduledTime").asString
                )

                //данные о самолете
                val aircraftObject = item.get("aircraft").asJsonObject
                val aircraft = FutureFlightAircraft(
                    modelCode = aircraftObject.get("modelCode").asString,
                    modelText = aircraftObject.get("modelText").asString
                )

                //данные о главной авиалинии
                val airlineObject = item.get("airline").asJsonObject
                val airline = FlightAirline(
                    name = airlineObject.get("name").asString,
                    iataCode = airlineObject.get("iataCode").asString,
                    icaoCode = airlineObject.get("icaoCode").asString
                )

                //данные о рейсе
                val flightObject = item.get("flight").asJsonObject
                val flight = Flight(
                    number = flightObject.get("number").asString,
                    iataNumber = flightObject.get("iataNumber").asString,
                    icaoNumber = flightObject.get(("icaoNumber")).asString
                )

                //собираем кодшеринг, если он есть
                val codeshared = if (item.get("codeshared") == JsonNull.INSTANCE || item.get("codeshared") == null) null else {

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

                //итоговый объект
                ResponseFutureFlight(
                    weekday = weekday,
                    departure = departure,
                    arrival = arrival,
                    aircraft = aircraft,
                    airline = airline,
                    flight = flight,
                    codeshared = codeshared
                )
            }
            return result
        }
        return emptyArray()
    }

}