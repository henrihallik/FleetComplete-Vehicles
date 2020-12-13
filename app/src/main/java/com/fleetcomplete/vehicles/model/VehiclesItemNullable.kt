package com.fleetcomplete.vehicles.model

import com.google.gson.annotations.SerializedName

data class VehiclesItemNullable(
        @SerializedName("speed") val speed: String? = null,
        @SerializedName("location") val location: String? = null,
        @SerializedName("driver") val driver: String? = null
)