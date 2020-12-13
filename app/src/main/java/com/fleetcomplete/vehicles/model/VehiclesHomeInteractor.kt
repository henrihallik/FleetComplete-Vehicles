package com.fleetcomplete.vehicles.model

import android.text.TextUtils
import com.fleetcomplete.vehicles.BuildConfig
import com.google.gson.Gson
import okhttp3.*
import okhttp3.Response
import java.io.IOException
import java.net.URL

class VehiclesHomeInteractor {
    companion object {
        private val TAG: String = VehiclesHomeInteractor::class.java.simpleName
    }

    interface OnFinishedListener {
        fun onResultSuccess(arrVehicleUpdates: VehiclesData)
        fun onResultFail(strError: String)
    }

    fun requestVehiclesDataAPI(onFinishedListener: OnFinishedListener) {
        val client = OkHttpClient()
        val s = "https://app.ecofleet.com/seeme/Api/Vehicles/getLastData?key=${BuildConfig.FLEET_COMPLETE_API_KEY}&json=true"
        val url = URL(s)

        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFinishedListener.onResultFail("Something went wrong")
            }
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body()?.string()
                if(!TextUtils.isEmpty(responseBody)){
                    val vehiclesData = Gson().fromJson(responseBody, VehiclesData::class.java)
                    if(vehiclesData.response!=null) {
                        onFinishedListener.onResultSuccess(vehiclesData)
                        return;
                    }
                }
                onFinishedListener.onResultFail("Something went wrong")
            }
        })
    }
}