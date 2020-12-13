package com.fleetcomplete.vehicles.presenter

import com.fleetcomplete.vehicles.model.VehiclesData
import com.fleetcomplete.vehicles.model.VehiclesHomeInteractor
import com.fleetcomplete.vehicles.view.VehiclesDataView

class VehiclesDataPresenter(private var vehiclesHomeView: VehiclesDataView?, private val vehiclesHomeInteractor: VehiclesHomeInteractor)
    : VehiclesHomeInteractor.OnFinishedListener {

    fun getNewData() {
        vehiclesHomeView?.showProgress()
        vehiclesHomeInteractor.requestVehiclesDataAPI(this)
    }

    fun onDestroy() {
        vehiclesHomeView = null
    }

    override fun onResultSuccess(arrVehicleUpdates: VehiclesData) {
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