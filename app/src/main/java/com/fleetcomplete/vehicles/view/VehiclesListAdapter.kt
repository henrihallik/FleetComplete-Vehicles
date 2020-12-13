package com.fleetcomplete.vehicles.view

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fleetcomplete.vehicles.R
import com.fleetcomplete.vehicles.getTimeAgo
import com.fleetcomplete.vehicles.inflate
import com.fleetcomplete.vehicles.model.Response
import com.fleetcomplete.vehicles.model.VehiclesData
import kotlinx.android.synthetic.main.item_vehicles_list_recycler.view.*
//import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class VehiclesListAdapter(private val vehiclesUpdates: VehiclesData, private val listener: (Int) -> Unit) : RecyclerView.Adapter<VehiclesListAdapter.MyViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(inflate(parent.context, R.layout.item_vehicles_list_recycler, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(vehiclesUpdates.response[position], listener)
    }

    override fun getItemCount(): Int {
        return if(vehiclesUpdates.response!=null) vehiclesUpdates.response.size else 0
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(vehicle: Response, listener: (Int) -> Unit) = with(itemView) {
            plateDriverName.text = "${vehicle.plate} / ${vehicle.driverName}"
            speed.text = "${vehicle.speed} km/h"
            address.text = vehicle.address

            //2020-12-13 02:50:11+0200
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
            try {
                val date: Date = sdf.parse(vehicle.timestamp)
                dataAge.text = date.getTimeAgo()
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            itemView.setOnClickListener{listener(vehicle.objectId)}
        }
    }
}