package com.fleetcomplete.vehicles.model

import android.text.TextUtils
import com.fleetcomplete.vehicles.App.Companion.app
import com.google.gson.Gson
import okhttp3.*
import okhttp3.Response
import java.io.IOException
import java.net.URL

class VehiclesDataInteractor {
    interface OnFinishedListener {
        fun onResultSuccess(vehiclesData: VehiclesData)
        fun onResultFail(strError: String)
    }

    fun requestVehiclesDataAPI(onFinishedListener: OnFinishedListener) {
        val s = "https://app.ecofleet.com/seeme/Api/Vehicles/getLastData?key=${app!!.fleetCompleteApiKey}&json=true"
        val url = URL(s)

        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        app?.httpClient?.newCall(request)?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFinishedListener.onResultFail(e.message.toString())
            }
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if(!TextUtils.isEmpty(responseBody)){
                    val vehiclesData = Gson().fromJson(responseBody, VehiclesData::class.java)
                    if(vehiclesData.response!=null) {
                        onFinishedListener.onResultSuccess(vehiclesData)
                        return;
                    }
                }
                onFinishedListener.onResultFail("No data available or invalid API key")
            }
        })
    }
}