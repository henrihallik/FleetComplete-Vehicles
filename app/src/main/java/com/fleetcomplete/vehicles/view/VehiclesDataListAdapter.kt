package com.fleetcomplete.vehicles.view

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fleetcomplete.vehicles.R
import com.fleetcomplete.vehicles.getTimeAgo
import com.fleetcomplete.vehicles.inflate
import com.fleetcomplete.vehicles.model.Response
import com.fleetcomplete.vehicles.model.VehiclesData
import kotlinx.android.synthetic.main.item_vehicles_list_recycler.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class VehiclesDataListAdapter(var vehiclesData: VehiclesData?, private val listener: (Response) -> Unit) : RecyclerView.Adapter<VehiclesDataListAdapter.MyViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(inflate(parent.context, R.layout.item_vehicles_list_recycler, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        vehiclesData?.response?.get(position)?.let { holder.bind(it, listener) }
    }

    override fun getItemCount(): Int {
        return if(vehiclesData?.response!=null) vehiclesData?.response!!.size else 0
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(vehicle: Response, listener: (Response) -> Unit) = with(itemView) {
            plateDriverName.text = StringBuilder().append(vehicle.plate)
                    .append("/").append(vehicle.driverName)
            speed.text = context.getString(R.string.speed_with_unit, vehicle.speed)
            address.text = vehicle.address

            //2020-12-13 02:50:11+0200
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.US)
            try {
                val date: Date? = sdf.parse(vehicle.timestamp)
                dataAge.text = date?.getTimeAgo()
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            itemView.setOnClickListener{listener(vehicle)}
        }
    }
}