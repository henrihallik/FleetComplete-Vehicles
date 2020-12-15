package com.fleetcomplete.vehicles.view

import com.fleetcomplete.vehicles.model.locationhistory.LocationHistory
import java.util.*

interface LocationHistoryView {
    fun showProgress()
    fun hideProgress()
    fun setLocationHistory(locationHistory: LocationHistory)
    fun getLocationHistoryFailed(strError: String)
    fun onDateChanged(date : Date)
}