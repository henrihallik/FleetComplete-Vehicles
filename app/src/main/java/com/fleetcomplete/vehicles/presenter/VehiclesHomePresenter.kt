package com.fleetcomplete.vehicles.presenter

import com.fleetcomplete.vehicles.model.VehiclesAllData
import com.fleetcomplete.vehicles.model.VehiclesHomeInteractor
import com.fleetcomplete.vehicles.view.VehiclesHomeView

class VehiclesHomePresenter(private var vehiclesHomeView: VehiclesHomeView?, private val vehiclesHomeInteractor: VehiclesHomeInteractor)
    : VehiclesHomeInteractor.OnFinishedListener {

    fun getNewsData() {
        vehiclesHomeView?.showProgress()
        vehiclesHomeInteractor.requestVehiclesDataAPI(this)
    }

    fun onDestroy() {
        vehiclesHomeView = null
    }

    override fun onResultSuccess(arrVehicleUpdates: VehiclesAllData) {
        vehiclesHomeView?.hideProgress()
        vehiclesHomeView?.setVehiclesData(arrVehicleUpdates)
    }

    override fun onResultFail(strError: String) {
        vehiclesHomeView?.hideProgress()
        vehiclesHomeView?.getDataFailed(strError)
    }

    fun onItemClick(adapterPosition: Int) {
        vehiclesHomeView?.onItemClick(adapterPosition)
    }
}