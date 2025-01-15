package com.test.mylocationtracking

import android.location.Location
import android.os.Build

import com.google.android.gms.location.DeviceOrientation
import com.google.gson.Gson
import com.test.mylocationtracking.data.LocationData
import com.test.mylocationtracking.data.OrientationData

class Parser {

    companion object {
        fun parseLocation(location: Location) : String {
            val lat = location.latitude
            val long = location.longitude
            val alt = location.altitude
            val acc = location.accuracy
            val id = Build.ID
            val data = LocationData(id, long, lat, alt, acc)
            val gson = Gson()
            return gson.toJson(data)
        }

        fun parseOrientation(orientation: DeviceOrientation): String {
            val heading = orientation.headingDegrees
            val id = Build.ID
            val data = OrientationData(id, heading)
            val gson = Gson()
            return gson.toJson(data)
        }
    }

}