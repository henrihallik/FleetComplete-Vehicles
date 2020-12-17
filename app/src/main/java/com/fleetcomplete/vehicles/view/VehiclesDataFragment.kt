package com.fleetcomplete.vehicles.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Adapter
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
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


class VehiclesDataFragment : Fragment(), VehiclesDataView {
    private var vehiclesHomePresenter: VehiclesDataPresenter? = null
    private var binding : View? = null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
       if(binding==null){
           binding = inflater.inflate(R.layout.fragment_vehicles_data, container, false)
       }

        vehiclesHomePresenter = VehiclesDataPresenter(this)
        binding?.recyclerView?.setHasFixedSize(true)

        return binding
    }

    override fun onResume() {
        super.onResume()
        if(binding?.recyclerView?.adapter==null) vehiclesHomePresenter?.getNewData()

        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_refresh_24)
        appCompatActivity.supportActionBar?.title = appCompatActivity.getString(R.string.vehicles)
        (appCompatActivity as MainActivity).menu?.findItem(R.id.action_api_key)?.isVisible = true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requireActivity()
                .onBackPressedDispatcher
                .addCallback(this){
                    vehiclesHomePresenter?.getNewData()
                }
    }

    override fun showProgress() {
        activity?.runOnUiThread {
            progressBar?.visibility = VISIBLE
        }
    }

    override fun hideProgress() {
        activity?.runOnUiThread {
           progressBar?.visibility = GONE
        }
    }

    override fun setVehiclesData(vehiclesData: VehiclesData) {
        activity?.runOnUiThread {
            view?.recyclerView?.visibility= VISIBLE
            emptyView.visibility=GONE
            view?.recyclerView?.adapter = VehiclesDataListAdapter(vehiclesData) {
                vehiclesHomePresenter?.onItemClick(it)
            }
        }
    }

    override fun getDataFailed(strError: String) {
        activity?.runOnUiThread {
            showToast(context!!, strError)
            emptyView.visibility=VISIBLE
            view?.recyclerView?.visibility=GONE
        }
    }

    override fun onItemClick(vehicle: Response) {
        activity?.runOnUiThread {
            val action = VehiclesDataFragmentDirections.actionVehiclesListToMap(vehicle.objectId, vehicle.plate, vehicle.timestamp)
            findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        vehiclesHomePresenter?.onDestroy()
        super.onDestroy()
    }

}
