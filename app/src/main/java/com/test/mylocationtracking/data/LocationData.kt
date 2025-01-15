package com.test.mylocationtracking.data

import com.google.gson.annotations.SerializedName

class LocationData(@SerializedName("id") var id: String,
                   @SerializedName("longitude") var longitude: Double,
                   @SerializedName("latitude") var latitude: Double,
                   @SerializedName("altitude") var altitude: Double,
                   @SerializedName("accuracy") var accuracy: Float
) {}

