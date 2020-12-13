package com.fleetcomplete.vehicles.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.fleetcomplete.vehicles.R
import kotlinx.android.synthetic.main.fragment_map.view.*

class MapFragment : Fragment() {
    val args: MapFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        view.game_title.text="${args.objectId}"
        return view
    }
}
