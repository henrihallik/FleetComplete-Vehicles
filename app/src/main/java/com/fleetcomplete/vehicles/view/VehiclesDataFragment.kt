/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fleetcomplete.vehicles.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fleetcomplete.vehicles.MainActivity
import com.fleetcomplete.vehicles.R
import com.fleetcomplete.vehicles.model.Response
import com.fleetcomplete.vehicles.model.VehiclesData
import com.fleetcomplete.vehicles.model.VehiclesDataInteractor
import com.fleetcomplete.vehicles.presenter.VehiclesDataPresenter
import com.fleetcomplete.vehicles.showToast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_vehicles_data.*
import kotlinx.android.synthetic.main.fragment_vehicles_data.view.*
import java.lang.NullPointerException

class VehiclesDataFragment : Fragment(), VehiclesDataView {
    private lateinit var vehiclesHomePresenter: VehiclesDataPresenter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vehicles_data, container, false)

        vehiclesHomePresenter = VehiclesDataPresenter(this, VehiclesDataInteractor())
        view.recyclerView.setHasFixedSize(true)

        return view
    }

    override fun onResume() {
        super.onResume()
        if(view?.recyclerView?.adapter==null) vehiclesHomePresenter.getNewData()

        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_refresh_24)
        appCompatActivity.supportActionBar?.title = "Vehicles"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        //enable menu
        setHasOptionsMenu(true)

        requireActivity()
                .onBackPressedDispatcher
                .addCallback(this){
                    vehiclesHomePresenter.getNewData()
                }
    }

    override fun showProgress() {
        activity?.runOnUiThread { (activity as MainActivity).progressBar?.visibility = View.VISIBLE }
    }

    override fun hideProgress() {
        activity?.runOnUiThread { (activity as MainActivity).progressBar?.visibility = View.GONE }
    }

    override fun setVehiclesData(vehiclesData: VehiclesData) {

        activity?.runOnUiThread {
            view?.recyclerView?.adapter = VehiclesDataListAdapter(vehiclesData) {
                vehiclesHomePresenter.onItemClick(it)
                emptyView.visibility=View.GONE;
            }
        }
    }

    override fun getDataFailed(strError: String) {
        activity?.runOnUiThread {
            showToast(context!!, strError)
            emptyView.visibility=View.VISIBLE;
        }
    }

    override fun onItemClick(vehicle: Response) {
        activity?.runOnUiThread {
            val action = VehiclesDataFragmentDirections.actionVehiclesListToMap(vehicle.objectId, vehicle.plate, vehicle.timestamp)
            findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        vehiclesHomePresenter.onDestroy()
        super.onDestroy()
    }

}
