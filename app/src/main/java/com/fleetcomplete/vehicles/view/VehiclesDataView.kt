package com.fleetcomplete.vehicles.view

import com.fleetcomplete.vehicles.model.VehiclesData

interface VehiclesDataView {
    fun showProgress()
    fun hideProgress()
    fun setVehiclesData(arrVehicleUpdates: VehiclesData)
    fun getDataFailed(strError: String)
    fun onItemClick(adapterPosition: Int)
}