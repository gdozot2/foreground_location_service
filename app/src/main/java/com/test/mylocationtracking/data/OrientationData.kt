package com.test.mylocationtracking.data

import com.google.gson.annotations.SerializedName

class OrientationData(@SerializedName("id") var id: String,
                      @SerializedName("heading") var heading: Float)
{}