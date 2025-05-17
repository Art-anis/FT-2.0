package com.example.network.models

import com.google.gson.annotations.SerializedName

data class ResponseAirline(
    @SerializedName("ageFleet") var ageFleet: Double? = null,
    @SerializedName("airlineId") var airlineId: Int? = null,
    @SerializedName("callsign") var callsign: String? = null,
    @SerializedName("codeHub") var codeHub: String? = null,
    @SerializedName("codeIataAirline") var codeIataAirline: String? = null,
    @SerializedName("codeIcaoAirline") var codeIcaoAirline: String? = null,
    @SerializedName("codeIso2Country") var codeIso2Country: String? = null,
    @SerializedName("founding") var founding: Int? = null,
    @SerializedName("iataPrefixAccounting") var iataPrefixAccounting: String? = null,
    @SerializedName("nameAirline") var nameAirline: String? = null,
    @SerializedName("nameCountry") var nameCountry: String? = null,
    @SerializedName("sizeAirline") var sizeAirline: Int? = null,
    @SerializedName("statusAirline") var statusAirline: String? = null,
    @SerializedName("type") var type: String? = null
)
