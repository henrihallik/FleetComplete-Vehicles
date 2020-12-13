package com.fleetcomplete.vehicles.model

data class Response(
    val CANDistance: Any,
    val EVDistanceRemaining: Any,
    val EVStateOfCharge: Any,
    val EventType: String,
    val address: String,
    val addressArea: Any,
    val available: Any,
    val customValues: List<Any>,
    val direction: Int,
    val displayColor: Any,
    val driverId: Int,
    val driverIsOnDuty: Boolean,
    val driverKey: Any,
    val driverName: String,
    val driverPhone: Any,
    val driverStatuses: List<Any>,
    val dutyTags: List<Any>,
    val employeeId: Any,
    val enforcePrivacyFilter: Any,
    val enginestate: Int,
    val externalId: Any,
    val fuel: Any,
    val gpsstate: Boolean,
    val inPrivateZone: Boolean,
    val lastEngineOnTime: String,
    val latitude: Double,
    val longitude: Double,
    val objectId: Int,
    val objectName: String,
    val offWorkSchedule: Boolean,
    val orgId: Int,
    val pairedObjectId: Any,
    val pairedObjectName: Any,
    val plate: String,
    val power: Double,
    val speed: Int,
    val tcoCardIsPresent: Boolean,
    val tcoData: Any,
    val timestamp: String,
    val tripPurposeDinSet: Any
)