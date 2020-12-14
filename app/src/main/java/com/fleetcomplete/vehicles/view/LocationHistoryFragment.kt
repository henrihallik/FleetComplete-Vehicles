package com.fleetcomplete.vehicles.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.fleetcomplete.vehicles.R
import com.fleetcomplete.vehicles.model.locationhistory.LocationHistory
import com.fleetcomplete.vehicles.model.locationhistory.LocationHistoryInteractor
import com.fleetcomplete.vehicles.presenter.LocationHistoryPresenter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import com.google.maps.android.ui.IconGenerator
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import java.util.*


class LocationHistoryFragment : Fragment(), OnMapReadyCallback, LocationHistoryView {
    val args: LocationHistoryFragmentArgs by navArgs()
    private lateinit var mMap: GoogleMap
    private lateinit var locationHistoryPresenter: LocationHistoryPresenter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_map, container, false)
        //view.game_title.text="${args.objectId}"

        v.mapView!!.onCreate(savedInstanceState)
        v?.mapView!!.getMapAsync(this)

        locationHistoryPresenter = LocationHistoryPresenter(this, LocationHistoryInteractor())
        //v.progressBar.visibility = View.GONE

        return v
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        view?.mapView!!.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        view?.mapView!!.onResume()
        locationHistoryPresenter.getNewData(args.objectId, Date(System.currentTimeMillis()))
    }

    override fun onPause() {
        super.onPause()
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
        mMap = googleMap

        val estonia = LatLng(58.7, 25.7)
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(estonia, 5f))
    }

    override fun showProgress() {

    }

    override fun hideProgress() {

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

            mMap.addPolyline(lineOptions)

            var markerOptions = MarkerOptions()
            val start = LatLng(locationHistory.response[0].Latitude, locationHistory.response[0].Longitude)
            val end = LatLng(locationHistory.response[locationHistory.response.size - 1].Latitude, locationHistory.response[locationHistory.response.size - 1].Longitude)

            markerOptions.position(start)
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            markerOptions.title("Start")
            mMap.addMarker(markerOptions);

            //val iconFactory = IconGenerator(activity)
            //iconFactory.setColor(Color.CYAN)
            //addIcon(mMap, iconFactory, "Start", start)

            markerOptions = MarkerOptions()
            markerOptions.position(end)
            markerOptions.title("End")
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMap.addMarker(markerOptions)

            val padding = 50
            val bounds = builder.build()
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
            mMap.setOnMapLoadedCallback {
                mMap.animateCamera(cu)
            }
            distanceTextView.text="AAAAAAAAAAAAAAAAA" //getString(R.string.distance_travelled, distance/1000)
        }
    }

    override fun getLocationHistoryFailed(strError: String) {
        TODO("Not yet implemented")
    }

    override fun onDateChanged(date: Date) {
        TODO("Not yet implemented")
    }

    private fun addIcon(googleMap: GoogleMap, iconFactory: IconGenerator, text: CharSequence, position: LatLng) {
        val markerOptions = MarkerOptions().icon(BitmapDescriptorFactory
                .fromBitmap(iconFactory.makeIcon(text as String?)))
                .position(position)
                .anchor(iconFactory.anchorU, iconFactory.anchorV)
        googleMap.addMarker(markerOptions)
    }
}
