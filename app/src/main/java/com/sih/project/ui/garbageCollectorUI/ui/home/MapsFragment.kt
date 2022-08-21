package com.sih.project.ui.garbageCollectorUI.ui.home

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sih.project.R

class MapsFragment : Fragment() {

    private val latLongs = listOf(
        "Manipal University" to LatLng(26.84386, 75.5630456),
        "Temptation Jaipur" to LatLng(26.8397479, 75.5653213),
        "Highland Farms" to LatLng(26.8397479, 75.5653213),
        "Sajjan Court" to LatLng(26.8415545, 75.560807),
        "Pioneer Hospital" to LatLng(26.8345718, 75.5582138),
        "Miracle Print Pack" to LatLng(26.8309593, 75.5575507),
        "Bhanwar Singh Palace" to LatLng(26.8309593, 75.5575507),
        "Mahavidhalaya" to LatLng(26.8283203, 75.5591401),
        "Dhami" to LatLng(26.8300544, 75.5816379),
    )

    private val callback = OnMapReadyCallback { googleMap ->
        latLongs.forEach {
            googleMap.addMarker(
                MarkerOptions()
                    .position(it.second)
                    .title(it.first)
                    .draggable(false)
            )
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLongs.first().second, 14f))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}