package com.fleetcomplete.vehicles.view

import com.fleetcomplete.vehicles.model.VehiclesAllData

interface VehiclesHomeView {
    fun showProgress()
    fun hideProgress()
    fun setVehiclesData(arrVehicleUpdates: VehiclesAllData)
    fun getDataFailed(strError: String)
    fun onItemClick(adapterPosition: Int)
}