package com.fleetcomplete.vehicles.model.locationhistory

data class Response(
    val Din1: Any,
    val Direction: Double,
    val Distance: Double,
    val DriverId: Any,
    val EngineStatus: Any,
    val GPSState: String,
    val Latitude: Double,
    val Longitude: Double,
    val Power: Double,
    val ServerGenerated: Any,
    val Speed: Any,
    val SplitSegment: Any,
    val timestamp: String
)