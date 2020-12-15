package com.fleetcomplete.vehicles.view

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.fleetcomplete.vehicles.R
import com.fleetcomplete.vehicles.model.locationhistory.LocationHistory
import com.fleetcomplete.vehicles.model.locationhistory.LocationHistoryInteractor
import com.fleetcomplete.vehicles.presenter.LocationHistoryPresenter
import com.fleetcomplete.vehicles.showToast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import com.google.maps.android.ui.IconGenerator
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class LocationHistoryFragment : Fragment(), OnMapReadyCallback, LocationHistoryView, View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private val args: LocationHistoryFragmentArgs by navArgs()
    private lateinit var map: GoogleMap
    private lateinit var locationHistoryPresenter: LocationHistoryPresenter
    private var latestDate: Date? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_map, container, false)

        v.mapView!!.onCreate(savedInstanceState)
        v?.mapView!!.getMapAsync(this)

        locationHistoryPresenter = LocationHistoryPresenter(this, LocationHistoryInteractor())

        //2020-12-13 02:50:11+0200
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.US)
        var date: Date? = null
        try {
            date = sdf.parse(args.timestamp)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        latestDate = date ?: Date(System.currentTimeMillis())

        val calendar = Calendar.getInstance()
        calendar.time=latestDate!!
        v.dateInput?.setText(StringBuilder()
                .append(calendar[Calendar.DAY_OF_MONTH]).append("/")
                .append(calendar[Calendar.MONTH]).append("/")
                .append(calendar[Calendar.YEAR]))

        locationHistoryPresenter.getNewData(args.objectId, latestDate as Date)

        return v
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        view?.mapView!!.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        view?.mapView!!.onResume()
        dateInput.setOnClickListener(this)
        calendarBtn.setOnClickListener(this)
        activity?.actionBar?.title=args.plate
    }

    override fun onPause() {
        super.onPause()
        dateInput.setOnClickListener(null)
        calendarBtn.setOnClickListener(null)
        view?.mapView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        view?.mapView?.onDestroy()
        locationHistoryPresenter.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        view?.mapView!!.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val estonia = LatLng(58.7, 25.7)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(estonia, 6f))
    }

    override fun showProgress() {
        //TODO
    }

    override fun hideProgress() {
        //TODO
    }

    override fun setLocationHistory(locationHistory: LocationHistory) {
        if(locationHistory.response.isNotEmpty()) activity?.runOnUiThread {
            val lineOptions = PolylineOptions()
            val builder = LatLngBounds.Builder()

            var distance = 0.00
            var previous : LatLng? = null

            locationHistory.response.forEach {
                val position = LatLng(it.Latitude, it.Longitude)
                if(previous==null) previous = position
                distance += SphericalUtil.computeDistanceBetween(previous, position)
                builder.include(position)
                lineOptions.add(position)
                previous=position;
            }

            lineOptions.width(12f)
            lineOptions.color(Color.BLUE)
            lineOptions.geodesic(true)

            map.addPolyline(lineOptions)

            var markerOptions = MarkerOptions()
            val start = LatLng(locationHistory.response[0].Latitude, locationHistory.response[0].Longitude)
            val end = LatLng(locationHistory.response[locationHistory.response.size - 1].Latitude, locationHistory.response[locationHistory.response.size - 1].Longitude)

            markerOptions.position(start)
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            markerOptions.title("Start")
            map.addMarker(markerOptions);

            //val iconFactory = IconGenerator(activity)
            //iconFactory.setColor(Color.CYAN)
            //addIcon(mMap, iconFactory, "Start", start)

            markerOptions = MarkerOptions()
            markerOptions.position(end)
            markerOptions.title("End")
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            map.addMarker(markerOptions)

            val padding = 50
            val bounds = builder.build()
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
            map.setOnMapLoadedCallback {
                map.animateCamera(cu)
            }
            distanceTextView.text= getString(R.string.distance_travelled, distance/1000)
        }
    }

    override fun getLocationHistoryFailed(strError: String) {
        activity?.runOnUiThread{
            showToast(context!!, getString(R.string.location_history_failed)+strError, Toast.LENGTH_LONG)
        }
    }

    override fun onDateChanged(date: Date) {
        map.clear()
        latestDate = date
        val calendar = Calendar.getInstance()
        calendar.time=latestDate!!
        dateInput?.setText(StringBuilder()
                .append(calendar[Calendar.DAY_OF_MONTH]).append("/")
                .append(calendar[Calendar.MONTH]).append("/")
                .append(calendar[Calendar.YEAR]))

        locationHistoryPresenter.getNewData(args.objectId, date)
    }

    private fun addIcon(googleMap: GoogleMap, iconFactory: IconGenerator, text: CharSequence, position: LatLng) {
        val markerOptions = MarkerOptions().icon(BitmapDescriptorFactory
                .fromBitmap(iconFactory.makeIcon(text as String?)))
                .position(position)
                .anchor(iconFactory.anchorU, iconFactory.anchorV)
        googleMap.addMarker(markerOptions)
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c.set(year, monthOfYear, dayOfMonth)
        onDateChanged(c.time)
    }

    override fun onClick(v: View?) {
        val calendar = Calendar.getInstance()
        calendar.time=latestDate!!
        val dialog = DatePickerDialog(context!!, this,
                calendar[Calendar.YEAR], calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH])

        dialog.show()
    }
}
