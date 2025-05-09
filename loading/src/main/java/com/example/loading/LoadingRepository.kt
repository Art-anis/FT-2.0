package com.example.loading

import android.content.Context
import android.preference.PreferenceManager
import com.example.db.dao.AirportDao
import com.example.db.dao.CityDao
import com.example.db.entities.CityEntity
import com.example.loading.util.toEntity
import com.example.network.api.AirportsAPI
import com.example.network.models.ResponseAirport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

//репозиторий для загрузки начальных данных
class LoadingRepository(
    private val airportsApi: AirportsAPI, //api аэропортов
    private val airportsDao: AirportDao, //dao аэропортов
    private val citiesDao: CityDao //dao городов
) {

    //загрузка аэропортов
    suspend fun loadAirports(context: Context) {
        //получаем все аэропорты
        val airports = CoroutineScope(Dispatchers.IO).async {
            airportsApi.getAllAirports()
        }

        //название файла
        val fileName = context.getString(R.string.airports_csv_name)
        //открываем файл с российскими названиями аэропортов
        context.assets.open(fileName).bufferedReader().use { reader ->
            //считываем заголовок
            reader.readLine()

            //sharedPref для сохранения прогресса загрузки
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = pref.edit()

            //строковые ресурсы, выделяем для избежания дублирования
            val iataColumn = context.getString(R.string.iata_column) //столбец iata в csv
            val nameColumn = context.getString(R.string.name_column) //столбец name в csv
            val unknownGeoname = context.getString(R.string.unknown_geoname_id) //нулевой geoname id
            val ruIso = context.getString(R.string.russian_iso) //"RU"
            //ключи в SharedPref
            val totalAirportsKey = context.getString(R.string.airports_size_pref_key) //общее кол=во аэропортов
            val loadedAirportsKey = context.getString(R.string.airports_loaded_pref_key) //кол-во уже загруженных в БД аэропортов

            //получаем данные о российских аэропортах
            val russianAirports = reader.lineSequence()
                //разбиваем строки
                .map { row -> row.split(';') }
                //преобразовываем данные из строки в словарь
                .map { row -> mapOf(iataColumn to row[0],
                    nameColumn to row[1]) }
                .toList()

            //ожидаем данных из API
            val response = airports.await()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    //убираем российские аэропорты, которых нет в таблице
                    val actualAirports = body.filterNot { airport ->
                        airport.geonameId == unknownGeoname
                                && airport.codeIso2Country == ruIso
                    }


                    //записываем в SharedPref размер списка
                    editor.putInt(totalAirportsKey, actualAirports.size)
                        .apply()

                    actualAirports.forEachIndexed { index, airport ->
                        //российский аэропорт переименовываем перед тем, как добавить в БД
                        if (airport.codeIso2Country == ruIso) {
                            airportsDao.addAirport(
                                remapAirportName(
                                    airport,
                                    russianAirports
                                ).toEntity()
                            )
                        }
                        //иначе просто добавляем
                        else {
                            airportsDao.addAirport(airport.toEntity())
                        }

                        //обновляем количество загруженных аэропортоа
                        editor.putInt(loadedAirportsKey, index + 1)
                            .apply()
                    }
                }
            }
        }
    }

    //функция изменения названия аэропорта
    private fun remapAirportName(airport: ResponseAirport, names: List<Map<String, String>>): ResponseAirport {
        //ищем аэропорт в списке с идентичным iata и возвращаем скопированную сущность с новым названием
        return airport.copy(nameAirport = names.filter { it["iata"] == airport.codeIataAirport }[0]["name"])
    }

    //загрузка городов
    suspend fun loadCities(context: Context) {
        //название файла
        val fileName = context.getString(R.string.cities_csv_name)
        //открываем файл с городами
        context.assets.open(fileName).bufferedReader().use { reader ->
            //считываем заголовое
            reader.readLine()

            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = pref.edit()

            //строковые ресурсы
            val iataColumn = context.getString(R.string.iata_column) //столбец iata в csv
            val nameColumn = context.getString(R.string.name_column) //столбец name в csv
            val timezoneColumn = context.getString(R.string.timezone_column) //столбец timezone в csv

            //ключи в SharedPref
            val totalCitiesKey = context.getString(R.string.cities_total_pref_key) //общее кол-во городов
            val loadedCitiesKey = context.getString(R.string.cities_loaded_pref_key) //кол-во загруженных городов

            //перебор всех строк файла, кроме строки заголовков
            val actualCities = reader.lineSequence()
                //разбиение строки на фактические значения
                .map { row -> row.split(';') }
                //фильтр конкретных значений, которые участвуют в формировании сущности
                .map { row -> mapOf("iata" to row[1], "name" to row[3], "timezone" to row[6]) }
                .toList()

            //записываем в SharedPref размер списка городов
            editor.putInt(totalCitiesKey, actualCities.size)
                .apply()
            //перебираем все города
            actualCities.forEachIndexed { index, city ->
                //формируем из словаря сущность и добавляем ее в БД
                citiesDao.addCity(CityEntity(
                    name = city[nameColumn]!!,
                    iata = city[iataColumn]!!,
                    timeZone = city[timezoneColumn]!!
                ))

                //обновляем количество загруженных городов
                editor.putInt(loadedCitiesKey, index + 1)
                    .apply()
            }
        }
    }
}