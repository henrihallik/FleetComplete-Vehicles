package com.fleetcomplete.vehicles.view

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.fleetcomplete.vehicles.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.view.*

class MapFragment : Fragment(), OnMapReadyCallback {
    val args: MapFragmentArgs by navArgs()
    private lateinit var mMap: GoogleMap

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_map, container, false)
        //view.game_title.text="${args.objectId}"

        v.mapView!!.onCreate(savedInstanceState)
        v?.mapView!!.getMapAsync(this)

        return v
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        view?.mapView!!.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        view?.mapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        view?.mapView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        view?.mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        view?.mapView!!.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val sydney = LatLng(58.7, 25.7)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10f))
    }
}
