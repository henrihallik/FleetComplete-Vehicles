package com.fleetcomplete.vehicles.model.locationhistory

data class LocationHistory(
    val meta: Meta,
    val response: List<Response>,
    val status: Int
)