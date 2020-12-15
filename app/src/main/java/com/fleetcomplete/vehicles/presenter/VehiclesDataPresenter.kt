package com.fleetcomplete.vehicles.presenter

import com.fleetcomplete.vehicles.model.Response
import com.fleetcomplete.vehicles.model.VehiclesData
import com.fleetcomplete.vehicles.model.VehiclesDataInteractor
import com.fleetcomplete.vehicles.view.VehiclesDataView

class VehiclesDataPresenter(private var vehiclesDataView: VehiclesDataView?, private val vehiclesHomeInteractor: VehiclesDataInteractor)
    : VehiclesDataInteractor.OnFinishedListener {

    fun getNewData() {
        vehiclesDataView?.showProgress()
        vehiclesHomeInteractor.requestVehiclesDataAPI(this)
    }

    fun onDestroy() {
        vehiclesDataView = null
    }

    override fun onResultSuccess(arrVehicleUpdates: VehiclesData) {
        vehiclesDataView?.hideProgress()
        vehiclesDataView?.setVehiclesData(arrVehicleUpdates)
    }

    override fun onResultFail(strError: String) {
        vehiclesDataView?.hideProgress()
        vehiclesDataView?.getDataFailed(strError)
    }

    fun onItemClick(vehicle: Response) {
        vehiclesDataView?.onItemClick(vehicle)
    }
}