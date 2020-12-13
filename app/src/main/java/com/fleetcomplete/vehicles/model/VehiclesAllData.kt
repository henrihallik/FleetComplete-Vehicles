package com.fleetcomplete.vehicles.model

data class VehiclesAllData(
    val meta: Meta,
    val response: List<Response>,
    val status: Int
)