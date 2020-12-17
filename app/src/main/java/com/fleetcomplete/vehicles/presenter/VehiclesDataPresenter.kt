package com.fleetcomplete.vehicles.presenter

import com.fleetcomplete.vehicles.model.Response
import com.fleetcomplete.vehicles.model.VehiclesData
import com.fleetcomplete.vehicles.model.VehiclesDataInteractor
import com.fleetcomplete.vehicles.view.VehiclesDataView

class VehiclesDataPresenter(private var vehiclesDataView: VehiclesDataView?)
    : VehiclesDataInteractor.OnFinishedListener {

    private val vehiclesDataInteractor : VehiclesDataInteractor = VehiclesDataInteractor()

    fun getNewData() {
        vehiclesDataView?.showProgress()
        vehiclesDataInteractor.requestVehiclesDataAPI(this)
    }

    fun onDestroy() {
        vehiclesDataView = null
    }

    override fun onResultSuccess(vehiclesData: VehiclesData) {
        vehiclesDataView?.hideProgress()
        vehiclesDataView?.setVehiclesData(vehiclesData)
    }

    override fun onResultFail(strError: String) {
        vehiclesDataView?.hideProgress()
        vehiclesDataView?.getDataFailed(strError)
    }

    fun onItemClick(vehicle: Response) {
        vehiclesDataView?.onItemClick(vehicle)
    }
}