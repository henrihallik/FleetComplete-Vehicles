package com.fleetcomplete.vehicles.model

data class VehiclesData(
    val meta: Meta,
    val response: List<Response>,
    val status: Int
)