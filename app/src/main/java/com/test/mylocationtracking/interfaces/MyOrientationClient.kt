package com.test.mylocationtracking.interfaces

import com.google.android.gms.location.DeviceOrientation
import kotlinx.coroutines.flow.Flow

interface MyOrientationClient {
    fun getOrientationUpdates(interval: Long): Flow<DeviceOrientation>

    class AnyException(message: String): Exception()
}