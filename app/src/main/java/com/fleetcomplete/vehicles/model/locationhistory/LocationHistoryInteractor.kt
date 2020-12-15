package com.fleetcomplete.vehicles.model.locationhistory

import android.text.TextUtils
import com.fleetcomplete.vehicles.App.Companion.app
import com.fleetcomplete.vehicles.BuildConfig
import com.fleetcomplete.vehicles.DAY_MILLIS
import com.fleetcomplete.vehicles.model.VehiclesData
import com.google.gson.Gson
import okhttp3.*
import okhttp3.Response
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class LocationHistoryInteractor {
    companion object {
        private val TAG: String = LocationHistoryInteractor::class.java.simpleName
    }

    interface OnFinishedListener {
        fun onResultSuccess(locationHistory: LocationHistory)
        fun onResultFail(strError: String)
    }

    fun requestLocationHistory(onFinishedListener: OnFinishedListener, objectId : Int, date : Date) {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        val dayBefore = Date(date.time- DAY_MILLIS)

        val s = "https://app.ecofleet.com/seeme/Api/Vehicles/getRawData?key=${BuildConfig.FLEET_COMPLETE_API_KEY}" +
                "&json=true?key=${BuildConfig.FLEET_COMPLETE_API_KEY}" +
                "&json=true" +
                "&begTimestamp=${sdf.format(dayBefore)}" +
                "&endTimestamp=${sdf.format(date)}" +
                "&objectId=$objectId"

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
                val responseBody = response.body()?.string()
                if(!TextUtils.isEmpty(responseBody)){
                    val locationHistory = Gson().fromJson(responseBody, LocationHistory::class.java)
                    if(locationHistory.response!=null) {
                        onFinishedListener.onResultSuccess(locationHistory)
                        return;
                    }
                }
                onFinishedListener.onResultFail("No location history found")
            }
        })
    }
}