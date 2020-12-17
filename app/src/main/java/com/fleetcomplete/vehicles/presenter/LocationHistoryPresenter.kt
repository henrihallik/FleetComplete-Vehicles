package com.fleetcomplete.vehicles.presenter

import com.fleetcomplete.vehicles.model.locationhistory.LocationHistoryInteractor
import com.fleetcomplete.vehicles.model.VehiclesData
import com.fleetcomplete.vehicles.model.locationhistory.LocationHistory
import com.fleetcomplete.vehicles.view.LocationHistoryView
import java.util.*

class LocationHistoryPresenter(private var locationHistoryView: LocationHistoryView?)
    : LocationHistoryInteractor.OnFinishedListener {

    private val locationHistoryInteractor: LocationHistoryInteractor = LocationHistoryInteractor()

    fun getNewData(objectId : Int, date : Date) {
        locationHistoryView?.showProgress()
        locationHistoryInteractor.requestLocationHistory(this, objectId, date)
    }

    fun onDestroy() {
        locationHistoryView = null
    }

    override fun onResultSuccess(locationHistory: LocationHistory) {
        locationHistoryView?.hideProgress()
        locationHistoryView?.setLocationHistory(locationHistory)
    }

    override fun onResultFail(strError: String) {
        locationHistoryView?.hideProgress()
        locationHistoryView?.getLocationHistoryFailed(strError)
    }
}