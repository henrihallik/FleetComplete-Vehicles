package com.fleetcomplete.vehicles.model

import com.google.gson.annotations.SerializedName

data class VehiclesResponseNullable(
        @SerializedName("vehicles") val vehicles: List<VehiclesItemNullable?>? = null
)